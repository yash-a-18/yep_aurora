package ziomicroservices.challenge
import zio._

import zio.http.Header.{AccessControlAllowMethods, AccessControlAllowOrigin, Origin}
import zio.http.HttpAppMiddleware.cors
import zio.http.internal.middlewares.Cors.CorsConfig
import zio.http._
import zio.http.Server
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}


import ziomicroservices.challenge.controller.ChallengeController
import ziomicroservices.challenge.service.{ChallengeServiceImpl, RandomGeneratorServiceImpl}


object Main extends ZIOAppDefault {
  def run: ZIO[Environment & ZIOAppArgs & Scope, Throwable, Any] =
    val httpApps =  ChallengeController()
    Server
      .serve(
        httpApps.withDefaultErrorResponse
      )
      .provide(
        Server.defaultWithPort(5000),
        RandomGeneratorServiceImpl.layer,
        ChallengeServiceImpl.layer
      )
}