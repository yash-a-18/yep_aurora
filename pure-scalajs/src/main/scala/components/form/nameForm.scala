package components.form

import com.raquo.laminar.api.L._
import org.scalajs.dom
import components.MainComponents

class nameForm extends MainComponents {

  def render(): Element = {
    // Reactive variable to store the name input value
    val nameVar = Var("")

    // Signal to track if the input is non-empty
    val isValidSignal = nameVar.signal.map(_.nonEmpty)

    div(
      form(
        onSubmit.preventDefault --> { _ =>
          val name = nameVar.now()
          if (name.nonEmpty) {
            dom.window.alert(s"Submitted name for form: $name")
          }
        },
        div(
          label("Name:"),
          input(
            typ := "text",
            inContext { thisNode =>
              onInput.mapToValue --> nameVar.writer
            }
          )
        ),
        div(
          button(
            "Submit",
            typ := "submit",
            disabled <-- isValidSignal.map(!_),
            // onClick.preventDefault --> { _ =>
            //   val name = nameVar.now()
            //   if (name.nonEmpty) {
            //     dom.window.alert(s"Submitted name: $name")
            //   }
            // }
          )
        )
      )
    )
  }
}
