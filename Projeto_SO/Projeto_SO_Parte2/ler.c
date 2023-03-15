#include<stdio.h>
#include "common.h"

int main(){
    FILE *fic = fopen("enfermeiros.dat", "r");
    Enfermeiro new;
    while(fread(&new, sizeof(new), 1, fic) != NULL)
          printf("%d:%s:%s:%d:%d\n", new.ced_profissional, new.nome, new.CS_enfermeiro, new.num_vac_dadas, new.disponibilidade);
}