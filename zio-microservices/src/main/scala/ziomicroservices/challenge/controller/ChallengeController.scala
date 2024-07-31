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
  // val config: CorsConfig =
  // CorsConfig(
  //   allowedOrigin = {  //allows origin from vite server to access routes on server
  //     case origin if origin == Origin.parse("http://localhost:8080").toOption.get =>
  //       Some(AccessControlAllowOrigin.Specific(origin))
  //     case _  => None
  //   },
  // )
  

  def apply(): Http[ChallengeService, Throwable, Request, Response] =
    Http.collectZIO[Request] {
      case Method.GET -> Root / "challenges" / "random" => {
        ChallengeService.createRandomNumbers().map(response => Response.json(response.toJson))
      } 
      case Method.GET -> Root / "challenges" / "mul" => {
        ChallengeService.createRandomMultiplication().map(response => Response.json(response.toJson))
      } //@@ cors(config)
      case req @ Method.POST -> Root / "test" => {
        req.body.asString(Charsets.Utf8).map(Response.text(_))
      }
      case req @ Method.POST -> Root / "challenges" / "check" =>
        req.body.asString
          .flatMap { requestBody =>
            // ZIO.fromEither(requestBody.fromJson[Challenge].left.map(new Exception(_)))
            ZIO.from(requestBody.fromJson[Challenge].left.map(new Exception(_)))//Convert?validating the JSON to Challenge type
          }
          .flatMap { challengeData =>
            ChallengeService.checkChallenge(challengeData)
          }
          .map { result =>
            Response.json(result.toJson)
            //  .addHeader("ContentSecurityPolicy", "default-src 'self'; connect-src 'self' http://localhost:5000;")
          }
    }//@@ cors(config)
}