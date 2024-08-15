package com.axiom.repository

import zio._
import io.getquill.*
import com.axiom.model.User
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
    run{
      query[User]
    }.map(users => users)
  }
    
  def create(user: User): Task[User] = {
    run {
         query[User].insertValue(lift(user)).returning(_.id)
    }.flatMap(id => getById(id)).map(_=>user)
  }

  def update(id: Long, user: User): Task[Option[User]] = {
    run {
          query[User].filter(_.id == lift(id)).updateValue(lift(user))
    }.flatMap(id => getById(id)).map(_.headOption)
  }

  def delete(id: Long): Task[Long] = {
    run {
      query[User].filter(_.id == lift(id)).delete
    }
  }
  
  def getById(id: Long): Task[Option[User]] = {
    run{
      query[User].filter(_.id == lift(id))
    }.map(_.headOption)
  }

  def sortByFirstName(): Task[List[User]] = {
    run{
      query[User].sortBy(u => u.firstName) //if First Name is same then by default it will sort by id in ascending order
    }
    
  }

object MySqlUserRepository:
  val layer = ZLayer {
    ZIO.service[Quill.Mysql[SnakeCase]].map(quill => MySqlUserRepository(quill))
  }