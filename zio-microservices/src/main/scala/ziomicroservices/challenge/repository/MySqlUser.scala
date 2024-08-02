package ziomicroservices.challenge.repository

import zio._
import ziomicroservices.challenge.model._
import io.getquill.*
import io.getquill.jdbczio.Quill

case class MySqlUserRepository(quill: Quill.Mysql[SnakeCase])
    extends UserRepository:
  import quill.*
  inline given schema: SchemaMeta[User] =
    schemaMeta[User]("user") // Table name `"User"`
  inline given insMeta: InsertMeta[User] =
    insertMeta[User](_.id) // Columns to generate on its own
  inline given upMeta: UpdateMeta[User] =
    updateMeta[User](_.id)

  def fetchAll(): Task[List[User]] = {
    val fetchQuery = quote {
      query[User]
    }

    run(fetchQuery).tapError { error =>
      ZIO.logError(s"Failed to fetch users: ${error.getMessage}")
    }
  }

  def save(user: User): Task[User] = {
    println(s"Rep: $user")
    println(s"Database: ${quill.ds.getConnection().getCatalog()}")
    println(quill.dsDelegate)
    println(schema)
    println(insMeta)
    // ====================
    for {
      _ <- ZIO.logInfo(s"Inserting user: $user")
      // _ <- ZIO.logInfo(s"Lifted user: ${lift(user)}")  // This will show the representation of the lift but not the value
      id <- run{query[User]
        .insertValue(lift(user))
        .returningGenerated(_.id)}.mapError(error =>
        new Exception(s"Insertion failed: ${error.getMessage}")
      )
      fetchedUser <- run(query[User].filter(_.id == lift(id)))
        .mapError(error =>
          new Exception(s"Fetching failed: ${error.getMessage}")
        )
        .map(_.headOption)
      user <- fetchedUser match {
        case Some(u) => ZIO.succeed(u)
        case None => ZIO.fail(new Exception("User not found after insertion"))
      }
    } yield user
    // ============================
    // println(insertQuery)
    //   run(insertQuery).flatMap { id =>
    //   println(s"Generated ID: $id")
    //   run {
    //     query[User].filter(_.id == lift(id)).take(1)
    //   }.map(_.headOption.getOrElse(throw new Exception("Inserted user not found")))
    // }

    //   val insertQuery = quote{
    //       query[User]
    //       .insert(_.id -> lift(user.id),
    // _.firstName -> lift(user.firstName),
    // _.lastName -> lift(user.lastName))
    //       .returning(u=>u)
    //   }
    // println(insertQuery)
    //   run(insertQuery)
  }

object MySqlUserRepository:

  val layer = ZLayer {
    ZIO.service[Quill.Mysql[SnakeCase]].map(quill => MySqlUserRepository(quill))
  }
