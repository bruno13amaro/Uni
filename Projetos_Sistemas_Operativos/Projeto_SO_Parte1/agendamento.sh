#!/bin/bash

###############################################################################
## ISCTE-IUL: Trabalho prático de Sistemas Operativos
##
## Aluno: Nº: 99437      Nome: Bruno Amaro
## Nome do Módulo: agendamento.sh
## Descrição/Explicação do Módulo: 
##
##
###############################################################################
if [ -e agenda.txt ]; then  ## Verifica se existe o ficheiro agenda.txt se sim apaga-o
      rm agenda.txt
fi

if [ -e cidadaos.txt ]; then
   if [ -e enfermeiros.txt ]; then  ## Verifica se existe o ficheiro enfemeiros.txt se não avança para o else
      cat enfermeiros.txt | awk '/:1$/' > dia.txt  ## Verifica se existem enfermeiros disponiveis, se sim coloca as suas infromações no ficheiro dia.txt
      if [ -e dia.txt ]; then
         i=1
         j=$( cat dia.txt | wc -l )
         while (( $i<=$j )); do
               c=$( cat dia.txt | awk -F[\ :] '{print $4}' | head -$i | tail -1 )  ## Guarda o Centro de Saúde do médico     
               cidade=$( echo ${c:2} )  ## Guarda a localidade do Centro de Saúde
               cat cidadaos.txt | grep $cidade > pessoas.txt  ## Verifica se exitem pessoas inscritas na localidade do enfermeiro disponivel, se sim coloca as asuas informações no ficheiro pessoas.txt
               if [ -e pessoas.txt ]; then
                  cedula=$( cat dia.txt | awk -F[\ :] '{print $1}' | head -$i | tail -1 ) ## Guarda o numero de cedula do enfrmeiro
                  enfermeiro=$( cat dia.txt | awk -F[\ :] '{print $2, $3}' | head -$i | tail -1 ) ## Guarda o nome do enfermeiro
                  date=$( date +%F ) ## Guarda a data
                  a=1
                  b=$( cat pessoas.txt | wc -l )
                  while (( a<=b )); do
                        pessoa=$( cat pessoas.txt | awk -F[\ :] '{print $2, $3}' | head -$a | tail -1 )  ## Guarda o nome da pessoa
                        utente=$( cat pessoas.txt | awk -F[\ :] '{print $1}' | head -$a | tail -1 )   ## Guarda o número de utente da pessoa
                        echo "$enfermeiro:$cedula:$pessoa:$utente:$c:$date" >> agenda.txt  ## Coloca as informações no ficheiro agenda.txt
                        a=$(( $a+1 ))
                  done
                  rm pessoas.txt 
               fi
               i=$(( $i+1 ))
          done
          rm dia.txt
      else ## Não existem enfermeiros disponiveis
          echo "Não existem enfermeiros disponiveis"
          exit 2
      fi
   else  ## Não existem enfermeiros inscritos
      echo "Não existem enfermeiros inscritos"
   exit 3
   fi
else
   echo "Não existem cidadãos inscritos"
fi
cat agenda.txt

