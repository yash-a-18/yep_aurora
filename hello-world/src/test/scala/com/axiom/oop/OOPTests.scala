package com.axiom.oop

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.io.StdIn._

class OOPTests extends AnyWordSpec with Matchers{
  object talkType extends Enumeration{
    val Woof, Meow, Hiss, Neigh = Value
  }

    trait Animal {
      val nameOfAnimal: String = "Bunty"
      val hasTail : Boolean = false
      def talk():String
      def numberOfLegs(): Int
    }
    class Dog(name:String) extends Animal{
      override val hasTail = true
      def talk ():String ={
        val sound: talkType.Value = talkType.Woof
        sound.toString 
      }
      override val nameOfAnimal = name
      def numberOfLegs(): Int = ???
    }
    class Cat(name:String) extends Animal{
      override val hasTail = true 
      def talk ():String ={
        val sound: talkType.Value = talkType.Meow
        sound.toString
      }
      println("Enter the name:")
      // override val nameOfAnimal = readLine()
      override val nameOfAnimal = name
      def numberOfLegs(): Int = ???
    }
    class Snake extends Animal{
      // val hasTail = true 
      def talk ():String ={
        val sound: talkType.Value = talkType.Hiss
        sound.toString
      }
      def numberOfLegs(): Int = {
        0
      }
    }

    "determine tail and talk" should {
      "For Dog" in {
        val dog = new Dog("Tommy")
        dog.hasTail should be (true)
        dog.talk() should be ("Woof")
        dog.nameOfAnimal should be ("Tommy")
        // info(s"${dog.talk()}")
      }
      "For Cats" in {
        val cat = new Cat("Missy")
        cat.hasTail should be (true)
        cat.talk() should be ("Meow")
        cat.nameOfAnimal should be ("Missy")
      }
      "For Snake" in {
        val snake = new Snake()
        snake.hasTail should be (false)
        snake.talk() should be ("Hiss")
        snake.nameOfAnimal should be ("Bunty")
        snake.numberOfLegs() should be (0)
      }
    }
}