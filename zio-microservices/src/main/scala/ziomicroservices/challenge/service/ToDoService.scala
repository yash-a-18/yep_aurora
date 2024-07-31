package ziomicroservices.challenge.service

import zio._
import ziomicroservices.challenge.model.ToDo

trait ToDoService{
    // def addToDo(ToDoData: ToDo): UIO[Unit]
    def addToDo(ToDoData: ToDo): Task[Boolean]
    def getToDoById(id: String): Task[ToDo]
}

object ToDoService{
    // def addToDo(ToDoData: ToDo): ZIO[ToDoService, Nothing, Unit] = ZIO.serviceWithZIO[ToDoService](_.addToDo(ToDoData))
    def addToDo(ToDoData: ToDo): ZIO[ToDoService, Throwable, Boolean] = ZIO.serviceWithZIO[ToDoService](_.addToDo(ToDoData))
    def getToDoById(id: String): ZIO[ToDoService, Throwable, ToDo] = ZIO.serviceWithZIO[ToDoService](_.getToDoById(id))
}