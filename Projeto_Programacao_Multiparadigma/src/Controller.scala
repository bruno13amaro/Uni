import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.{Button, SplitPane}
import javafx.scene.{Parent, Scene, SubScene}
import javafx.scene.layout.{AnchorPane, GridPane, StackPane}
import javafx.stage.{FileChooser, Modality, Stage}

class ViewController {

  @FXML
  private var gridPane:GridPane = _

  @FXML
  private var subScene1:SubScene = _

  @FXML
  private var scaleUp:Button = _

  @FXML
  private var scaleDown:Button = _

  @FXML
  private var sepiaFilter:Button = _

  @FXML
  private var greenRemove:Button = _

  @FXML
  private var load:Button = _

  @FXML
  private var save:Button = _

  @FXML
  private var splitPane:SplitPane = _


  //method automatically invoked after the @FXML fields have been injected
  @FXML
  def initialize(): Unit = {
    splitPane.setPrefHeight(650.0)
    splitPane.setPrefWidth(900.0)
    InitSubScene.subScene.widthProperty.bind(subScene1.widthProperty)
    InitSubScene.subScene.heightProperty.bind(subScene1.heightProperty)
    subScene1.setRoot(InitSubScene.root)
    subScene1.widthProperty().bind(gridPane.widthProperty())
    subScene1.heightProperty().bind(gridPane.heightProperty())
    disableButtons()
  }

  def disableButtons(): Unit = {
    save.setDisable(true)
    greenRemove.setDisable(true)
    sepiaFilter.setDisable(true)
    scaleUp.setDisable(true)
    scaleDown.setDisable(true)
  }

  def enableButtons(): Unit = {
    save.setDisable(false)
    greenRemove.setDisable(false)
    sepiaFilter.setDisable(false)
    scaleUp.setDisable(false)
    scaleDown.setDisable(false)
  }

  def loadButton(): Unit ={
    val filechooser = new FileChooser()
    val selectedFile = filechooser.showOpenDialog(load.getScene.getWindow)
    if(selectedFile == null)
      return
    try{
      val dataFile = IO_Utils.readFile(selectedFile.getAbsolutePath)
      OctreeCriacao.tree = Tree.create(((0.0,0.0,0.0),dataFile._1), dataFile._2)
      Functions.restarCamera()
      Functions.update("scale",InitSubScene.worldRoot)
      if(save.isDisabled)
        enableButtons()
      else if(OctreeCriacao.tree.root == OcEmpty && ! save.isDisabled)
        disableButtons()
    }
    catch{
      case _ =>
        val root = new FXMLLoader(getClass.getResource("ErrorWindow.fxml"))
        val aux: Parent = root.load()
        val secondStage: Stage = new Stage()
        secondStage.setScene(new Scene(aux))
        secondStage.setTitle("Erro")
        secondStage.initModality(Modality.WINDOW_MODAL)
        secondStage.initOwner(load.getScene.getWindow)
        secondStage.show()
    }

  }

  def saveButton(): Unit = {
    val filechooser = new FileChooser()
    val selectedFile = filechooser.showOpenDialog(save.getScene.getWindow)
    IO_Utils.shapesToFile(selectedFile.getAbsolutePath,OctreeCriacao.tree.getShapes,OctreeCriacao.tree.getSize)
  }

  def applyGreenRemove(): Unit = {
    OctreeCriacao.tree = OctreeCriacao.tree.mapColourEffect(Functions.greenRemove)
    Functions.update("filter", InitSubScene.worldRoot)
  }

  def applySepiaFilter(): Unit = {
    OctreeCriacao.tree = OctreeCriacao.tree.mapColourEffect(Functions.sepiaFilter)
    Functions.update("filter", InitSubScene.worldRoot)
  }

  def applyScaleDown():Unit = {
    OctreeCriacao.tree = OctreeCriacao.tree.scale(0.5)
    Functions.update("scale", InitSubScene.worldRoot)
  }

  def applyScaleUp():Unit = {
    OctreeCriacao.tree = OctreeCriacao.tree.scale(2.0)
    Functions.update("scale", InitSubScene.worldRoot)
  }
}
