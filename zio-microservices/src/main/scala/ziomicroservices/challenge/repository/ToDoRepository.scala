package ziomicroservices.challenge.repository

import zio._
import ziomicroservices.challenge.model._

trait ToDoRepository:
  def save(at: ToDo): Task[String]
  def find(id: String): Task[Option[ToDo]]
  
object ToDoRepository:
  def save(at: ToDo): ZIO[ToDoRepository, Throwable, String] = ZIO.serviceWithZIO[ToDoRepository](_.save(at))
  def find(id: String): ZIO[ToDoRepository, Throwable, Option[ToDo]] = ZIO.serviceWithZIO[ToDoRepository](_.find(id))