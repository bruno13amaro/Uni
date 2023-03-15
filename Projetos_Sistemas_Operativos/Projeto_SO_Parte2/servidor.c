/******************************************************************************
 ** ISCTE-IUL: Trabalho prático de Sistemas Operativos
 **
 ** Aluno: Nº: 99437      Nome: Bruno Amaro
 ** Nome do Módulo: servidor.c
 ** Descrição/Explicação do Módulo: Não consegui implementar a parte em que o 
 ** filho recebe o sinal SIGTERM e manda outro sinal SIGTERM para o processo 
 ** cidadão correspondente e implementei de forma diferente, fazendo que seja
 ** o pai a mandar um sinal SIGTERM aos processos filho e cidadão.
 ** Na função handle_sigterm_filho o que está comentado é o que não funcionou.
 ** 
 ******************************************************************************/
#include "common.h"
#include<stdlib.h>
#include<unistd.h>
#include<signal.h>
#include<sys/wait.h>

Vaga vagas[NUM_VAGAS];
int tamanho;
Enfermeiro *enfermeiros;

//Função que corre quando cada processo filho recebe um sinal SIGTERM
void handle_sigterm_filho(int signal){
    sucesso("S5.6.1) SIGTERM recebido, servidor dedicado termina Cidadão");
    //for(int i = 0; i < NUM_VAGAS; i ++){
        //if(vagas[i].PID_filho == getpid()){
            //kill(vagas[i].cidadao.PID_cidadao, SIGTERM);
            //break;
        //}
    //}
    exit(0);
}

//Função que corre quando o processo pai recebe um sinal SIGINT
void handle_sigint(int signal){
    for(int i = 0; i < NUM_VAGAS; i ++)
        if(vagas[i].index_enfermeiro != -1){
            kill(vagas[i].cidadao.PID_cidadao, SIGTERM);
            kill(vagas[i].PID_filho, SIGTERM);
        }
    free(enfermeiros);
    remove("servidor.pid");
    sucesso("S6) Servidor terminado");
    exit(0);
}

//Função que corre quando o processo pai recebe um sinal SIGUSR1
void handle_sig(int sinal){
    //Lê e guarda os dados do novo cidadão
    Cidadao b;
    FILE *fic = fopen("pedidovacina.txt", "r");
    if(fic){
        fscanf(fic, "%d:%99[^:]:%d:%99[^:]:%9[^:]:%d:%d\n",&b.num_utente, b.nome, &b.idade, b.localidade, b.nr_telemovel, &b.estado_vacinacao, &b.PID_cidadao);
        fclose(fic);
        sucesso("S5.1) Dados Cidadão: %d; %s; %d; %s; %d; 0", b.num_utente, b.nome, b.idade, b.localidade, atoi(b.nr_telemovel));
    }else{
        if(fic == NULL) erro("S5.1) Não foi possivel abrir o ficheiro FILE_PEDIDO_VACINA");
        else erro("S5.1) Não foi possivel ler o ficheiro FILE_PEDIDO_VACINA");
    }
    printf("Chegou o cidadão com o pedido nº %d, com nº utente %d, para ser vacinado no Centro de Saúde %s.\n", b.PID_cidadao, b.num_utente, b.localidade);
    //Verifica se o enfermeiro está disponivel, se sim verifica se há vagas
    int a, c;
    for(int i = 0; i < tamanho; i ++){
        char local[100];
        for(int k = 2; k < 100; k ++) local[k-2] = enfermeiros[i].CS_enfermeiro[k];
        if(i == tamanho - 1){
            if(strcmp(local, b.localidade) != 0){
                erro("Não existe Centro de Saúde na localidade do cidadão");
                kill(b.PID_cidadao, SIGTERM);
                return;
            }
        }
        if(strcmp(local, b.localidade) == 0){
            a = i;
            if(enfermeiros[i].disponibilidade == 1){
                sucesso("S5.2.1) Enfermeiro %d disponivel para o pedido %d", enfermeiros[i].ced_profissional, b.PID_cidadao);
                for(int j = 0; j < NUM_VAGAS; j ++){
                    if(vagas[j].index_enfermeiro == -1){
                        c = j;
                        sucesso("S5.2.2) Há vaga para o pedido %d", b.PID_cidadao);
                        break;
                    }
                }  
            break;
            }else{
                erro("S5.2.1) Enfermeiro %d indisponivel para o pedido %d para o Centro de Saúde %s", enfermeiros[i].ced_profissional, b.PID_cidadao, enfermeiros[i].CS_enfermeiro);
                kill(b.PID_cidadao, SIGTERM);
                return;
            }
        }
    }
    if( c == NUM_VAGAS - 1){
        if(vagas[c].index_enfermeiro != -1){
            erro("S5.2.2) Não há vaga para a vacinação para o pedido %d", b.PID_cidadao);
            kill(b.PID_cidadao, SIGTERM);
            return;
        }
    }
    //Se os dois pontos anteriores se verificam, há atualização da disponibilidade do enfermeiro e do vetor vagas
    vagas[c].index_enfermeiro = a;
    vagas[c].cidadao = b;
    enfermeiros[a].disponibilidade = 0;
    sucesso("S5.3) Vaga nº %d preenchida para o pedido %d", c, b.PID_cidadao);
    //Criação do processo filho que cuida da vacinação do cidadão
    pid_t pid = fork();
    //Acaba caso a criação do processo filho teve algum erro
    if(pid < 0){
        erro("S5.4) Não foi possivel criar o servidor dedicado");
        kill(SIGTERM, b.PID_cidadao);
        return;
    }
    //Consulta
    if(pid == 0){
        signal(SIGTERM, handle_sigterm_filho);
        sucesso("S5.4) Servior dedicado %d criado para o pedido %d", getpid(), b.PID_cidadao);
        kill(b.PID_cidadao, SIGUSR1);
        sucesso("S5.6.2) Servidor dedicado inicia consulta de vacinação");
        sleep(TEMPO_CONSULTA);
        sucesso("S.6.3) Vacinação terminada para o cidadão com o pedido nº %d", b.PID_cidadao);
        kill(b.PID_cidadao, SIGUSR2);
        sucesso("S5.6.4) Servidor dedicado termina consulta de vacinação");
        exit(1);
    //O processo pai atualiza o vetor vagas com o pid do processo filho criado
    }else{
        sucesso("S5.5.1) Servidor dedicado %d na vaga %d", pid, c);
        vagas[c].PID_filho = pid;
        sucesso("S5.5.2) Servidor aguarda fim do servidor dedicado %d", pid);
    }
}

