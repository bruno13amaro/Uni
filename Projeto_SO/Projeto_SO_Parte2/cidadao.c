/******************************************************************************
 ** ISCTE-IUL: Trabalho prático de Sistemas Operativos
 **
 ** Aluno: Nº: 99437       Nome: Bruno Amaro
 ** Nome do Módulo: cidadao.c
 ** Descrição/Explicação do Módulo: 
 **
 **
 ******************************************************************************/
#include "common.h"
#include<stdlib.h>
#include<unistd.h>
#include<ctype.h>
#include<signal.h>
#include<sys/wait.h>

//Variável de espera que permite que o processo espere até poder continuar
int espera = 1;

//Criação de um novo cidadão
Cidadao new_cidadao(){
    Cidadao a;
    printf("Digite o número de utente do cidadão: ");
    scanf("%d", &a.num_utente);
    printf("Digite o nome do cidadão: ");
    my_gets(a.nome, 100);
    if(strlen(a.nome) == 0){
        erro("Erro ao introduzir o nome do cidadão");
        exit(0);
    }
    printf("Digite a idade do cidadão: ");
    scanf("%d", &a.idade);
    printf("Digite a localidade do cidadão: ");
    my_gets(a.localidade, 100);
    if(strlen(a.localidade) == 0){
        erro("Erro ao introduzir a localidade do cidadão");
        exit(0);
    }
    printf("Digite o número de telemóvel do cidadão: ");
    my_gets(a.nr_telemovel, 10);
    if(strlen(a.nr_telemovel) != 9){
        erro("Número de telemóvel introduzido inválido");
        exit(0);
    }
    a.estado_vacinacao = 0;
    sucesso("C1) Dados Cidadão: %d; %s; %d; %s; %d; 0", a.num_utente, a.nome, a.idade, a.localidade, atoi(a.nr_telemovel));
    a.PID_cidadao = getpid();
    return a;
}

//Função que permite a espera até que o ficheiro pedidovacina.txt já não exista
void handle_alarm(int signal){
    if(access("pedidovacina.txt", F_OK) == 0){
        erro("C3) Não é possivel iniciar o processo de vacinação neste momento");
        alarm(5);
    }else{
       sucesso("C3) Ficheiro FILE_PEDIDO_VACINA pode ser criado");
       espera = 0;
    }
}

//Função que permite terminar o processo porque o cidadão cancelou o processo(Ctrl-C)
void handle_sigint(int signal){
    sucesso("C5) O cidadão cancelou a vacinação, o pedido nº %d foi cancelado", getpid());
    if(access("pedidovacina.txt", F_OK) == 0) remove("pedidovacina.txt");
    exit(0);
}

//Função que é executada quando o processo recebe o sinal SIGUSR1
void handle_sig1(int signal){
    sucesso("C7) Vacinação do cidadão com o pedido nº %d em curso", getpid());
    remove("pedidovacina.txt");
}

//Função que é executada quando o processo recebe o sinal SIGUSR2
void handle_sig2(int signal){
    sucesso("C8) Vacinação do cidadão com o pedido nº %d concluida", getpid());
    exit(1);
}

//Função que é executada quando o processo recebe o sinal SIGTERM
void handle_sigterm(int signal){
    sucesso("Não é possivel vacinar o cidadão com o pedido nº %d", getpid());
    if(access("pedidovacina.txt", F_OK) == 0)  remove("pedidovacina.txt");
    exit(0);
}

int main(){
    //Cria um novo cidadão
    Cidadao a = new_cidadao();
    sucesso("C2) PID Cidadão: %d", a.PID_cidadao);

    //Verifica se já existe algum cidadão a iniciar o processo de incrição , se sim espera até pedidovacina.txt for apagado
    signal(SIGALRM, handle_alarm);
    handle_alarm(0);
    while(espera == 1) pause();

    //Guarda os dados do cidadão no ficheiro pedidovacina.txt se o passo anterior não se verificar
    FILE *fic = fopen("pedidovacina.txt", "w");
    if(fic){
        fprintf(fic, "%d:%s:%d:%s:%d:%d:%d\n", a.num_utente, a.nome, a.idade, a.localidade, atoi(a.nr_telemovel), a.estado_vacinacao, a.PID_cidadao);
        sucesso("C4) Ficheiro FILE_PEDIDO_VACINA criado e preenchido");
    }else{
        erro("C4) Não é possivel criar o ficheiro FILE_PEDIDO_VACINA");
        exit(0);
    }
    fclose(fic);

    //Armar e tratar o sinal SIGINT
    signal(SIGINT, handle_sigint);

    //Ler o pid do servidor e mandar o sinal SIGUSR1 ao processo servidor
    pid_t servidor;
    if(access("servidor.pid", F_OK) == 0){
        FILE *s = fopen("servidor.pid", "r");
        fscanf(s, "%d", &servidor);
        fclose(s);
    }else{
        erro("C6) Não existe ficheiro FILE_PID_SERVIDOR");
        remove("pedidovacina.txt");
        exit(0);
    }
    kill(servidor, SIGUSR1);
    sucesso("C6) Sinal enviado ao Servidor: %d", servidor);

    //Armar diferentes sinais
    signal(SIGUSR1, handle_sig1);
    signal(SIGUSR2, handle_sig2);
    signal(SIGTERM, handle_sigterm);

    //O processo fica em espera até receber o sinal SIGUSR2 ou SIGTERM que terminam o processo
    while(1) pause();
}
