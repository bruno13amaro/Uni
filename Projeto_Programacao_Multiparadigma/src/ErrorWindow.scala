import javafx.fxml.FXML
import javafx.scene.control.Button

class ErrorWindow {

  @FXML
  private var ok:Button = _

  def goBack(): Unit = {
    ok.getScene.getWindow.hide()
  }

}
