package com.axiom

object MatchTypes:
    type PartOf[T] = T match 
        case BigInt => Int
        case String => Char
        case List[x] => x // TODO For checking Nested Lists
        case _ => String
        
    def lastComponentOf[T](value: T): PartOf[T] = value match
        case n : BigInt => (n % 10).toInt
        case s : String => 
            if s.isEmpty then throw new NoSuchElementException
            else s.charAt(s.length - 1)
        case l : List[_] => 
            if l.isEmpty then throw new NoSuchElementException
            else l.last
        case _=> "default"

    @main
    def runMatchTypes() =
        println(lastComponentOf(List(List(1,2,3,4,5,6,7,8,9,0))))