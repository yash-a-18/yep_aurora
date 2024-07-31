package ziomicroservices.challenge.repository

import zio._
import ziomicroservices.challenge.model._


case class InMemoryToDoRepository(map: Ref[Map[String, ToDo]]) extends ToDoRepository:

    def save(att: ToDo): UIO[String] = 
        for
            id <- Random.nextUUID.map(_.toString)
            _  <- map.update(_ + (id -> att))
        yield id
    
    def find(id: String): UIO[Option[ToDo]] = 
      map.get.map(_.get(id))


object InMemoryToDoRepository:

  def layer: ZLayer[Any, Nothing, InMemoryToDoRepository] =
    ZLayer.fromZIO(
      Ref.make(Map.empty[String, ToDo]).map(new InMemoryToDoRepository(_))
    )