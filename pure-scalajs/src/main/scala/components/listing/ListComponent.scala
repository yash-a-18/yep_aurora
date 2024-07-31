package components.listing

import com.raquo.laminar.api.L.{*, given}
import components.MainComponents

case class ListComponent(list: List[String], sort: Boolean)
    extends MainComponents {
  def render(): Element = {
    sort match {
      case true =>
        ul(
          list.sorted.map { item =>
            li(item)
          }
        )
      case false =>
        ul(
          list.map { item =>
            li(item)
          }
        )
    }
  }

}
