import zio._
import zio.http._
import io.getquill.*
import zio.http.Server
import zio.http.endpoint.Routes
import io.getquill.jdbczio.Quill
import zio.http.HttpAppMiddleware.cors
import com.axiom.service.UserServiceImpl
import com.axiom.controller.UserController
import com.axiom.repository.MySqlUserRepository
import zio.http.Header.AccessControlAllowHeaders
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}
import zio.http.internal.middlewares.Cors.CorsConfig
import zio.http.Header.{AccessControlAllowMethods, AccessControlAllowOrigin, Origin}

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
    val httpApps =  UserController()
    Server
      .serve(
        // httpApps.withDefaultErrorResponse
        cors(corsConfig)(httpApps) // Apply CORS middleware manually
          .withDefaultErrorResponse
      )
      .provide(
        Server.defaultWithPort(5000),
        UserServiceImpl.layer,
        MySqlUserRepository.layer,
        Quill.Mysql.fromNamingStrategy(SnakeCase),
        Quill.DataSource.fromPrefix("userDatabase") //for configuration file
      )
}

