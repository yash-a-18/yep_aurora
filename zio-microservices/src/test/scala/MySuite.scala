// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
import zio._
import ziomicroservices.challenge.model.Challenge
import ziomicroservices.challenge.service.ChallengeService

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class MySuite extends munit.FunSuite {
  test("example test that succeeds") {
    val obtained = 42
    val expected = 42
    assertEquals(obtained, expected)
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