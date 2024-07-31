import org.scalajs.dom
import org.scalajs.dom.document
import components.buttons.AddButton
import com.raquo.laminar.api.L.{*, given}
import components.listing.ListComponent
import components.table.printTabularData
import components.form.nameForm
import components.todolist.ToDoList

@main def scalaJsHere(): Unit = {
  document.addEventListener(
    "DOMContentLoaded",
    { (e: dom.Event) =>
      setupUI()
      // Create an instance of AddButton
      val addButton = new AddButton("Push ur Anger over Me")

      val unorderedList = new ListComponent(
        List("Dr Kim", "James", "Yash", "Yash2", "Abel"),
        true
      )

      val twoDList: List[List[Any]] = List(
        List("ID", "Name", "Address"),
        List(1, "Spider Man", "Far from home"),
        List(2,"Batman", "Bigger Thunder Bay"),
        List(3, "Iron Man", "Heaven")
      )

      val tabularData = new printTabularData(twoDList)

      val container = div(
        addButton.render(), // Use the render method to get the button element
        p(h2("Aurora")),
        unorderedList.render(),
        p(h2("Super Hero Data")),
        tabularData.render(),
        p(h2("Fill Your Super Hero Form")),
        nameForm().render(),
        ToDoList().render()
      )
      // Append the container to the document body
      render(document.body, container)
    }
  )
}

def appendPar(targetNode: dom.Node, text: String): Unit = {
  val parNode = document.createElement("h1")
  parNode.textContent = text
  targetNode.appendChild(parNode)
}

def setupUI(): Unit = {
  appendPar(document.body, "High functioning Socio-path")
}
