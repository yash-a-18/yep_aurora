package ziomicroservices.challenge.controller
import zio._

import zio.http.Header.{AccessControlAllowMethods, AccessControlAllowOrigin, Origin}
import zio.http.HttpAppMiddleware.cors
import zio.http.internal.middlewares.Cors.CorsConfig


import zio.http._
import zio.json._

import ziomicroservices.challenge.model.Challenge
import ziomicroservices.challenge.service.ChallengeService


object ChallengeController{
  val config: CorsConfig =
    CorsConfig(
      allowedOrigin = {
        case origin@Origin.Value(_, host, _) => Some(AccessControlAllowOrigin.All)
        case _ => Some(AccessControlAllowOrigin.All)
      },
      allowedMethods = AccessControlAllowMethods(Method.PUT, Method.DELETE, Method.GET, Method.POST),
    )
  def apply(): Http[ChallengeService, Throwable, Request, Response] =
    Http.collectZIO[Request] {
      case Method.GET -> Root / "challenges" / "random" => {
        ChallengeService.createRandomNumbers().map(response => Response.json(response.toJson))
      } 
      case Method.GET -> Root / "challenges" / "mul" => {
        ChallengeService.createRandomMultiplication().map(response => Response.json(response.toJson))
      } //@@ cors(config)
      case req @ Method.POST -> Root / "challenges" / "check" =>
        req.body.asString
          .flatMap { requestBody =>
            ZIO.fromEither(requestBody.fromJson[Challenge].left.map(new Exception(_)))
          }
          .flatMap { challengeData =>
            ChallengeService.checkChallenge(challengeData)
          }
          .map { result =>
            Response.json(result.toJson)
          }
    }@@ cors(config)
}