package components.todolist

import components.MainComponents
import com.raquo.laminar.api.L._
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import scala.concurrent.Future
import scala.scalajs.js.JSON
import scala.concurrent.ExecutionContext.Implicits.global


class ToDoList extends MainComponents {
  val todos: Var[List[String]] = Var(List.empty)

  def addToDo(newtodo: String): Unit = {
    todos.update(todos => todos :+ newtodo)
    println(todos.now().map(println))

    val dataUrl = "http://localhost:5000/todo/add"
    val futureResponse: Future[dom.XMLHttpRequest] = Ajax.post(dataUrl,
    newtodo,
    headers = Map("Content-Type" -> "application/json"))

    // Handle the Future response
    futureResponse.onComplete {
      case scala.util.Success(xhr) =>
        // Parse the JSON response
        val json = JSON.parse(
          xhr.responseText
        ) // type of "val json" is scala.scalajs.js.Dynamic
        println(s"Hola TO Do ${JSON.stringify(json)}")
      case scala.util.Failure(exception) =>
        println(s"Request failed with exception: $exception")
    }
  }

//   def deleteToDo(index: Int): Unit = {
//     todos.update(todos => todos.patch(index, Nil, 1))
//   }

  // Render method to create the to-do list element
  def render(): Element = {
    // Reactive variable to store the current input value
    val inputVar = Var("")
    div(
      h2("To-Do List"),
      form(
        onSubmit.preventDefault --> { _ =>
          val newTodo = inputVar.now()
          if (newTodo.nonEmpty) {
            addToDo(newTodo)
            inputVar.set("")

          }
        },
        div(
          input(
            typ := "text",
            value <-- inputVar.signal,
            onInput.mapToValue --> inputVar.writer,
            placeholder := "Add a new to-do"
          ),
          button(
            "Add",
            typ := "submit",
            margin := "10px"
          )
        )
      ),
      ol(
        children <-- todos.signal.split(identity) {
          (todo, initialTodo, todoSignal) =>
            li(
              span(child.text <-- todoSignal),
              button(
                "Remove",
                onClick.map(_ => todo) --> { _ =>
                  todos.update(todos => todos.filterNot(_ == todo))
                  println("Removed from To-Do because you can't")
                },
                margin := "10px"
              )
            )
        }
      )
    )
  }
}
