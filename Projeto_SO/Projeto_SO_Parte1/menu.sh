#!/bin/bash

###############################################################################
## ISCTE-IUL: Trabalho prático de Sistemas Operativos
##
## Aluno: Nº: 99437      Nome: Bruno Amaro
## Nome do Módulo: menu.sh
## Descrição/Explicação do Módulo: 
##
##
###############################################################################

while :; do     ## Loop infinito que só acaba quando o utilizador escolhe a opção 0
    clear
    echo ""
    echo "1. Listar cidadãos"
    echo "2. Adicionar enfermeiro"
    echo "3. Stats"
    echo "4. Agendar vacinação"
    echo "0. Sair"
    echo ""
 
   echo -n "Digite a sua opção:"
   read option

   if (( $option == 0 )); then
      break
   fi

   case $option in    ## Verifica que opção é escolhida 
      1)              ## Se for a opção 1 executa o ficheiro lista_cidadaos.sh
         echo ""
        ./lista_cidadaos.sh
        echo ""
        echo -n "Prima qualquer tecla para continuar"
        read tecla;;

      2)              ## Se for a opção 2 pede ao utilizador para colocar as informações necessárias e depois executa o ficheiro adiciona_enfermeiros.sh
         echo ""
         echo "Digite o nome do enfermeiro:"
         read name
         echo "Digite o nº de cédula profissional:"
         read numero 
         echo "Digite o Centro de Saúde:"
         read centro
         echo "Digite a disponibilidade do enfermeiro:"
         read dispo
         echo ""
         ./adiciona_enfermeiros.sh "$name" $numero $centro $dispo
         echo ""
         echo -n "Prima qualquer tecla para continuar"
         read tecla;;

      3)            ## Se for a opção 3 pergunta ao utlizador que opção deseja escolher e depois executa o ficheiro stats.sh com ainfromação dada
         echo ""
         echo "Digite a sua opção:  (cidadaos/registados/enfermeiros)"
         read k
         echo ""
         case $k in
           cidadaos)
              echo "Digite a localidade que pretende verificar:"
              read local
              echo ""
              ./stats.sh $k $local;;
       
          registados)
              ./stats.sh $k;;
         
          enfermeiros)
              ./stats.sh $k;;
       
          *) echo "Opção inválida";;
         esac
         echo ""
         echo -n "Prima qualquer tecla para continuar"
         read tecla;;

      4)         ## Se for a opção 4 executa o ficheiro agendamento.sh
         echo ""
         ./agendamento.sh
         echo ""
         echo -n "Prima qualquer tecla para continuar"
         read tecla;;

      *)        ## Se não for nenhuma das opçoões acima escolhidas dá erro
       echo "" 
       echo "Opção inválida, tente novamente"
       echo -n "Prima qualquer tecla para continuar"
       read tecla;; 
   esac
done
