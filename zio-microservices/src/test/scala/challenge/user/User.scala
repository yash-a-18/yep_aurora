import zio.test._
import zio.test.Assertion._
import io.getquill._
import ziomicroservices.challenge.model.User

// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
import zio._
import ziomicroservices.challenge.model.User
import ziomicroservices.challenge.service.UserService
import ziomicroservices.challenge.controller.UserController

import zio.test._
import zio.Console._

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TestUser extends AnyWordSpec with Matchers {

  "UserService should add a User and yield ID" should {
    "For User add at" in {
      val testUser = User(1, "John", "Doe")
      println(testUser.firstName)
      val userTask = UserService.dynamicInsert(testUser)
      println(userTask.flatMap.toString)
      userTask.flatMap(result => ZIO.succeed(println(s"Result: $result")))
    }
  }
}
