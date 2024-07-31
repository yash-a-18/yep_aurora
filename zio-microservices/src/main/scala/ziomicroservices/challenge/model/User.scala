package ziomicroservices.challenge.model

import zio._
import zio.json._


case class User(id: Int, firstName: String, lastName: String)

object User {
  // val ctx = new MysqlZioJdbcContext(SnakeCase) // Use MySQL context
  // import ctx._

  // val user = User(1, "John", "Doe")
  

  given JsonEncoder[User] = DeriveJsonEncoder.gen[User]
  given JsonDecoder[User] = DeriveJsonDecoder.gen[User]
}
