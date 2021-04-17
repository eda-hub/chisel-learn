// See README.md for license details.

package chisel.chiselbasic.counter

import chisel3._
import chisel3.util._
import chisel._
import chisel3.iotesters._

class ShiftReg extends MultiIOModule {

  val serIn = IO(Input(Bool()))
  val q = IO(Output(UInt()))

  // 内部节点最好用 private 修饰
  private val outReg = RegInit(0.U(4.W))
  // 最新输入的serIn（即 lsb）成为 reg 的最高位，同时前一个最高位变为次高位，即右移
  outReg := Cat(serIn , outReg(3, 1))
  q := outReg
}

class ShiftRegTester(dut: ShiftReg) extends PeekPokeTester(dut) {
  val data = "b1101".U
  println(data.toBools().toString())
  // UInt 转换为 Bools 序列时，顺序自动调整为 lsb 到 msb
  data.asBools().foreach(i => {
    poke(dut.serIn,i)
    step(1)
  })
  // 经过一定周期后，串行变并行
  expect(dut.q, data)
}

object ShiftReg extends App {
  iotesters.Driver.execute(Array("-tgvo", "on"), () => new ShiftReg) {
    c => new ShiftRegTester(c)
  }
}

class ShiftRegParallel extends MultiIOModule {

  val load = IO(Input(Bool()))
  val d = IO(Input(UInt(4.W)))
  val serOut = IO(Output(Bool()))

  private val loadReg = RegInit(0.U(4.W))
  when (load) {
    loadReg := d
  } otherwise {
    // 将寄存器的高位依次移动到最低位
    loadReg := Cat(0.U, loadReg(3, 1))
  }
  serOut := loadReg(0)
}

class ShiftRegParallelTester(dut: ShiftRegParallel) extends PeekPokeTester(dut) {
  val data = "b1101".U
  // 先准备数据，再 load
  poke(dut.d,data)
  poke(dut.load,true)

  // 这时已经输出 1 位
  step(1)
  expect(dut.serOut, data(0))

  poke(dut.load,false)

  val length = unsignedBitLength(data)
  for (i <- 1 to length - 1) {
    step(1)
//    println(s"$i ${data(i).toString()}")
    expect(dut.serOut, data(i))
  }
}

object ShiftRegParallel extends App {
  iotesters.Driver.execute(Array("-tgvo", "on"), () => new ShiftRegParallel) {
    c => new ShiftRegParallelTester(c)
  }
}