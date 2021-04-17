// See README.md for license details.

package chisel.chiselbasic

import chisel3._
import chisel3.iotesters._
import chisel3.tester._

class RegTest extends MultiIOModule {
  val load = IO(Input(Bool()))
  val din = IO(Input(UInt(4.W)))
  val y = IO(Output(UInt()))

  private val loadReg = RegInit(2.U(4.W))
  when (load) {
    loadReg := din
  }

  y := loadReg

  // 在这里仅能获取到输入值
//  printf(p"${Binary(din)} \n")
}

class RegTestTester(dut: RegTest) extends PeekPokeTester(dut) {
  val data: UInt = "b1101".U

  // 先准备数据，再 load
  poke(dut.din,data)
  poke(dut.load,true)
  step(1)
  println(peek(dut.y).toInt.toBinaryString)
  // 时钟周期低电平阶段采样，高电平阶段输出，因此这里直接输出输入内容而不是初始值
  expect(dut.y,data)

  poke(dut.load,false)
  step(1)
}

object RegTest extends App {
//  iotesters.Driver.execute(Array("-tgvo", "on"), () => new RegTest) {
//    c => new RegTestTester(c)
//  }

  RawTester.test(new RegTest) { dut =>

    println(dut.y.peek().litValue().toString(2))

    dut.din.poke("b1101".U)
    dut.load.poke(true.B)
    dut.clock.step()

    // 时钟周期低电平阶段采样，高电平阶段输出，因此这里直接输出输入内容而不是初始值
    println(dut.y.peek().litValue().toString(2))
    dut.y.expect("b1101".U)

    dut.din.poke("b1100".U)
    dut.load.poke(false.B)
    dut.clock.step()
    println(dut.y.peek().litValue().toString(2))

  }
}