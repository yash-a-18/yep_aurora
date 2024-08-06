package com.axiom.service

import zio._
import com.axiom.model.User

trait UserService{
    def addUser(user:User): Task[User]
    def deleteUser(id: Long): Task[Long]
    def getUsers(): Task[List[User]]
    def searchUser(id: Long): Task[Option[User]]
    def updateUser(id: Long, user: User): Task[Option[User]]
}

object UserService{
    def addUser(UserData: User): ZIO[UserService, Throwable, User] = ZIO.serviceWithZIO[UserService](_.addUser(UserData))
    def deleteUser(id: Long): ZIO[UserService, Throwable, Long] = ZIO.serviceWithZIO[UserService](_.deleteUser(id))
    def getUsers(): ZIO[UserService, Throwable, List[User]] = ZIO.serviceWithZIO[UserService](_.getUsers())
    def searchUser(id: Long): ZIO[UserService, Throwable, Option[User]] = ZIO.serviceWithZIO[UserService](_.searchUser(id))
    def updateUser(id: Long, user: User): ZIO[UserService, Throwable, Option[User]] = ZIO.serviceWithZIO[UserService](_.updateUser(id, user))
}