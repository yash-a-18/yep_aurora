package com.axiom

//Dependent Types
object DependentTypes:

  class Outer:
    class Inner
    object InnerObject
    type InnerType

    def someProcess(inner: Inner): Unit =
      println(s"Hello from Process: $inner") 
    
    //path-dependent type
    def someGeneralProcess(inner: Outer#Inner): Unit =
      println(s"Hello from General Process: $inner")

  //We could also write methods with dependent types
  /* 
  Only other way, except generics, that allows a method to have a different return Type 
  depending on the value of the argument
  */
  trait Record:
    type Key

  def getId(record: Record): record.Key = ???