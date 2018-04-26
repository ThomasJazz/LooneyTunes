#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

// only for windows//
//-----------------//
#ifdef __unix__
# include <unistd.h>
#elif defined _WIN32
# include <windows.h>
#define sleep(x) Sleep(1000 * x)
#endif
//-----------------//

#define N 3
#define TWEETY 0
#define MUTTLEY 1

typedef struct thread {
     pthread_t thread_id;
     int       thread_num,id;
     int copy_COUNT_GLOBAL;
     char name[15];
     int condition,position,copy_FINISH_LINE;
} ThreadData;

pthread_mutex_t toon_signal_mutex;

struct mutex_shared{
int CONDITION_TOON,WHOLE_CYCLE,COUNT_GLOBAL,FROZEN[2],FINISH_LINE,TOON_POSITION[2];
char WINNER[15];
}SHARED;

void print_board_vertical(int toon_pos[]){
    char toon_letter[2]={'T','M'};
	for(int row= 0 ; row<10; row++){
		for(int agent=0;agent<2;agent++){
			printf("|");
			if(toon_pos[agent]==row){
				printf("%c|",toon_letter[agent]);
			}else{
				printf(" |");
			}
		}
        printf("\n");
	}
}
void print_board_horizontal(int toon_pos[]){
    char toon_letter[2]={'T','M'};
    for(int column= 0 ; column<10; column++){
        printf("_ ");
    }
    for(int agent=0;agent<2;agent++){
        printf("\n");
        for(int column= 0 ; column<10; column++){
			if(toon_pos[agent]==column){
				printf("%c ",toon_letter[agent]);
			}else{
				printf("_ ");
			}
		}
	}
}

int getRandom(int rangeLow, int rangeHigh) {
    double myRand = rand()/(1.0 + RAND_MAX);
    int range = rangeHigh - rangeLow + 1;
    int myRand_scaled = (myRand * range) + rangeLow;
    return myRand_scaled;
}

void toon_signal(ThreadData *toon){
   pthread_mutex_lock(&toon_signal_mutex);

		if(SHARED.CONDITION_TOON==toon->condition){
			SHARED.CONDITION_TOON=toon->id;
		    printf("\nCOUNT_GLOBAL = %d \t toon->name = %s \t| next CONDITION_TOON = %d\n",
		    		             SHARED.COUNT_GLOBAL,toon->name,SHARED.CONDITION_TOON);

            //if(toon->name == "Marvin")
            if(strcmp(toon->name,"Marvin") == 0){
                SHARED.WHOLE_CYCLE++;
                if(SHARED.WHOLE_CYCLE%2 == 0){//even
                    SHARED.FROZEN[TWEETY]=getRandom(0,1);
                    SHARED.FROZEN[MUTTLEY]=getRandom(0,1);
                    printf("%s can shoot, timing is even - FROZEN[TWEETY] = %d  - FROZEN[MUTTLEY] = %d\n",toon->name,SHARED.FROZEN[TWEETY],SHARED.FROZEN[MUTTLEY]);
                }
                print_board_horizontal(SHARED.TOON_POSITION);
                //print_board_vertical(TOON_POSITION);
                if(SHARED.FINISH_LINE){
			    		toon->copy_FINISH_LINE=SHARED.FINISH_LINE;
			    		if(SHARED.TOON_POSITION[TWEETY]==SHARED.TOON_POSITION[MUTTLEY]){
                            strcpy(SHARED.WINNER,"IT IS A TIE!");
			    		}
                        printf("\n WINNER = %s\n",SHARED.WINNER);

                }
            }else{
			    	if(!SHARED.FROZEN[toon->id]){
			    		toon->position++;
			    		SHARED.TOON_POSITION[toon->id]=toon->position;
			    		printf("%s move position : %d",toon->name,toon->position);
			    	}else{
			    		printf("%s was frozen, don't move position : %d",toon->name,toon->position);
			    		SHARED.FROZEN[toon->id]--;
			    	}
			    	if(toon->position>=9||SHARED.FINISH_LINE==1){
                        if(!SHARED.FINISH_LINE){
                            strcpy(SHARED.WINNER,toon->name);
                        }
			    		toon->copy_FINISH_LINE=SHARED.FINISH_LINE=1;
			    	}
            }
		    SHARED.COUNT_GLOBAL++;
		    toon->copy_COUNT_GLOBAL=SHARED.COUNT_GLOBAL;
		}
    pthread_mutex_unlock(&toon_signal_mutex);
}

void setup_time_seed(){
   struct timeval time;
   gettimeofday(&time, NULL);
   //srand for windows instead
   srandom((unsigned int) time.tv_usec);
}

void *run_API(void *thread)
{
   ThreadData *toon  = (ThreadData*)thread;
   setup_time_seed();

   while(!toon->copy_FINISH_LINE){
		toon_signal(toon);
		sleep(2);
   }

   pthread_exit(NULL);
}

void init_data(ThreadData *toon){
    SHARED.WHOLE_CYCLE=0;
    SHARED.COUNT_GLOBAL=0;
	SHARED.CONDITION_TOON=2;
	SHARED.FINISH_LINE=0;
	SHARED.TOON_POSITION[0]=SHARED.TOON_POSITION[1]=0;
	print_board_horizontal(SHARED.TOON_POSITION);

	toon[0].condition=2;
	toon[0].id=0;
	toon[0].position=0;
	toon[0].copy_COUNT_GLOBAL=SHARED.COUNT_GLOBAL;
	toon[0].copy_FINISH_LINE=SHARED.FINISH_LINE;
	strcpy(toon[0].name, "Tweety");

	toon[1].id=1;
	toon[1].condition=0;
	toon[1].position=0;
	toon[1].copy_COUNT_GLOBAL=SHARED.COUNT_GLOBAL;
	toon[1].copy_FINISH_LINE=SHARED.FINISH_LINE;
	strcpy(toon[1].name, "Muttley");

	toon[2].id=2;
	toon[2].condition=1;
	toon[2].position=0;
	toon[2].copy_COUNT_GLOBAL=SHARED.COUNT_GLOBAL;
	toon[2].copy_FINISH_LINE=SHARED.FINISH_LINE;
	strcpy(toon[2].name, "Marvin");
}

int main(int argc, char *argv[])
{
   int i;
   ThreadData thread[N];
   init_data(thread);

    for(i=0;i<N;i++){
        printf("\n toon[%d].condition=%d\n",thread[i].id,thread[i].condition);
    }

   pthread_mutex_init(&toon_signal_mutex, NULL);

	   for(i=0; i<N; i++)
	   {  thread[i].thread_num=i;
		  pthread_create(&(thread[i].thread_id), NULL, run_API, (void *)(&thread[i]));
	   }

	   for (i = 0; i < N; i++){
		   pthread_join(thread[i].thread_id, NULL);
	   }

   pthread_mutex_destroy(&toon_signal_mutex);
   return 0;
}
