import Tree.Placement
import javafx.scene.paint.{Color, PhongMaterial}
import javafx.scene.Node
import javafx.scene.shape.{Box, Shape3D}
import scala.annotation.tailrec


case class oct(root: Octree[Placement]){
  def insert(shape: Shape3D): oct = oct(Tree.insertInTree(this.root, shape))
  def scale(scale: Double): oct = Tree.scale(scale, this.root)
  def mapColourEffect(func: Color => Color): oct = Tree.mapColourEffect(func, this.root)
  def getCubos: List[Box] = Tree.getCubos(this.root)
  def getShapes: List[Node] = Tree.getShapes(this.root)
  def getSize: Double = Tree.getSize(this.root)
}

object Tree {

  type Point = (Double, Double, Double)
  type Size = Double
  type Placement = (Point, Size)
  type Section = (Placement, List[Node])

  def getSize(root: Octree[Placement]): Double = {
    if(root == OcEmpty)
      0.0
    else
      root match{
        case o: OcLeaf[Placement, Section] =>
          o.section._1._2
        case n: OcNode[Placement] =>
          n.coords._2
      }
  }

  def createCube(x: Octree[Placement]): Box = {
    x match {
      case y: OcNode[Placement] =>
        val aux = y.coords._2
        val result = new Box(aux,aux,aux)
        result.setTranslateX(aux/2 + y.coords._1._1)
        result.setTranslateY(aux/2 + y.coords._1._2)
        result.setTranslateZ(aux/2 + y.coords._1._3)
        result
      case y: OcLeaf[Placement, Section] =>
        val aux = y.section._1._2
        val result = new Box(aux,aux,aux)
        result.setTranslateX(aux/2 + y.section._1._1._1)
        result.setTranslateY(aux/2 + y.section._1._1._2)
        result.setTranslateZ(aux/2 + y.section._1._1._3)
        result
    }
  }

  def create(coords: Placement, lst: List[Shape3D]): oct = {
    if(lst.isEmpty || coords._2 <= 0.0)
      oct(OcEmpty)
    else
      add(oct(OcNode(coords, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty)), lst)
  }

  @tailrec
  def add(tree: oct, lst: List[Shape3D]): oct = {
    lst match{
      case Nil =>
        if(getShapes(tree.root).isEmpty)
          oct(OcEmpty)
        else
          tree
      case x :: tail =>
        if(createCube(tree.root).getBoundsInParent.contains(x.getBoundsInParent))
          add(tree.insert(x), tail)
        else
          add(tree, tail)
    }
  }

  def insertInTree(node: Octree[Placement], shape: Shape3D): Octree[Placement] = {
    node match{
      case l : OcLeaf[Placement,Section] => OcLeaf((l.section._1, l.section._2 :+ shape.asInstanceOf[Node]))
      case n : OcNode[Placement] =>
        val aux = getFilhos(n,shape,"up_00")
        if(aux._1 != OcEmpty)
          createOcNode(n, shape, aux)
        else
          createOcLeaf(n, shape)
      }
  }

