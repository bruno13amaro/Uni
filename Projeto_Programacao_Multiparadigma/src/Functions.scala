import javafx.scene.paint.{Color, PhongMaterial}
import javafx.scene.{Group, Node}
import javafx.scene.shape.{Box, Cylinder, DrawMode, Shape3D}

import scala.annotation.tailrec

object Functions {

  val redMaterial = new PhongMaterial()
  redMaterial.setDiffuseColor(Color.rgb(150,0,0))

  val whiteMaterial = new PhongMaterial()
  whiteMaterial.setDiffuseColor(Color.rgb(255,255,255))

  @tailrec
  def putShapes(shapes: List[Node], worldRoot: Group): Unit= {
    if(!shapes.isEmpty)
      shapes match{
        case x :: Nil => worldRoot.getChildren.add(x)
        case x :: tail =>
          worldRoot.getChildren.add(x)
          putShapes(tail, worldRoot)
      }
  }

  @tailrec
  def putParticoes(particoes: List[Box], worldRoot: Group): Unit = {
    if(!particoes.isEmpty)
      particoes match{
        case x :: Nil =>
          x.setMaterial(redMaterial)
          x.setDrawMode(DrawMode.LINE)
          worldRoot.getChildren.add(x)
        case x :: tail =>
          x.setMaterial(redMaterial)
          x.setDrawMode(DrawMode.LINE)
          worldRoot.getChildren.add(x)
          putParticoes(tail, worldRoot)
    }
  }

  def sepiaFilter(color : Color) : Color = {
    val colorR = color.getRed*255
    val colorG = color.getGreen*255
    val colorB = color.getBlue*255
    val r = (colorR * 0.39) + (colorG * 0.77) + (colorB * 0.19)
    val g = (colorR * 0.35) + (colorG * 0.69) + (colorB * 0.17)
    val b = (colorR * 0.27) + (colorG * 0.53) + (colorB * 0.13)
    Color.rgb(Math.min(r.toInt,255),Math.min(g.toInt,255),Math.min(b.toInt,255))
  }


  // Funcao para T5
  def greenRemove(c : Color) : Color = {
    Color.rgb((c.getRed*255).toInt,0,(c.getBlue*255).toInt)
  }


  def roam(x: Node, cam: Cylinder): Unit = {
    if(x.isInstanceOf[Box]){
      if(x.asInstanceOf[Shape3D].getDrawMode == DrawMode.LINE)
        if (x.asInstanceOf[Shape3D].getMaterial != whiteMaterial && x.asInstanceOf[Shape3D].getBoundsInParent.intersects(cam.getBoundsInParent)) {
          x.asInstanceOf[Shape3D].setMaterial(whiteMaterial)
        }
        else if (x.asInstanceOf[Shape3D].getMaterial == whiteMaterial && !x.asInstanceOf[Shape3D].getBoundsInParent.intersects(cam.getBoundsInParent) ) {
          x.asInstanceOf[Shape3D].setMaterial(redMaterial)
        }
    }
  }

  @tailrec
  def updateParticoes(grp: Group, cam: Cylinder, indice: Int, length: Int): Unit = {
    if(indice < length - 1) {
      roam(grp.getChildren.get(indice),cam)
      updateParticoes(grp, cam,indice+1, length)
    }else if(indice < length)
      roam(grp.getChildren.get(indice),cam)
  }

  // Funcao para T1
  def criar_shape(data: Array[String]): Shape3D = {
    val shape = obter_shape(data(0))
    val aux = data(1).substring(1,data(1).length-1).split(",")
    val color = new PhongMaterial(Color.rgb(aux(0).toInt,aux(1).toInt,aux(2).toInt))
    shape.setMaterial(color)
    shape.setTranslateX(data(2).toDouble)
    shape.setTranslateY(data(3).toDouble)
    shape.setTranslateZ(data(4).toDouble)
    shape.setScaleX(data(5).toDouble)
    shape.setScaleY(data(6).toDouble)
    shape.setScaleZ(data(7).toDouble)
    shape
  }

  // Funcao para T1
  def obter_shape(tipo: String): Shape3D={
    tipo match{
      case "Cylinder" => new Cylinder(0.5,1,10)
      case "Box" => new Box(1,1,1)
      case _ => throw new IllegalArgumentException()
    }
  }

  // Funcao para T3,4
  def update(tipo: String, root: Group): Unit= {
    tipo match{
      case "filter" =>
        root.getChildren.removeIf(x => x.isInstanceOf[Shape3D] && x.asInstanceOf[Shape3D].getDrawMode == DrawMode.FILL)
        putShapes(OctreeCriacao.tree.getShapes, root)
      case "scale" =>
        root.getChildren.removeIf(x => x.isInstanceOf[Shape3D] && x.asInstanceOf[Shape3D].getDrawMode == DrawMode.FILL || (x.isInstanceOf[Box] ))
        putShapes(OctreeCriacao.tree.getShapes, root)
        putParticoes(OctreeCriacao.tree.getCubos, root)
        updateParticoes(root, InitSubScene.camVolume, 0, root.getChildren.size())
    }
  }

  def shapeToLine(shape: Node): String = {
    val name = getName(shape)
    val color = getColor(shape.asInstanceOf[Shape3D].getMaterial.asInstanceOf[PhongMaterial].getDiffuseColor)
    name+" " + color + " " + shape.getTranslateX + " " + shape.getTranslateY + " " + shape.getTranslateZ + " " + shape.getScaleX + " " + shape.getScaleY + " " + shape.getScaleZ
  }


  def getName(shape: Node): String = {
    if(shape.isInstanceOf[Cylinder])
      "Cylinder"
    else
      "Box"
  }

  def getColor(color: Color): String = {
    val R = (color.getRed*255).toInt
    val G = (color.getGreen*255).toInt
    val B = (color.getBlue*255).toInt
    "("+R+","+G+","+B+")"
  }

  def restarCamera(): Unit ={
    InitSubScene.camVolume.setTranslateX(1)
  }

}
