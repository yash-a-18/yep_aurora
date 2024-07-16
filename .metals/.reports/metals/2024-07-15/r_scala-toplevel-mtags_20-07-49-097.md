error id: file:///C:/Users/yasha/OneDrive/Documents/GitHub/yep_aurora/hello-world/src/main/scala/Main.scala:[1870..1870) in Input.VirtualFile("file:///C:/Users/yasha/OneDrive/Documents/GitHub/yep_aurora/hello-world/src/main/scala/Main.scala", "def msg = "I was compiled by Scala 3. :)"

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

def ")
file:///C:/Users/yasha/OneDrive/Documents/GitHub/yep_aurora/hello-world/src/main/scala/Main.scala
file:///C:/Users/yasha/OneDrive/Documents/GitHub/yep_aurora/hello-world/src/main/scala/Main.scala:43: error: expected identifier; obtained eof
def 
    ^
#### Short summary: 

expected identifier; obtained eof