package ziomicroservices.challenge.repository

import zio._
import ziomicroservices.challenge.model._
import io.getquill.*
import io.getquill.jdbczio.Quill


case class MySqlUserRepository(quill: Quill.Mysql[SnakeCase]) extends UserRepository:
    import quill.*
    inline given schema: SchemaMeta[User] = schemaMeta[User]("User") //Table name `"User"`
    inline given insMeta: InsertMeta[User] = insertMeta[User](_.id) //Columns to generate on its own

    def save(user: User): Task[User] = 
        val insertQuery = quote{
            query[User]
            .insertValue(lift(user))
            .returning(u=>u)
        }
        run(insertQuery)


object MySqlUserRepository:

  val layer = ZLayer{
    ZIO.service[Quill.Mysql[SnakeCase]].map(quill => MySqlUserRepository(quill))
  }