  @tailrec
  def getFilhos(node: OcNode[Placement], shape : Shape3D, s: String): (Octree[Placement], String) = {
    def aux(filho: Octree[Placement], pai: OcNode[Placement], s: String): Octree[Placement] = {
      filho match {
        case OcEmpty =>
          s match{
            case "up_00" =>
              OcNode ((pai.coords._1, pai.coords._2/2), OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty)
            case "up_01" =>
              val ponto = (pai.coords._1._1, pai.coords._2/2, pai.coords._1._3)
              OcNode ((ponto, pai.coords._2/2), OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty)
            case "up_10" =>
              val ponto = (pai.coords._2/2, pai.coords._1._2, pai.coords._1._3)
              OcNode ((ponto, pai.coords._2/2), OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty)
            case "up_11" =>
              val ponto = (pai.coords._2/2, pai.coords._2/2, pai.coords._1._3)
              OcNode ((ponto, pai.coords._2/2), OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty)
            case "down_00" =>
              val ponto = (pai.coords._1._1, pai.coords._1._2, pai.coords._2/2)
              OcNode ((ponto, pai.coords._2/2), OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty)
            case "down_01" =>
              val ponto = (pai.coords._1._1, pai.coords._2/2, pai.coords._2/2)
              OcNode ((ponto, pai.coords._2/2), OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty)
            case "down_10" =>
              val ponto = (pai.coords._2/2, pai.coords._1._2, pai.coords._2/2)
              OcNode ((ponto, pai.coords._2/2), OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty)
            case "down_11" =>
              val ponto = (pai.coords._2/2, pai.coords._2/2, pai.coords._2/2)
              OcNode ((ponto, pai.coords._2/2), OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty)
          }
        case _ => filho
      }
    }
    s match{
      case "up_00" =>
        val filho = aux(node.up_00, node, s)
        val cube = createCube(filho)
        if(cube.getBoundsInParent.contains(shape.getBoundsInParent))
          (filho, "up_00")
        else
          getFilhos(node,shape,"up_01")
      case "up_01" =>
        val filho = aux(node.up_01, node, s)
        val cube = createCube(filho)
        if(cube.getBoundsInParent.contains(shape.getBoundsInParent))
          (filho, "up_01")
        else
          getFilhos(node,shape,"up_10")
      case "up_10" =>
        val filho = aux(node.up_10, node, s)
        val cube = createCube(filho)
        if(cube.getBoundsInParent.contains(shape.getBoundsInParent))
          (filho, "up_10")
        else
          getFilhos(node,shape,"up_11")
      case "up_11" =>
        val filho = aux(node.up_11, node, s)
        val cube = createCube(filho)
        if(cube.getBoundsInParent.contains(shape.getBoundsInParent))
          (filho, "up_11")
        else
          getFilhos(node,shape,"down_00")
      case "down_00" =>
        val filho = aux(node.down_00, node, s)
        val cube = createCube(filho)
        if(cube.getBoundsInParent.contains(shape.getBoundsInParent))
          (filho, "down_00")
        else
          getFilhos(node,shape,"down_01")
      case "down_01" =>
        val filho = aux(node.down_01, node, s)
        val cube = createCube(filho)
        if(cube.getBoundsInParent.contains(shape.getBoundsInParent))
          (filho, "down_01")
        else
          getFilhos(node,shape,"down_10")
      case "down_10" =>
        val filho = aux(node.down_10, node, s)
        val cube = createCube(filho)
        if(cube.getBoundsInParent.contains(shape.getBoundsInParent))
          (filho, "down_10")
        else
          getFilhos(node,shape,"down_11")
      case "down_11" =>
        val filho = aux(node.down_11, node, s)
        val cube = createCube(filho)
        if(cube.getBoundsInParent.contains(shape.getBoundsInParent))
          (filho, "down_11")
        else
          (OcEmpty,"")
    }
  }

  def createOcLeaf(node: OcNode[Placement], shape: Shape3D): OcLeaf[Placement,Section] = {
    val lst = getShapesInChildren(node)
    OcLeaf(node.coords, lst :+ shape.asInstanceOf[Node])
  }

  def getShapesInChildren(node: OcNode[Placement]): List[Node] = {
    @tailrec
    def aux(result: List[Node], lst: List[OcLeaf[Placement,Section]]): List[Node] = {
      lst match{
        case Nil => result
        case x :: tail => aux(result ++ x.section._2,tail)
      }
    }
    val filhos = getParticoes(node, "up_00").filter(x => x.isInstanceOf[OcLeaf[Placement,Section]]).map(x => x.asInstanceOf[OcLeaf[Placement,Section]])
    aux(Nil,filhos)
  }

