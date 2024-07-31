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

import ziomicroservices.challenge.model.ToDo
import ziomicroservices.challenge.service.ToDoService

object ToDoController {
  // val config: CorsConfig =
  //   CorsConfig(
  //     allowedOrigin = {
  //       case origin@Origin.Value(_, host, _) => Some(AccessControlAllowOrigin.All)
  //       case _ => Some(AccessControlAllowOrigin.All)
  //     },
  //     allowedMethods = AccessControlAllowMethods(Method.PUT, Method.DELETE, Method.GET, Method.POST),
  //   )
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
  def apply(): Http[ToDoService, Throwable, Request, Response] =
    // Http.collectZIO[Request] {
    //   case req @ Method.POST -> Root / "todo" / "add" =>
    //     req.body.asString
    //       .flatMap { requestBody =>
    //         ZIO.from(requestBody.fromJson[ToDo].left.map(new Exception(_)))
    //       }
    //       .flatMap { ToDoData =>
    //         ToDoService.addToDo(ToDoData)
    //       }
    //       .as(Response.status(Status.Ok))
    // }@@ cors(config)

    Http.collectZIO[Request] {
      case Method.GET -> Root / "todo" /"get" / id =>
        ToDoService.getToDoById(id).map(out => Response.json(out.toJson)).orDie

      case req @ (Method.POST -> Root / "todo" / "add") =>
        req.body.asString
          .map(_.fromJson[ToDo])
          .flatMap {
            case Left(e) =>
              ZIO
                .debug(s"Failed to parse the input: $e")
                .as(Response.text(e).withStatus(Status.BadRequest))
            case Right(u) =>
              ToDoService
                .addToDo(u)
                .map(out => Response.json(out.toJson))
          }
          .orDie
    } @@ cors(config)
}
