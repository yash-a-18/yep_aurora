package com.axiom.example

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ParserTest extends AnyWordSpec with Matchers{
"successful parse" should {
  "return Parsed.Success" in {
    1 should be (1)
    val x = 21
    x should be (1)
    }
  }
}