package ziomicroservices.challenge.service

import zio._
import ziomicroservices.challenge.model.User

trait UserService{
    def dynamicInsert(user:User): Task[User]
}

object UserService{
    def dynamicInsert(UserData: User): ZIO[UserService, Throwable, User] = ZIO.serviceWithZIO[UserService](_.dynamicInsert(UserData))
}