import javafx.scene.Node
import javafx.scene.shape._
import java.io.PrintWriter
import scala.io.StdIn.readLine
import scala.io._


object IO_Utils{

  // Funcao para T1
  def readFile(name: String): (Double,List[Shape3D])={
    val source = Source.fromFile(name)
    var size:Double = -1
    var bool:Boolean = true
    var result = List[Shape3D]()
    for(line <- source.getLines()){
      if(bool){
        size = line.toDouble
        bool = false
      }else
        try {
          result = result :+ (Functions.criar_shape(line.split(" ")))
        }
        catch {
          case _ => throw new IllegalArgumentException
        }
    }
    (size, result)
  }


  def shapesToFile(file: String, shapes: List[Node], size: Double): Unit = {
    val writer = new PrintWriter(file)
    writer.println(size)
    shapes.map(x => writer.println(Functions.shapeToLine(x)))
    writer.close()
  }

  // Funcoes para T6
  def showMenu(): Unit= {
    println("\nMenu:")
    println("1. Criar")
    println("2. Visualizar")
    println("3. Mudar escala")
    println("4. Aplicar função às shapes")
    println("5. Save")
    print("Opção: ")
  }


  def showScale():Unit = {
    println("\nEscala:")
    println("1. Aumentar(x2.0)")
    println("2. Diminuir(x0.5)")
    println("3. Voltar")
    print("Opção: ")
  }

  def showColorFunctions():Unit = {
    println("\nFunções:")
    println("1. Sepia Filter")
    println("2. Green Remove")
    println("3. Voltar")
    print("Opção: ")
  }

  def readOpção(): Int = {
    readLine().toInt
  }




}


