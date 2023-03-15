#!/bin/bash

###############################################################################
## ISCTE-IUL: Trabalho prático de Sistemas Operativos
##
## Aluno: Nº: 99437      Nome: Bruno Amaro
## Nome do Módulo: lista_cidadaos.sh
## Descrição/Explicação do Módulo: 
##
##
###############################################################################

date=$( date +%Y )
i=1  ## Variável que itera o ficheiro listagem.txt
j=$(cat listagem.txt | wc -l)  ## Variável que define o número máximo de linhas do ficheiro listagem.txt 
while (( $i<=$j )); do  ## Iteração pelas diferentes pessoas presentes no ficheiro listagem.txt para a recolha dos seus dados
     u=$(( 1000+$i ))  ## Número de Utente
     n=$( cat listagem.txt | awk -F[\ :] '{print $2, $3}' | head -$i | tail -1 )  ## Nome da pessoa
     a=$( cat listagem.txt | awk -F[\ -] '{print $8}' | head -$i | tail -1 )  ## Ano de nascimento
     a=$(( date-a ))  ## Idade
     l=$( cat listagem.txt | awk -F[\ :] '{print $11}' | head -$i | tail -1 ) ## Localidade
     m=$( cat listagem.txt | awk -F[\ :] '{print $14}' | head -$i | tail -1 ) ## Número de telemóvel
     if (( $i == 1 )); then
        echo "$u:$n:$a:$l:$m:0" > cidadaos.txt ## Cria e coloca os dados em um novo ficheiro cidadaos.txt apagando o último se existisse
     else
        echo "$u:$n:$a:$l:$m:0" >> cidadaos.txt ## Coloca os dados na próxima linha disponivel do ficheiro cidadaos.txt
     fi
     i=$(( i+1 ))  
done
echo "As pessoas inscritas são:"
cat cidadaos.txt  ## Mostra o ficheiro cidadaos.txt/Lista final
