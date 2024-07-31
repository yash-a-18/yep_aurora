package components.table

import com.raquo.laminar.api.L.{*, given}
import components.MainComponents

case class printTabularData(twoDList: List[List[Any]])
    extends MainComponents {
  def render(): Element = {
    table(
      thead(
        tr( 
          width := "100%",
          twoDList.head.map { cell =>
            th(
              cell.toString,
              )
          }
        )
      ),
      tbody(
        twoDList.tail.map { row =>
          tr(
            width := "100%",
            row.map { cell =>
              td(cell.toString)
            }
          )
        },
        
      )
    )
  }

}