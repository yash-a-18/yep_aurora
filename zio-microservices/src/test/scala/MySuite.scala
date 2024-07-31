// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
import zio._
import ziomicroservices.challenge.model.Challenge
import ziomicroservices.challenge.service.ChallengeService
import ziomicroservices.challenge.controller.ChallengeController

import zio.http._
import zio.http.Server
import zio.test._
import zio.Console._

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class MySuite extends munit.FunSuite {
  test("example test that succeeds") {
    val obtained = 42
    val expected = 42
    assertEquals(obtained, expected)
  }
  test("Controller should return True when validating challenge attempt") {
  val app = ChallengeController()
  val req = Request.post(Body.fromString("""{"valueA":2,"valueB":2,"result":4}"""), 
      URL(Root  / "challenges" / "check"))
      // app.runZIO(req).map(x => printLine(x.body.asString))
      app.runZIO(req).flatMap { response =>
        response.body.asString.flatMap { bodyString =>
          printLine(s"Response Body: $bodyString") *>
          ZIO.succeed(assertTrue(bodyString == "true"))
        }
      }
  // assertZIO(app.runZIO(req).map(x => x.body))(equals(Response.json("true").body))
  }
  test("ChallengeService should check a checkChallenge and yield true") {
  val challenge = Challenge(2, 3, 6, None)
  // val op = ChallengeService.checkChallenge(challenge)
  ChallengeService.checkChallenge(challenge).flatMap { op =>
    // Extract the `Challenge` object from the ZIO effect
    ZIO.succeed(op.isCorrect) // `op` is the Challenge object
      .map { isCorrectValue =>
        // Perform the assertion
        // info(s"Hiiiiiii ${challenge.isCorrect}, ${isCorrectValue}")
        println()
        assertEquals(challenge.isCorrect, isCorrectValue)
        }
    }
  }
}