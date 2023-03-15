import javafx.application.Application.launch

import scala.io.StdIn.readLine

object TextInterface{


  def main(args: Array[String]): Unit ={
    launch(classOf[Main])
  }

  def mainloop(tree: oct): oct = {
    IO_Utils.showMenu()
    val opcao = IO_Utils.readOpção()
    opcao match{
      case 1 =>
        print("Digite o nome do ficheiro: ")
        val ficheiro = readLine()
        try{
          val dataFile = IO_Utils.readFile(ficheiro)
          mainloop(Tree.create(((0.0,0.0,0.0),dataFile._1), dataFile._2))
        }
        catch{
          case _ =>
            System.err.println("Problema com o ficheiro!!")
            mainloop(tree)
        }
      case 2 =>
        tree
      case 3 =>
        if(tree.root == OcEmpty) {
          System.err.println("Operação indisponivel, octree vazia")
          mainloop(tree)
        }else{
        IO_Utils.showScale()
        val scale = IO_Utils.readOpção()
        scale match{
          case 1 => mainloop(tree.scale(2.0))
          case 2 => mainloop(tree.scale(0.5))
          case 3 => mainloop(tree)
          case _ =>
            println("Opção inválida, tente novamente")
            mainloop(tree)
        }}
      case 4 =>
        if(tree.root == OcEmpty) {
          System.err.println("Operação indisponivel, octree vazia")
          mainloop(tree)
        }else{
        IO_Utils.showColorFunctions()
        val funcao = IO_Utils.readOpção()
        funcao match{
          case 1 => mainloop(tree.mapColourEffect(Functions.sepiaFilter))
          case 2 => mainloop(tree.mapColourEffect(Functions.greenRemove))
          case 3 => mainloop(tree)
          case _ =>
            println("Opção inválida, tente novamente")
            mainloop(tree)
        }}
      case 5 =>
        if(tree.root == OcEmpty) {
          System.err.println("Operação indisponivel, octree vazia")
          mainloop(tree)
        }else{
        print("Digite o nome do ficheiro: ")
        val ficheiro = readLine()
        try{
          IO_Utils.shapesToFile(ficheiro, tree.getShapes, tree.getSize)
          mainloop(tree)
        }
        catch{
          case _ =>
            System.err.println("Problema com o ficheiro")
            mainloop(tree)
        }}
      case _ =>
        System.err.println("Opção inválida, tente novamente")
        mainloop(tree)
    }
  }

}
