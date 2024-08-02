package com.axiom.controller

import zio._
import zio.http._
import zio.json._
import com.axiom.model.User
import com.axiom.service.UserService
import zio.http.HttpAppMiddleware.cors
import zio.http.internal.middlewares.Cors.CorsConfig

object UserController {
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
                .map(out => Response.text("Inserted"))
                // .map(out => Response.json(out.toJson))
          }
          .orDie
    }
}
