package tutorial.webapp
// import scala.scalajs.js.annotation.JSExportTopLevel
import org.scalajs.dom
import org.scalajs.dom.document

object TutorialApp {
    val counter: Int = 0
    @main def scalaJsHere(): Unit = {
        document.addEventListener("DOMContentLoaded", { (e: dom.Event) =>
            setupUI()
        })
    }

    // @JSExportTopLevel("addClickedMessage")
    def addClickedMessage(): Unit = {
    appendPar(document.body, "Why did you clicked?")
    }

    def appendPar(targetNode: dom.Node, text: String): Unit = {
        val parNode = document.createElement("p")
        parNode.textContent = text
        targetNode.appendChild(parNode)
    }

    def setupUI(): Unit = {
        val button = document.createElement("button")
        button.textContent = "Click me!"
        button.addEventListener("click", { (e: dom.MouseEvent) =>
            addClickedMessage()
        })
        document.body.appendChild(button)

        appendPar(document.body, "Hello Fuucckkkerrer")
    }
}
