package com.axiom.controller

import zio._
import zio.http._
import zio.json._
import io.circe.Json
import io.circe.parser._
import io.circe.generic.auto._
import com.axiom.model.User
import com.axiom.service.UserService
import zio.http.HttpAppMiddleware.cors
import zio.http.internal.middlewares.Cors.CorsConfig

object UserController {
  def validateUser(jsonUser: String) = {
    println(s"Here $jsonUser")
    // parse(jsonUser) match {
    //   case Left(fail) => println(s"${fail.toString()}"); None
    //   case Right(value) =>
        decode[User](jsonUser) match {
          case Left(fail)   => println(s"${fail.toString()}"); None
          case Right(value) => Some(value)
        }
    }
  def apply(): Http[UserService, Throwable, Request, Response] =
    Http.collectZIO[Request] {
      case req @ (Method.GET -> Root / "user" / "getall") =>
        // Request with no parameters while Union ?
        UserService.getUsers().map {
          case None => Response.text(s"No user found")
          case out  => Response.json(out.toJson)
        }

      case req @ (Method.POST -> Root / "user" / "get") =>
        val jsonUser = req.body.asString.map(validateUser(_))
        ZIO.succeed(Response.text(s"Parsed JSON: ${jsonUser}"))
      // val parsedJson: Either[io.circe.Error, Json] = parse(jsonString)
      // ZIO.succeed(parsedJson match {
      //   case Right(json) => Response.text(s"Parsed JSON: $json")
      //   case Left(error) => Response.text(s"Failed to parse JSON: $error")
      // })
      // UserService.getUsers(firstName).map(out => Response.json(out.toJson)).orDie

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
                  case None =>
                    Response.text(s"No user found to update, with id $id")
                  case out => Response.json(out.toJson)
                }
          }
    }
}
