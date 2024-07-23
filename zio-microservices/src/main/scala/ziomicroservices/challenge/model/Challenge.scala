package ziomicroservices.challenge.model

import zio.json._

case class Challenge(valueA: Int, valueB: Int, result: Int = 0, isCorrect: Option[Boolean] = None)

object Challenge{
    given JsonEncoder[Challenge] = DeriveJsonEncoder.gen[Challenge]
    given JsonDecoder[Challenge] = DeriveJsonDecoder.gen[Challenge]
}