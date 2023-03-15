import javafx.application.Application
import javafx.application.Application.launch
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

object GraphicInterface {

  def main(args: Array[String]): Unit = {
    launch(classOf[GUI])
  }

}

class GUI extends Application{
  override def start(primaryStage: Stage): Unit ={
    primaryStage.setTitle("PPM 21/22")
    val fxmlLoader =
      new FXMLLoader(getClass.getResource("GUI.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    primaryStage.setScene(scene)
    primaryStage.show()
  }
}
