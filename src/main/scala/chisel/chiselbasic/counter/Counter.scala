// See README.md for license details.

package chisel.chiselbasic.counter

import chisel3._
import chisel._
import chisel3.iotesters._

class Counter extends MultiIOModule {

  val y = IO(Output(UInt()))
//  y := genUpCounter(4)
  y := genDownCounter(4)

  // ASIC 递减计数器的比较器电路比递增的简单，需要更小的代价，FPGA 因为使用查找表，一般没区别
  def genDownCounter(n: Int): UInt = {
    val cntReg = RegInit(n.U)
    cntReg := cntReg - 1.U
    when(cntReg === 0.U) {
      cntReg := n.U
    }
    cntReg
  }

  def genUpCounter(n: Int): UInt = {
    val cntReg = RegInit(0.U(8.W))
    cntReg := Mux(cntReg === n.U, 0.U, cntReg + 1.U)
    cntReg
  }
}

class Tester(dut: Counter) extends PeekPokeTester(dut) {
  step(4)
//  expect(dut.y,4) // up
  expect(dut.y,0) // down
  step(1)
//  expect(dut.y,0) // up
  expect(dut.y,4)   // down

}

object Counter extends App {
  genGraph(new Counter, Array("--lowFir"))
//  iotesters.Driver.execute(Array("-tgvo", "on"), () => new Counter) {
//    c => new Tester(c)
//  }
}