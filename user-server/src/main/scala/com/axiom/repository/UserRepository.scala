package com.axiom.repository

import zio._
import com.axiom.model.User

trait UserRepository:
  def create(user: User): Task[User]
  def update(id: Long, user: User): Task[Option[User]]
  def getById(id: Long): Task[Option[User]]
  def delete(id: Long): Task[Long]
  def fetchAll(): Task[Option[List[User]]]
  
object UserRepository:
  def create(user: User): ZIO[UserRepository, Throwable, User] = ZIO.serviceWithZIO[UserRepository](_.create(user))
  def update(id: Long, user: User): ZIO[UserRepository, Throwable, Option[User]] = ZIO.serviceWithZIO[UserRepository](_.update(id, user))
  def getById(id: Long): ZIO[UserRepository, Throwable, Option[User]] = ZIO.serviceWithZIO[UserRepository](_.getById(id))
  def delete(id: Long): ZIO[UserRepository, Throwable, Long] = ZIO.serviceWithZIO[UserRepository](_.delete(id))
  def fetchAll(): ZIO[UserRepository, Throwable, Option[List[User]]] = ZIO.serviceWithZIO[UserRepository](_.fetchAll())