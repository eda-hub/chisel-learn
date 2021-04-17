// See README.md for license details.

package chisel.chiselbasic.counter

import chisel3._
import chisel._
import chisel3.iotesters._
import chisel3.util.unsignedBitLength

class PWM(nrCycles: Int, din: Int) extends MultiIOModule {

  val y = IO(Output(UInt()))
  y := pwm(nrCycles,din.U)
  /**
   * unsignedBitLength 得到要表示一个无符号整数所需要的位数
   * @param nrCycles  PWM 信号周期
   * @param din 脉冲宽度周期  duty cycle
   * @return
   */
  def pwm(nrCycles: Int, din: UInt): Bool = {
    val cntReg = RegInit(0.U(unsignedBitLength(nrCycles -1).W))
    cntReg := Mux(cntReg === (nrCycles -1).U, 0.U, cntReg + 1.U)
    din > cntReg
  }
}

class PWMTester(dut: PWM) extends PeekPokeTester(dut) {
  step(20)
}

object PWM extends App {
  iotesters.Driver.execute(Array("-tgvo", "on"), () => new PWM(10,3)) {
    c => new PWMTester(c)
  }
}