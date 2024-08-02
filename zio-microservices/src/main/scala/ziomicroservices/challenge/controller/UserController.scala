package ziomicroservices.challenge.controller

import zio._

import zio.http.Header.{
  AccessControlAllowHeaders,
  AccessControlAllowMethods,
  AccessControlAllowOrigin,
  Origin
}
import zio.http.HttpAppMiddleware.cors
import zio.http.internal.middlewares.Cors.CorsConfig

import zio.http._
import zio.json._

import ziomicroservices.challenge.model.User
import ziomicroservices.challenge.service.UserService

object UserController {
  val config: CorsConfig =
    CorsConfig(
      allowedOrigin = { // allows origin from vite server to access routes on server
        case origin
            if origin == Origin.parse("http://127.0.0.1:8080").toOption.get =>
          Some(AccessControlAllowOrigin.Specific(origin))
        case _ => None
      },
      allowedMethods = AccessControlAllowMethods(
        Method.PUT,
        Method.DELETE,
        Method.GET,
        Method.POST
      ),
      allowedHeaders = AccessControlAllowHeaders(
        "Content-Type",
        "Authorization",
        "*"
      ) // Optionally, allow all headers using *
    )
  def apply(): Http[UserService, Throwable, Request, Response] =
    Http.collectZIO[Request] {
      case req @ (Method.GET -> Root / "user" / "getall") =>
        UserService.getUsers().map(out => Response.json(out.toJson)).orDie
      case req @ (Method.POST -> Root / "user" / "add") =>
        req.body.asString
          .map(_.fromJson[User])
          .flatMap {
            case Left(e) =>
              ZIO
                .debug(s"Failed to parse the input: $e")
                .as(Response.text(s"I am sending the error $e").withStatus(Status.BadRequest))
            case Right(u) =>
              UserService
                .dynamicInsert(u)
                .map(out => Response.json(out.toJson))
          }
          .orDie
    } @@ cors(config)
}
