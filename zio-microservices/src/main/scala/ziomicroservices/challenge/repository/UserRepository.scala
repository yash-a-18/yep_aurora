package ziomicroservices.challenge.repository

import zio._
import ziomicroservices.challenge.model._

trait UserRepository:
  def save(at: User): Task[User]
  
object UserRepository:
  def save(at: User): ZIO[UserRepository, Throwable, User] = ZIO.serviceWithZIO[UserRepository](_.save(at))