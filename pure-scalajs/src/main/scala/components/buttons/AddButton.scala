package components.buttons

import com.raquo.laminar.api.L.{*, given}
import components.MainComponents
import com.raquo.laminar.api.features.unitArrows //For Unit return type

case class AddButton(value: String) extends MainComponents {

  def render(): Element = {
    button(
      value,
      onClick --> {
        println("Oooooo... That hurts")
      }
    )
  }
}
