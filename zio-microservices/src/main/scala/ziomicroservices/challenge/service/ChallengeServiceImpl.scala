package ziomicroservices.challenge.service

import zio._
import ziomicroservices.challenge.model.Challenge

case class ChallengeServiceImpl(randomGeneratorService: RandomGeneratorService) extends ChallengeService{

  def createRandomMultiplication(): ZIO[Any, Nothing, Challenge] = {
    randomGeneratorService.generateRandomFactor().flatMap { id1 =>
      randomGeneratorService.generateRandomFactor().map { id2 =>
        Challenge(id1, id2, id1 * id2)
      }
    }
  }

  def createRandomNumbers(): ZIO[Any, Nothing, Challenge] = {
    // for {
    //   id1 <- randomGeneratorService.generateRandomFactor()
    //   id2 <- randomGeneratorService.generateRandomFactor()
    // } yield (Challenge(id1, id2))
    randomGeneratorService.generateRandomFactor().flatMap { id1 =>
      randomGeneratorService.generateRandomFactor().map { id2 =>
        Challenge(id1, id2)
      }
    }
  }

  def checkChallenge(challengeData: Challenge): ZIO[Any, Nothing, Challenge] = {
    val isCorrect = Some(challengeData.valueA * challengeData.valueB == challengeData.result)
    // Challenge(challengeData.valueA, challengeData.ValueB, challengeData.Result, isCorrect)
    ZIO.succeed(Challenge(challengeData.valueA, challengeData.valueB, challengeData.result, isCorrect))
  }
}

object ChallengeServiceImpl {
  def layer: ZLayer[RandomGeneratorService, Nothing, ChallengeServiceImpl] = ZLayer {
    for {
      generator <- ZIO.service[RandomGeneratorService]
    } yield ChallengeServiceImpl(generator)
  }
}