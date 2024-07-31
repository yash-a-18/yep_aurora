package ziomicroservices.challenge.model

import zio.json._

case class ToDo(todo: String)

object ToDo{
    given JsonEncoder[ToDo] = DeriveJsonEncoder.gen[ToDo]
    given JsonDecoder[ToDo] = DeriveJsonDecoder.gen[ToDo]
}