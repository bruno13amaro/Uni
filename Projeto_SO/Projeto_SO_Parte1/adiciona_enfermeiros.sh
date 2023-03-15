#!/bin/bash

###############################################################################
## ISCTE-IUL: Trabalho prático de Sistemas Operativos
##
## Aluno: Nº: 99437      Nome: Bruno Amaro
## Nome do Módulo: adiciona_enfermeiros.sh
## Descrição/Explicação do Módulo: 
##
##
###############################################################################

if (( $# != 4 )); then  ## Verifica se o número de Argumentos é o correto
   echo "Erro:Sintaxe:$0 <nome> <número de cédula profissional> <Centro de Saúde associado> <disponibilidade>"
   exit 1
fi

h=$( echo $2 | awk '/[a-zA-Z]/' )

if [[ -z $1 ]]; then  ## Verifica se o nome não é uma string vazia
  echo "Erro: nome inválido"
  exit 1
fi

if [[ -n $h ]]; then  ## Verifica se o número de cédula não possui outros caracteres sem ser números
   echo "Erro: número de cédula inválido"
   exit 1
fi

if (( $4 != 1 && $4 != 0 )); then   ## Verifica se a disponibilidade é válida
   echo "Erro: A disponibilidade só pode ser 1 ou 0"
   exit 1
fi

if [ -e enfermeiros.txt ]; then  ##Verifica se existe o ficheiro enfermeiros.txt se não existir avança para o else
 i=1   ## Variável que vai percorrer o ficheiro       
 j=$( cat enfermeiros.txt | wc -l )  ## Variável que define o numero da ultima linha a percorrer
 while (( $i <= $j )); do
       c=$( cat enfermeiros.txt | awk -F[\ :] '{print $4}' | head -$i | tail -1 ) ## Variável que guarda o Centro de Saúde do enfermeiro da linha
       if [[ $3 = $c ]]; then  ## Verifica se os Centros de Saúde são iguais se sim, o script termina
          echo "Erro: O Centro de Saúde introduzido já tem um enfermeiro registado"
          exit 2
       fi
       u=$( cat enfermeiros.txt | awk -F[\ :] '{print$1}' | head -$i | tail -1 )  ##Variável que guarda o número de cédula do enfermeiro da linha
       if (( $2 == $u )); then ## Verifica se os números de cédulas são iguais se são o script termina
          echo "Erro: Enfermeiro inscrito em outro Centro de Saúde"
          exit 3
      fi
      i=$(( $i+1 ))
 done
 echo "$2:$1:$3:0:$4" >> enfermeiros.txt ## O enfermeiro passou os "testes" e é inscrito no ficheiro enfermeiros.txt
else
 echo "$2:$1:$3:0:$4" > enfermeiros.txt  ## O ficheiro enfermeiros.txt não existia logo o enfermeiro pode logo ser inscrito após a criação do ficheiro
fi
echo "Os enfermeiros inscritos são:"
cat enfermeiros.txt