  def getParticoes(node: OcNode[Placement], s: String): List[Octree[Placement]] = {
    s match{
      case "up_00" =>
        if(node.up_00 == OcEmpty)
          getParticoes(node,"up_01")
        else node.up_00 match {
          case o: OcNode[Placement] => o :: getParticoes(o, "up_00") ++ getParticoes(node, "up_01")
          case _ => node.up_00.asInstanceOf[OcLeaf[Placement, Section]] :: getParticoes(node, "up_01")
        }
      case "up_01" =>
        if(node.up_01 == OcEmpty)
          getParticoes(node,"up_10")
        else node.up_01 match {
          case value: OcNode[Placement] => value :: getParticoes(value, "up_00") ++ getParticoes(node, "up_10")
          case _ => node.up_01.asInstanceOf[OcLeaf[Placement, Section]] :: getParticoes(node, "up_10")
        }
      case "up_10" =>
        if(node.up_10 == OcEmpty)
          getParticoes(node,"up_11")
        else node.up_10 match {
          case value: OcNode[Placement] => value :: getParticoes(value, "up_00") ++ getParticoes(node, "up_11")
          case _ => node.up_10.asInstanceOf[OcLeaf[Placement, Section]] :: getParticoes(node, "up_11")
        }
      case "up_11" =>
        if(node.up_11 == OcEmpty)
          getParticoes(node,"down_00")
        else node.up_11 match {
          case value: OcNode[Placement] => value :: getParticoes(value, "up_00") ++ getParticoes(node, "down_00")
          case _ => node.up_11.asInstanceOf[OcLeaf[Placement, Section]] :: getParticoes(node, "down_00")
        }
      case "down_00" =>
        if(node.down_00 == OcEmpty)
          getParticoes(node,"down_01")
        else node.down_00 match {
          case value: OcNode[Placement] => value :: getParticoes(value, "up_00") ++ getParticoes(node, "down_01")
          case _ => node.down_00.asInstanceOf[OcLeaf[Placement, Section]] :: getParticoes(node, "down_01")
        }
      case "down_01" =>
        if(node.down_01 == OcEmpty)
          getParticoes(node,"down_10")
        else node.down_01 match {
          case value: OcNode[Placement] => value :: getParticoes(value, "up_00") ++ getParticoes(node, "down_10")
          case _ => node.down_01.asInstanceOf[OcLeaf[Placement, Section]] :: getParticoes(node, "down_10")
        }
      case "down_10" =>
        if(node.down_10 == OcEmpty)
          getParticoes(node,"down_11")
        else node.down_10 match {
          case value: OcNode[Placement] => value :: getParticoes(value, "up_00") ++ getParticoes(node, "down_11")
          case _ => node.down_10.asInstanceOf[OcLeaf[Placement, Section]] :: getParticoes(node, "down_11")
        }
      case "down_11" =>
        if(node.down_11 == OcEmpty)
          Nil
        else node.down_11 match {
          case value: OcNode[Placement] => value :: getParticoes(value, "up_00")
          case _ => List(node.down_11.asInstanceOf[OcLeaf[Placement, Section]])
        }
    }
  }

  def createOcNode(node: OcNode[Placement], shape: Shape3D, tuplo: (Octree[Placement], String)): Octree[Placement] = {
    tuplo._2 match{
      case "up_00" => OcNode(node.coords, insertInTree(tuplo._1, shape), node.up_01, node.up_10, node.up_11, node.down_00, node.down_01, node.down_10, node.down_11)
      case "up_01" => OcNode(node.coords, node.up_00, insertInTree(tuplo._1, shape), node.up_10, node.up_11, node.down_00, node.down_01, node.down_10, node.down_11)
      case "up_10" => OcNode(node.coords, node.up_00, node.up_01, insertInTree(tuplo._1, shape), node.up_11, node.down_00, node.down_01, node.down_10, node.down_11)
      case "up_11" => OcNode(node.coords, node.up_00, node.up_01, node.up_11, insertInTree(tuplo._1, shape), node.down_00, node.down_01, node.down_10, node.down_11)
      case "down_00" => OcNode(node.coords, node.up_00, node.up_01, node.up_10, node.up_11, insertInTree(tuplo._1, shape), node.down_01, node.down_10, node.down_11)
      case "down_01" => OcNode(node.coords, node.up_00, node.up_01, node.up_10, node.up_11, node.down_00, insertInTree(tuplo._1, shape), node.down_10, node.down_11)
      case "down_10" => OcNode(node.coords, node.up_00, node.up_01, node.up_10, node.up_11, node.down_00, node.down_01, insertInTree(tuplo._1, shape), node.down_11)
      case "down_11" => OcNode(node.coords, node.up_00, node.up_01, node.up_10, node.up_11, node.down_00, node.down_01, node.down_10, insertInTree(tuplo._1, shape))
    }
  }

