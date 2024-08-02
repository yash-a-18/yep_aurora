package com.axiom.service

import zio._
import com.axiom.model.User

trait UserService{
    def dynamicInsert(user:User): Task[Unit]
    def getUsers(): Task[List[User]]
}

object UserService{
    def dynamicInsert(UserData: User): ZIO[UserService, Throwable, Unit] = ZIO.serviceWithZIO[UserService](_.dynamicInsert(UserData))
    def getUsers(): ZIO[UserService, Throwable, List[User]] = ZIO.serviceWithZIO[UserService](_.getUsers())
}