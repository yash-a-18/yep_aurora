package tutorial.webapp
// import scala.scalajs.js.annotation.JSExportTopLevel
// import com.raquo.laminar.api.L.{*, given}
import com.raquo.laminar.api.L._
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.document
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.JSON
import org.scalajs.dom.raw.HTMLParagraphElement

object TutorialApp {
    val dataUrl = "http://localhost:5000/challenges"
    val counter: Int = 0
    @main def scalaJsHere(): Unit = {
        document.addEventListener("DOMContentLoaded", { (e: dom.Event) =>
            setupUI()
        })
    }

    def createButton[A](buttonName: String, func: () => A) = {
      val div = document.createElement("div")
      div.asInstanceOf[dom.html.Div].style.margin = "12px"
      val button = document.createElement("button")
        button.textContent = buttonName
        button.addEventListener("click", { (e: dom.MouseEvent) =>
            func()
        })
        div.appendChild(button)
        document.body.appendChild(div)
    }

    def getLastParagraph(): Option[HTMLParagraphElement] = {
      // Get all <p> tags
      val paragraphs = dom.document.getElementsByTagName("p")
      val paragraphsList: List[HTMLParagraphElement] = (0 until paragraphs.length).map { i =>
        paragraphs(i).asInstanceOf[HTMLParagraphElement]
      }.toList

      //Get the last <p> element using lastOption
      val lastParagraph: Option[HTMLParagraphElement] = paragraphsList.lastOption
      lastParagraph
    }

    def getLastTextBox(): Option[dom.html.Input] = {
      // Get all <input> tags
      val inputs = dom.document.getElementsByTagName("input")
      val inputsList: List[dom.html.Input] = (0 until inputs.length).map { i =>
        inputs(i).asInstanceOf[dom.html.Input]
      }.toList

      //Get the last <p> element using lastOption
      val lastInput: Option[dom.html.Input] = inputsList.lastOption
      lastInput
    }

    def checkResult(): Unit = {
      val lastInput: Option[dom.html.Input] = getLastTextBox()
      // Safely handle the Option and print the value if it exists
      lastInput match {
        case Some(input) =>
          // Print the value of the last input box
          println(input.value)
        case None =>
          // Handle the case where no input box was found
          println("No input box found.")
      }
    }

    def createTextBox(): Unit = {
      val resVar = Var("")
      val resValueSignal = resVar.signal
      val resInputObserver = resVar.writer
      val resCodeInput =  input(
          placeholder := "Enter only integer Numbers: ",
          controlled(
            value <-- resValueSignal,
            onInput.mapToValue.map(_.filter(Character.isDigit)) --> resInputObserver
        )
      )
      val lastParagraph = getLastParagraph()

      //Append the input element to the last <p> tag if it exists
       lastParagraph match {
        case Some(lastP) =>
          val inLineButton = lastP.appendChild(resCodeInput.ref) // Use `resCodeInput.ref` to get the actual DOM element
          createButton("Check Multiplication", checkResult)
        case None =>
          // Handle the case where no <p> element exists, if needed
          dom.console.log("No <p> elements found.")
      }
      
    }

    // @JSExportTopLevel("getMultiplication")
    def getMultiplication(): Unit = {
    val futureResponse: Future[dom.XMLHttpRequest] = Ajax.get(dataUrl + "/mul")

      // Handle the Future response
      futureResponse.onComplete {
        case scala.util.Success(xhr) =>
          // Parse the JSON response
          val json = JSON.parse(xhr.responseText) //type of "val json" is scala.scalajs.js.Dynamic
          println(s"Hola World ${JSON.stringify(json)}")
          appendPar(document.body, JSON.stringify(json, space = 2))
        case scala.util.Failure(exception) =>
          println(s"Request failed with exception: $exception")
      }
    }

    def getRandomNumbers(): Unit = {
    val futureResponse: Future[dom.XMLHttpRequest] = Ajax.get(dataUrl + "/random")

      // Handle the Future response
      futureResponse.onComplete {
        case scala.util.Success(xhr) =>
          // Parse the JSON response
          val json: js.Dynamic = JSON.parse(xhr.responseText)//type of "val json" is scala.scalajs.js.Dynamic
          println(s"Hola World ${JSON.stringify(json)}")
           // Get the keys of the JSON object
           val obj = json.asInstanceOf[js.Object] 
           // Extract values for specific keys
          val valueA = json.selectDynamic("valueA").toString()
          val valueB = json.selectDynamic("valueB").toString()

          // Build the result string
          val toPrint = s"$valueA x $valueB = "
          appendPar(document.body, toPrint)
          createTextBox()
        case scala.util.Failure(exception) =>
          println(s"Request failed with exception: $exception")
      }
    }

    def appendPar(targetNode: dom.Node, text: String): Unit = {
        val parNode = document.createElement("p")
        parNode.textContent = text
        targetNode.appendChild(parNode)
    }

    def setupUI(): Unit = {
      appendPar(document.body, "Hello Fuucckkkerrer")

        createButton("Get Multiplication!", getMultiplication)
      
        createButton("Get Random Numbers!", getRandomNumbers)

        
    }
}
