#!/bin/bash

###############################################################################
## ISCTE-IUL: Trabalho prático de Sistemas Operativos
##
## Aluno: Nº: 99437      Nome: Bruno Amaro
## Nome do Módulo: stats.sh
## Descrição/Explicação do Módulo: 
##
##
###############################################################################

if (( $# < 1 || $# > 2 )); then  ##Verificar se o número de Argumentos é o correto
   echo "Erro:Sintaxe:$0 cidadaos <localidade> / registados / enfermeiros"
fi

case $1 in
   cidadaos)  ## No caso do Argumento ser cidadaos
       if (( $# != 2 )); then   ## Verifica se o primeiro argumento é acompanhado de outro que é a localidade
           echo "Erro:Sintaxe:$0 cidadaos <localidade>"
           exit 1
       fi
       n=$( cat cidadaos.txt | grep $2 | wc -l )  ## Guardar o número de pessoas da localidade pretendida
       echo "O número de cidadãos inscritos em $2 é $n";;
   
   registados)  ## No caso do argumento ser registados
       i=1 ## Variável para percorrer as linhas do ficheiro cidadaos.txt
       j=$( cat cidadaos.txt | wc -l ) ## Variável que indica a ultima linha a ser percorrida
       while (( $i<=$j )); do
             a=$( cat cidadaos.txt | awk -F[\ :] '{print $4}' | head -$i | tail -1 ) ## Variável que guarda a idade da pessoa
             if (( a >= 60 )); then  ## Verifica se a idade é superior a 60 e se for guarda-se as informações necesárias sobre as pessoas e guarda-se num ficheiro
                n=$( cat cidadaos.txt | awk -F[\ :] '{print $2, $3}' | head -$i | tail -1 ) ## Guarda o nome
                u=$( cat cidadaos.txt | awk -F[\ :] '{print $1}' | head -$i | tail -1 )    ## Guarda o número de utente
                echo "$n nº$u $a" >> registados.txt 
             fi
             i=$(( $i+1 ))
      done
      if [ -e registados.txt ]; then  ## Verifica se o ficheiro existe
         echo "Os cidadãos inscritos com idade superior ou igual a 60 são:"
         cat registados.txt | sort -k 4 -rn | awk '{print $1, $2, $3}'  ## Coloca os cidadãos por ordem decrescente tendo em conta a idade
         rm registados.txt ## Apaga o ficheiro que já não é preciso
      else  ## Se o ficheiro não existir significa que não existem pessoas com mais de 60 anos
         echo "Não existem cidadãos com idade igual ou superior a 60 anos inscritos"
      fi;;
  
   enfermeiros)  ## No caso do Argumento ser enfermeiros
      x=1 ## Variável para percorrer as linhas do ficheiro cidadaos.txt
      y=$( cat enfermeiros.txt | wc -l )  ## Variável que indica a ultima linha a ser percorrida
      while (( $x <= $y )); do
            d=$( cat enfermeiros.txt | awk -F[\ :] '{print $6}' | head -$x | tail -1 )  ## Guarda a disponibilidade do enfermeiro
            if [[ $d -eq 1 ]]; then ## Se o enfermeiro estiver disponivel guarda-se a informação necessária num ficheiro
               m=$( cat enfermeiros.txt | awk -F[\ :] '{print $2, $3}' | head -$x | tail -1 ) ## Guarda o nome 
               echo "$m" >> pronto.txt
            fi
            x=$(( x+1 ))
            done
      if [ -e pronto.txt ]; then ## Verifica se o tal ficheiro existe
            echo "Os enfermeiros disponiveis são:"
            cat pronto.txt ## Mostra o conteúdo do ficheiro  
            rm pronto.txt  ## Apaga o ficheiro que já não é preciso 
      else  ## Se o ficheiro não existir signiica que não existem médicos disponiveis
            echo "Não existem enfermeiros disponiveis"
      fi;;

    *) echo "Erro:Sintaxe:$0 cidadaos <localidade> / registados / enfermeiros";;  ## Qualquer outro tipo de argumentos não é aceite
                      
esac
       
