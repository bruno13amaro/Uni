#include "common.h"
#include<stdlib.h>
#include<unistd.h>
#include<ctype.h>
#include<signal.h>
#include<sys/wait.h>
int n = 1;


void handle_um(int signal){
    printf("Ola\n");
    exit(1);
}

int main(){
    pid_t pid = fork();
    if(pid == 0){
        while(n == 1) pause();
    }else{
        kill(pid, SIGKILL);
        signal(SIGCHLD, handle_um);
    }
}