// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html

import zio.test._
import zio.json
import zio.json._
import com.axiom.model.User

class MySuite extends munit.FunSuite {
  test("example test that succeeds") {
    val obtained = 42
    val expected = 42
    assertEquals(obtained, expected)
  }
  test("First way: Decoding the User"){
    val jsonUser = "{\"id\":\"1\",\"firstName\":\"Jason\",\"lastName\":\"Bourne\"}"
    assertEquals(jsonUser.fromJson[User], Right(User(1,"Jason","Bourne")))
  }
  test("Second way: Decoding the User"){
    val jsonUser = "{\"id\":\"1\",\"firstName\":\"Jason\",\"lastName\":\"Bourne\"}"
    val decoded = JsonDecoder[User].decodeJson(jsonUser)
    assertEquals(jsonUser.fromJson[User], decoded)
  }
  test("Trying to Decode the partial User"){
    val jsonUser = "{\"id\":\"1\",\"firstName\":\"Jason\",\"lastName\":\"Bourne\"}"
    val decoded = JsonDecoder[User].decodeJson("{\"firstName\":\"Jason\",\"lastName\":\"Bourne\"}")
    assertEquals(jsonUser.fromJson[User], decoded)
  }
}