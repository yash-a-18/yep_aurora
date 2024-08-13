import com.axiom.DependentTypes.Outer

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class DependentTypesSpec extends AnyWordSpec with Matchers{
"Type mis-match" should {
  "occur for someProcess method with different inner instances" in {
    val outerA = new Outer
    val outerB = new Outer
    val innerA = new outerA.Inner //instance-dependent type, because to create new object we dependent on Outer class's object
    val innerB = new outerB.Inner

    // This will compile because we're assigning the correct type
    outerA.someProcess(innerA)
    assertDoesNotCompile("outerA.someProcess(innerB)")
    }
  "occur for Inner classes from different Outer instances" in {
    val outerA = new Outer
    val outerB = new Outer

    // This will compile because we're assigning the correct type
    val innerA: outerA.Inner = new outerA.Inner
    assertDoesNotCompile("val innerA: outerA.Inner = outerB.Inner")
    }
  }

  /*
  To Solve above compilation error, 
  we use
  - path-dependent types 
  - path-dependent types can be called as parent of all instance-dependent types
   */

   "Path-dependent types" should {
    "compile successfully" in {
      val outerA = new Outer
      val outerB = new Outer
      val innerA = new outerA.Inner

      outerB.someGeneralProcess(innerA)
      succeed
    }
   }
}