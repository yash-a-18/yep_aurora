package ziomicroservices.challenge

import zio._
import zio.http._
import zio.http.Header.{AccessControlAllowMethods, AccessControlAllowOrigin, Origin}
import zio.http.HttpAppMiddleware.cors
import zio.http.internal.middlewares.Cors.CorsConfig

import zio.http.Server
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}


import ziomicroservices.challenge.controller.{ChallengeController, ToDoController, UserController}
import ziomicroservices.challenge.service.{ChallengeServiceImpl, RandomGeneratorServiceImpl, ToDoServiceImpl, UserServiceImpl}
import ziomicroservices.challenge.model.{ToDo,User}
import ziomicroservices.challenge.repository.{InMemoryToDoRepository, MySqlUserRepository}
import zio.http.endpoint.Routes
import zio.http.Header.AccessControlAllowHeaders

import io.getquill.*
import io.getquill.jdbczio.Quill


object Main extends ZIOAppDefault {
  // Define your CORS configuration
  private val corsConfig: CorsConfig = CorsConfig(
    allowedOrigin = {
      case origin if origin == Origin.parse("http://127.0.0.1:8080").toOption.get =>
        Some(AccessControlAllowOrigin.Specific(origin))
      case _ => None
    },
    allowedMethods = AccessControlAllowMethods(
      Method.GET,
      Method.POST,
      Method.PUT,
      Method.DELETE
    ),
    allowedHeaders = AccessControlAllowHeaders(
      "Content-Type",
      "Authorization",
      "*"
    )
  )
  def run: ZIO[Environment & ZIOAppArgs & Scope, Throwable, Any] =
    // val httpApps =  ChallengeController()
    val httpApps =  UserController()
    Server
      .serve(
        httpApps.withDefaultErrorResponse
        // cors(corsConfig)(httpApps) // Apply CORS middleware manually
        //   .withDefaultErrorResponse
      )
      .provide(
        Server.defaultWithPort(5000),
        // ChallengeServiceImpl.layer,
        // RandomGeneratorServiceImpl.layer,
        //ToDoServiceImpl.layer,
        //InMemoryToDoRepository.layer
        UserServiceImpl.layer,
        MySqlUserRepository.layer,
        Quill.Mysql.fromNamingStrategy(SnakeCase),
        Quill.DataSource.fromPrefix("myDatabase") //for configuration file
      )
}

