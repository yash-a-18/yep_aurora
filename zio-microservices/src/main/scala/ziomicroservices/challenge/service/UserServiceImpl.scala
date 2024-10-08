package ziomicroservices.challenge.service

import zio._
import ziomicroservices.challenge.model.User
import io.getquill._
import io.getquill.SnakeCase
import ziomicroservices.challenge.repository.UserRepository

case class UserServiceImpl(caRepo: UserRepository) extends UserService {
  def dynamicInsert(user: User): Task[User] = {
    println(user)
    caRepo.save(user).map(_ => user)
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
