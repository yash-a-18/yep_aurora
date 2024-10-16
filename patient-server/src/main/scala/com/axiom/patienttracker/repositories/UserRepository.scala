package com.axiom.patienttracker.repositories

import zio.*
import com.axiom.patienttracker.domain.data.User
import io.getquill.jdbczio.Quill
import io.getquill.*

trait UserRepository:
    def create(user: User): Task[User]
    def getById(id: Long): Task[Option[User]]
    def getByEmail(email: String): Task[Option[User]]
    def update(id: Long, op: User => User): Task[User]
    def delete(id: Long): Task[User]

class UserRepositoryLive private (quill: Quill.Postgres[SnakeCase]) extends UserRepository:
    import quill.*
    
    inline given schema: SchemaMeta[User] = schemaMeta[User]("users")
    inline given insMeta: InsertMeta[User] = insertMeta[User](_.id)
    inline given upMeta: UpdateMeta[User] = updateMeta[User](_.id)

    override def create(user: User): Task[User] = 
        run{
            query[User]
            .insertValue(lift(user))
            .returning(u => u)
        }
    override def getById(id: Long): Task[Option[User]] = 
        run{
            query[User]
            .filter(_.id == lift(id))
        }.map(_.headOption)
    override def getByEmail(email: String): Task[Option[User]] = 
        run{
            query[User]
            .filter(_.email == lift(email))
        }.map(_.headOption)
    override def update(id: Long, op: User => User): Task[User] = 
        for{
            current <- getById(id).someOrFail(new RuntimeException(s"Could not update: missing id: $id"))
            updated <- run{
                query[User]
                .updateValue(lift(op(current)))
                .returning(u => u)
            }
        } yield updated
    override def delete(id: Long): Task[User] = 
        run{
            query[User]
            .filter(_.id == lift(id))
            .delete
            .returning(u => u)
        }

object UserRepositoryLive:
    val layer = ZLayer{
        for{
            quill <- ZIO.service[Quill.Postgres[SnakeCase]]
        }yield new UserRepositoryLive(quill)
    }