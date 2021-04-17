// See README.md for license details.

package chisel.chiselbasic

import chisel3._
import chisel._

class RegModule extends MultiIOModule {
  val a = IO(Input(UInt(32.W)))
  val en = IO(Input(Bool()))
  val y = IO(Output(UInt()))

//  val reg = RegInit(0.U)
//  // 寄存器必须连接到某信号，否则没有用
//  reg := a
//  y := reg + 1.U

  // 可以在定义时直接连接到某信号和初始化，不能使用 RegInit(t: T, init: T)
  val reg = RegNext(a,0.U)
  y := reg + 1.U

  // 通过 Vec 定义一组寄存器，https://github.com/freechipsproject/chisel3/wiki/Cookbook#how-do-i-create-a-reg-of-type-vec
  val regOfVec = Reg(Vec(4, UInt(32.W)))
  regOfVec(0) := a // 单独使用某个寄存器
  y := regOfVec(0) + 1.U

  // 同上，定义时初始化，批量赋值
  val initRegOfVec = RegInit(VecInit(Seq.fill(4)(0.U(32.W))))
  initRegOfVec(0) := a // 单独使用某个寄存器
  y := initRegOfVec(0) + 1.U

  // 上述定义，在生成 verilog 时，未使用的寄存器会被优化掉

  // 具有 en 和同步 reset 的寄存器，本质就是加两个复用器
  val resetEnableReg = RegInit(0.U(4.W))
  when (en) {
    resetEnableReg := a
  }
  y := resetEnableReg
}

object RegModule extends App {
  getVerilog(new RegModule, args)
}
