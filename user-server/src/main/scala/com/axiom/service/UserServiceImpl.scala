package com.axiom.service

import zio._
import io.getquill._
import com.axiom.model.User
import io.getquill.SnakeCase
import com.axiom.repository.UserRepository

case class UserServiceImpl(caRepo: UserRepository) extends UserService {
  def dynamicInsert(user: User): Task[Unit] = {
    println(user)
    caRepo.save(user)//.map(_ => user)
  }

  def getUsers(): Task[List[User]] = {
    println("Getting Users....")
    caRepo.fetchAll()
  }    
}

object UserServiceImpl {
  def layer: ZLayer[UserRepository, Nothing, UserServiceImpl] = ZLayer {
    for {
      repo <- ZIO.service[UserRepository]
    } yield UserServiceImpl(repo)
  }
}
