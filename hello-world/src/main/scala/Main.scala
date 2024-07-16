import questions.sumOfSquares

def msg = "I was compiled by Scala 3. :)"

def msg2 = "Hello There!"

def hello(): Unit =
  println("Hello world!")
  println(msg)

@main def learningNew(): Unit =
  hello()
  println(msg2)
  val last_val = listMethod()
  println(last_val)
  val numbers = List.range(1, 10)
  val greaterNumbers = lambdaFunc(numbers)
  println(greaterNumbers)
  multiCollectionMethods()
  println(sum(numbers))

  //using reduce
  numbers.reduce(add)
  val mulGN = greaterNumbers.reduce(_ * _)
  println(s"Multiplication of all greater numbers using reduce is $mulGN")

  val theSumOfSquares = sumOfSquares(numbers)
  println(s"The sum of squares is $theSumOfSquares")

def listMethod(): Boolean = 
    val a = List(10, 20, 30, 40, 10)      // List(10, 20, 30, 40, 10)
    // println(a.distinct)                            // List(10, 20, 30, 40)
    // println(a.drop(2))                             // List(30, 40, 10)
    // println(a.dropRight(2))                        // List(10, 20, 30)
    // println(a.head)                                // 10
    // println(a.headOption)                          // Some(10)
    // println(a.init)                                // List(10, 20, 30, 40)
    // println(a.intersect(List(19,20,21)))           // List(20)
    // println(a.last)                                // 10
    // println(a.lastOption)                          // Some(10)
    // println(a.slice(2,4)) //first included and last not included                         // List(30, 40)
    // println(a.tail)                                // List(20, 30, 40, 10)
    // println(a.take(3))                             // List(10, 20, 30)
    // println(a.takeRight(2))
    // println(a(2))  
    return a.last == 10     // return in optional

def lambdaFunc(numbers: List[Int]): List[Int] = 
    // println(numbers.filter((i: Int) => i < 5))   // 1. most explicit form
    // println(numbers.filter((i) => i < 5))        // 2. `Int` is not required
    // println(numbers.filter(i => i < 5))          // 3. the parens are not required
    println(numbers.filter(_ < 5))               // 4. `i` is not required
    numbers.filter(_ > 5)

def multiCollectionMethods(): Unit = 
    val a = List.range(1, 100)
    println(a.filter(_ < 40)
            .takeWhile(_ < 30)
            .map(_ - 10))

    val names = List("adam", "brandy", "chris", "david")
    // val firstName :: lastName = names
    println(names.map((s:String) => s.contains('a')))
    println(names.filter(_.contains('a')))
    val nameLengthsMap = names.map(s => (s.capitalize, s.length)).toMap
    println(nameLengthsMap)

// Sum of list value, recursively
def sum(list: List[Int]): Int = list match
    case Nil => 0
    case x :: xs => {
        val theSum = x + sum(xs)
        val headOfxs = xs.headOption.getOrElse(0)
        println(s"Sum of $x and $headOfxs is $theSum")
        return theSum
    }

def add(x: Int, y: Int): Int =
    val theSum = x + y
    println(s"received $x and $y, their sum is $theSum")
    theSum