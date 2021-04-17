// See README.md for license details.

package chisel.chiselbasic

import chisel3._
import chisel._
import chisel3.util._

class SwitchModule extends RawModule {
  val io = IO(new Bundle {
    val a = Input(UInt(16.W))
    val b = Input(UInt(16.W))
    val fn = Input(UInt(2.W))
    val y = Output(UInt(16.W))
  })
  // 万一不匹配任何一个
  // 当前 chisel := 是有顺序的，默认值要放在 switch 前面
  // when 可以通过 otherwise 指定默认值
  io.y := 0.U

  // switch is 是语句，不是表达式
  switch(io.fn) {
    is(0.U) { io.y := io.a + io.b }
    is(1.U) { io.y := io.a - io.b }
    is(2.U) { io.y := io.a | io.b }
    is(3.U) { io.y := io.a & io.b }
  }

  // when 也是语句，条件只能是 Bool 类型
//  when (io.fn === 0.U) {
//    io.y := io.a + io.b
//  } .elsewhen (io.fn === 1.U) {
//    io.y := io.a - io.b
//  } .otherwise {
//    io.y := io.a | io.b
//  }

}

object SwitchModule extends App {
  getVerilog(new SwitchModule, args)
}