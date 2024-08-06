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
                .debug(
                  s"Failed to parse the input: $e"
                ) // Prints on the terminal
                .as(
                  Response
                    .text(s"Failed to parse the input: $e")
                    .withStatus(Status.BadRequest)
                ) // Sends response to the Server
            case Right(u) =>
              UserService
                .addUser(u)
                .map(out => Response.json(out.toJson))
          }
          .orDie

      case req @ (Method.DELETE -> Root / "user" / "delete" / id) =>
        UserService
          .deleteUser(id.toLong)
          .map {
            case 0 => Response.text(s"No user found with id $id")
            case n => Response.text(s"User with id $id is deleted")
          }

      case req @ (Method.GET -> Root / "user" / "search" / id) =>
        UserService
          .searchUser(id.toLong)
          .map {
            case None => Response.text(s"No user found with id $id")
            case out  => Response.json(out.toJson)
          }

      case req @ (Method.PUT -> Root / "user" / "update" / id) =>
        req.body.asString
          .map(_.fromJson[User])
          .flatMap {
            case Left(e) =>
              ZIO
                .debug(
                  s"Failed to parse the input: $e"
                ) // Prints on the terminal
                .as(
                  Response
                    .text(s"Failed to parse the input: $e")
                    .withStatus(Status.BadRequest)
                ) // Sends response to the Server
            case Right(u) =>
              UserService
                .updateUser(id.toLong, u)
                .map {
                  case None => Response.text(s"No user found to update with id $id")
                  case out  => Response.json(out.toJson)
                }
          }
          .orDie
    }
}
