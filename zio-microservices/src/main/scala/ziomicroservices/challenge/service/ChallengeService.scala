package ziomicroservices.challenge.service

import zio._
import ziomicroservices.challenge.model.Challenge

trait ChallengeService{
    def createRandomMultiplication(): UIO[Challenge]//ZIO[Any, Nothing, Challenge]
    def createRandomNumbers(): UIO[Challenge]//ZIO[Any, Nothing, Challenge]
    def checkChallenge(challengeData: Challenge): UIO[Challenge]
}

object ChallengeService{
    def createRandomMultiplication(): ZIO[ChallengeService, Nothing, Challenge] = ZIO.serviceWithZIO[ChallengeService](_.createRandomMultiplication())
    def createRandomNumbers(): ZIO[ChallengeService, Nothing, Challenge] = ZIO.serviceWithZIO[ChallengeService](_.createRandomNumbers())
    def checkChallenge(challengeData: Challenge): ZIO[ChallengeService, Nothing, Challenge] = ZIO.serviceWithZIO[ChallengeService](_.checkChallenge(challengeData)
    )
}