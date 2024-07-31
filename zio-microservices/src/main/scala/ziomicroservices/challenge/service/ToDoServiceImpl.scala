package ziomicroservices.challenge.service

import zio._
import ziomicroservices.challenge.model.ToDo

// case class ToDoServiceImpl(ref: Ref[List[ToDo]]) extends ToDoService {
//   def addToDo(ToDoData: ToDo): UIO[Unit] = { // Task[Unit] => ZIO[Any, Throwable, Unit], Similar to Future as well
//     ref.update(_ :+ ToDoData).flatMap { _ =>
//       ZIO.succeed(println(ToDoData))
//     }
//     // ref.get.map(println)
//   }
// }

// object ToDoServiceImpl {
//   def layer: ZLayer[Any, Nothing, ToDoService] = {
//     ZLayer.fromZIO {
//     for {
//       ref <- Ref.make(List.empty[ToDo])
//     } yield ToDoServiceImpl(ref)
//   }
//   }
// }

import ziomicroservices.challenge.repository.ToDoRepository

case class ToDoServiceImpl(caRepo: ToDoRepository) extends ToDoService{
  def addToDo(ToDoData: ToDo): Task[Boolean] = {
    for {
      _ <- ZIO.logInfo(s"ToDoData ${ToDoData}")
      _ <- caRepo.save(ToDoData)
    } yield (true)
  }

  def getToDoById(id: String): Task[ToDo] = {
    ZIO.logInfo(s"Here at Get ToDo $id")
    caRepo.find(id)
      .map {
        case Some(data) => data
        case None => null
      }
  }
}

object ToDoServiceImpl {
  def layer: ZLayer[ToDoRepository, Nothing, ToDoServiceImpl] = ZLayer {
    for {
      repo <- ZIO.service[ToDoRepository]
    } yield ToDoServiceImpl(repo)
  }
}
