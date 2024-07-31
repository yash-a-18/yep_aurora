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

class TestUser extends AnyWordSpec with Matchers{
  
  
  "UserService should add a User and yield ID" should {
    "For User add at" in{
  val testUser = User(1, "John", "Doe")
  UserService.dynamicInsert(testUser).flatMap { op =>
    // Extract the `User` object from the ZIO effect
    op should be (1)
    println(op)
    ZIO.logInfo(s"Added User at $op")
  }
  }
}
}