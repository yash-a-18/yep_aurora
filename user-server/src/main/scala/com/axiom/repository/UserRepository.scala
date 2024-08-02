package com.axiom.repository

import zio._
import com.axiom.model.User

trait UserRepository:
  def save(at: User): Task[Unit]
  def fetchAll(): Task[List[User]]
  
object UserRepository:
  def save(at: User): ZIO[UserRepository, Throwable, Unit] = ZIO.serviceWithZIO[UserRepository](_.save(at))
  def fetchAll(): ZIO[UserRepository, Throwable, List[User]] = ZIO.serviceWithZIO[UserRepository](_.fetchAll())