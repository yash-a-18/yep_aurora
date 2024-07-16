package questions

def sumOfSquares(numbers: List[Int]): Int =
    val theSum = numbers.map((i: Int) => i*i).sum
    return theSum