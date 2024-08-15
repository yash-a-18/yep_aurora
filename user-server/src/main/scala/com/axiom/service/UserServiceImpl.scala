package com.axiom.service

import zio._
import io.getquill._
import com.axiom.model.User
import io.getquill.SnakeCase
import com.axiom.repository.UserRepository

case class UserServiceImpl(userRepo: UserRepository) extends UserService {
  def addUser(user: User): Task[User] = {
    println("Inserting the User....")
    userRepo.create(user).map(_ => user)
  }

  def deleteUser(id: Long): Task[Long] = {
    println("Deleting User....")
    userRepo.delete(id)
  }

  def getUsers(): Task[List[User]] = {
    println("Getting Users....")
    userRepo.fetchAll()
  }

  def searchUser(id: Long): Task[Option[User]] = {
    println("Searching User...")
    userRepo.getById(id)
  }

  def updateUser(id: Long, user: User): Task[Option[User]] = {
    println("Updating User...")
    userRepo.update(id, user)
  }

  def getSortByFirstName(): Task[List[User]] = {
    println("Sorting by First Name...")
    userRepo.sortByFirstName()
  }
}

object UserServiceImpl {
  def layer: ZLayer[UserRepository, Nothing, UserServiceImpl] = ZLayer {
    for {
      repo <- ZIO.service[UserRepository]
    } yield UserServiceImpl(repo)
  }
}
