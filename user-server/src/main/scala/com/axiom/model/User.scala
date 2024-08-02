package com.axiom.model

import zio._
import zio.json._

case class User(id: Int, firstName: String, lastName: String)

object User {
    given JsonEncoder[User] = DeriveJsonEncoder.gen[User]
    given JsonDecoder[User] = DeriveJsonDecoder.gen[User]
}