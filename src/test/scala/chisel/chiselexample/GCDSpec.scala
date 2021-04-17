// See README.md for license details.

package chisel.chiselexample

import chisel3._
import chisel3.tester._
import chisel.chiselexample.GCD
import org.scalatest.FreeSpec

/**
  * This is a trivial example of how to run this Specification
  * From within sbt use:
  * {{{
  * testOnly gcd.GCDSpec
  * }}}
  * From a terminal shell use:
  * {{{
  * sbt 'testOnly gcd.GCDSpec'
  * }}}
  */
class GCDSpec extends FreeSpec with ChiselScalatestTester {

  /**
   * compute the gcd and the number of steps it should take to do it
   *
   * @param a positive integer
   * @param b positive integer
   * @return the GCD of a and b
   */
  def computeGcd(a: Int, b: Int): (Int, Int) = {
    var x = a
    var y = b
    var depth = 1
    while(y > 0 ) {
      if (x > y) {
        x -= y
      }
      else {
        y -= x
      }
      depth += 1
    }
    (x, depth)
  }

  "GCD should calculate proper greatest common denominator" in {
    test(new GCD) { dut =>
      for(i <- 1 to 40 by 3) {
        for (j <- 1 to 40 by 7) {
          dut.io.value1.poke(i.U)
          dut.io.value2.poke(j.U)
          dut.io.loadingValues.poke(1.B)
          dut.clock.step()

          dut.io.loadingValues.poke(0.B)

          val (expected_gcd, steps) = computeGcd(i, j)

          dut.clock.step(steps - 1) // -1 is because we step(1) already to toggle the enable
          dut.io.outputGCD.expect(expected_gcd.U)
          dut.io.outputValid.expect(1.B)
        }
      }
    }
  }
}