  def getCubos(root: Octree[Placement]): List[Box] = {
    if(root == OcEmpty)
      Nil
    else
      root match{
      case o : OcLeaf[Placement, Section] =>
        List(createCube(o))
      case n : OcNode[Placement] =>
       (root :: getParticoes(n, "up_00")).map(x => createCube(x))
    }
  }

  def getShapes(root: Octree[Placement]): List[Node] = {
    if(root == OcEmpty)
      Nil
    else
      root match{
      case o: OcLeaf[Placement, Section] =>
        o.section._2
      case n: OcNode[Placement] =>
        getShapesInChildren(n)
    }
  }

  def scale(scale: Double, root: Octree[Placement]):oct = {
    if(root == OcEmpty)
      oct(OcEmpty)
    else
      root match{
        case o: OcLeaf[Placement,Section] =>
          val newPlacement = (o.section._1._1, o.section._1._2 * scale)
          val newShapes = scaleShapes(o.section._2.map(x => x.asInstanceOf[Shape3D]), scale)
          add(oct(OcNode(newPlacement, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty)),newShapes)
        case n: OcNode[Placement] =>
          val newPlacement = (n.coords._1, n.coords._2 * scale)
          val newShapes = scaleShapes(getShapes(n).map(x => x.asInstanceOf[Shape3D]), scale)
          add(oct(OcNode(newPlacement, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty)),newShapes)
      }
  }

  def scaleShapes(lst: List[Shape3D], scale: Double): List[Shape3D] = {
    def aux(x: Shape3D, scale: Double): Shape3D = {
      x.setTranslateX(x.getTranslateX * scale)
      x.setTranslateY(x.getTranslateY * scale)
      x.setTranslateZ(x.getTranslateZ * scale)
      x.setScaleX(x.getScaleX * scale)
      x.setScaleY(x.getScaleY * scale)
      x.setScaleZ(x.getScaleZ * scale)
      x
    }
    lst match {
      case Nil => Nil
      case x :: tail => aux(x,scale) :: scaleShapes(tail,scale)
    }
  }

  def mapColourEffect(func: Color => Color, root: Octree[Placement]):oct = {
    if(root == OcEmpty)
      oct(OcEmpty)
    else
      root match {
      case o: OcLeaf[Placement, Section] =>
        val newShapes = changeColours(o.section._2.map(x => x.asInstanceOf[Shape3D]),func)
        add(oct(OcNode(o.section._1, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty)),newShapes)
      case n: OcNode[Placement] =>
        val newShapes = changeColours(getShapes(n).map(x => x.asInstanceOf[Shape3D]),func)
        add(oct(OcNode(n.coords, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty)),newShapes)
    }
  }

  def changeColours(lst: List[Shape3D], func: Color => Color): List[Shape3D] = {
    def aux(x: Shape3D, func: Color => Color): Shape3D = {
      val oldColor = x.getMaterial.asInstanceOf[PhongMaterial].getDiffuseColor
      val newColor = func(oldColor)
      val newMaterial = new PhongMaterial()
      newMaterial.setDiffuseColor(newColor)
      x.setMaterial(newMaterial)
      x
    }
    lst match{
      case Nil => Nil
      case x :: tail => aux(x,func) :: changeColours(tail, func)
    }
  }








}