//Função que corre quando o processo filho acaba a consulta
void handle_child(int signal){
    //Procura pelo processo filho/enfermeiro qua acabaram a consulta
    int pid = wait(NULL);
    int j;
    for(int i = 0; i < NUM_VAGAS; i ++){
        if(vagas[i].PID_filho == pid){
            j = vagas[i].index_enfermeiro;
            vagas[i].index_enfermeiro = -1;
            sucesso("S5.5.3.1) Vaga %d que era do servidor dedicado %d libertada", i, pid);
            break;
        }
    }
    //Atualização das informações do enfermeiros que realizou a consulta
    enfermeiros[j].disponibilidade = 1;
    sucesso("S.5.5.3.2) Enfermeiro %d atualizado para disponivel", j);
    enfermeiros[j].num_vac_dadas ++;
    sucesso("S5.5.3.3) Enfermeiro %d atualizado para %d vacinas dadas", j, enfermeiros[j].num_vac_dadas);
    FILE *a = fopen("enfermeiros.dat", "r+");
    if(a == NULL){
        printf("Erro ao abrir o ficheiro enfermeiros.dat\n");
        return;
    }
    fseek(a, j*sizeof(Enfermeiro), SEEK_SET);
    fwrite(&enfermeiros[j], sizeof(Enfermeiro), 1, a);
    sucesso("S5.5.3.4) Ficheiro FILE_ENFERMEIROS %d atualizado para %d vacinas dadas", j, enfermeiros[j].num_vac_dadas);
    fclose(a);
    sucesso("S5.5.3.5 Retorna");
}

int main () {
    // Regista o seu pid no ficheiro servidor.pid
    FILE *fic = fopen("servidor.pid", "w");
    if(fic == NULL){
        erro("S1) Não consegui registar o servidor");
        exit(0);
    }
    fprintf(fic, "%d\n", getpid());
    sucesso("S1) Escrevi no ficheiro FILE_PID_SERVIDOR o PID: %d", getpid());
    fclose(fic);

    //Guarda a informação de todos os enfermeiros num vetor
    FILE *fib = fopen("enfermeiros.dat", "r");
    if(fib){
        fseek(fib, 0, SEEK_END);
        long size = ftell(fib);
        tamanho = size / sizeof(Enfermeiro);
        sucesso("S2) Ficheiro FILE_ENFERMEIROS tem %d bytes, ou seja, %d enfermeiros", size, tamanho);
        fseek(fib, 0, SEEK_SET);
        enfermeiros = (Enfermeiro *) malloc(tamanho * sizeof(Enfermeiro));
        if(!enfermeiros){
           printf("ERRO: A alocação de memória falhou\n");
           exit(0);
       }
       int i = 0;
       Enfermeiro new;
       while(fread(&new, sizeof(new), 1, fib) > 0){
             enfermeiros[i] = new;
             i ++;
       }
       fclose(fib);
    }else{
        erro("S2) Não consegui ler o ficheiro FILE_ENFERMEIROS!");
        exit (0);
    }

    //Limpa a lista de vagas
    for(int j = 0; j < NUM_VAGAS; j ++) vagas[j].index_enfermeiro = -1;
    sucesso("S3) Iniciei a lista de %d vagas", NUM_VAGAS);

    signal(SIGUSR1, handle_sig);
    sucesso("S4) Servidor espera pedidos");
    signal(SIGCHLD, handle_child);
    signal(SIGINT, handle_sigint);
    
    while(1) pause();
}