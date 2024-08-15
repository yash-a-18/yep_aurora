package com.axiom.service

import zio.*
import zio.test
import zio.test.ZIOSpecDefault
import zio.test.Spec
import zio.test.TestEnvironment
import com.axiom.model.User
// import com.axiom.syntax.*
import com.axiom.repository.UserRepository

object UserServiceTest extends ZIOSpecDefault:

    val service = ZIO.serviceWithZIO[UserService]

    val testRepoLayer = ZLayer.succeed(
        new UserRepository {
            override def create(user: User): Task[User] = ???
            override def update(id: Long, user: User): Task[Option[User]] = ???
            override def getById(id: Long): Task[Option[User]] = ???
            override def delete(id: Long): Task[Long] = ???
            override def fetchAll(): Task[List[User]] = ???
            override def sortByFirstName(): Task[List[User]] = ???
        }
    )
    override def spec: Spec[TestEnvironment & Scope, Any] = 
        suite("UserServiceTest")(
            test("addUser"){
                val userZIO = service(_.addUser(User(1,"Yash","Amethiya")))
                userZIO.assert{user =>
                    user.firstName == "Yash"
                    user.lastName == "Amethiya"
                }
            }
        ).provide(UserService.layer, testRepoLayer)
