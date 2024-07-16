package com.axiom.concurrency

import scala.concurrent.{Future, ExecutionContext}
import scala.util.Random

object AsyncExample {
  implicit val ec: ExecutionContext = ExecutionContext.global

  def asyncComputation(): Future[Int] = Future {
    // Simulate a long-running computation
    Thread.sleep(1000)
    Random.nextInt(100) // Return a random number between 0 and 99
  }
}

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class AsyncExampleSpec extends AnyFlatSpec with Matchers {

  "asyncComputation" should "return a value between 0 and 99" in {
    val futureResult = AsyncExample.asyncComputation()
    
    // Await the result of the Future
    val result = Await.result(futureResult, 2.seconds)
    info(s"$result")
    // Check if the result is in the expected range
    result should be >= 0
    result should be < 100
  }
}
