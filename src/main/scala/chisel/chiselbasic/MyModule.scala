// See README.md for license details.

package chisel.chiselbasic

import chisel3._
import chisel._

/**
 * 模块类型
 * Module(实际指向 LegacyModule)，只能定义一个 IO 端口，带有隐式的 clock 和 reset 信号
 * RawModule 支持多个 IO 端口，不含 clock 和 reset 信号
 * MultiIOModule 支持多个 IO 端口，带有隐式的 clock 和 reset 信号
 * 仿真时 reset 信号默认开始为高电平，经过5个周期变为低电平
 *
 * 单独定义数据类型变量即表示常量信号组件，然而一般在电路中，我们需要定义特定类型的硬件组件，如 IO、Wire、Reg
 * = 用来定义硬件组件，包括常量信号
 * := 用于连接具有硬件组件属性的变量，如果所连接的变量具有方向，左边必须是输出
 * 所谓的定义硬件通常即为给某个数据类型如UInt，设置其硬件属性，如
 * val wire = Wire(UInt(4.W))
 * 这里 Wire 的返回类型（或变量 wire 的类型）同样为 UInt
 * 现在的 chisel 语法有些啰嗦
 */
class MyModule extends RawModule {
  val in = IO(Input(UInt(4.W)))
  val out = IO(Output(UInt(4.W)))
  val out2 = IO(Output(UInt(4.W)))

  // 没有使用的输出信号需要声明为 DontCare，否则报未初始化异常
  // 声明为 DontCare 后，生成的 verilog 将信号初始化为 0
  out2 := DontCare

  // firrtl 会优化掉没用的线，不会优化掉没用的端口
  val nouse = Wire(UInt(4.W))
  nouse := DontCare
  //  保留没用的线
  dontTouch(nouse)

  // Wire 不能在定义时连接到默认信号，使用 WireDefault
  val wire = Wire(UInt(4.W))
  wire := in
  out := wire
}

object MyModule extends App {
  getVerilog(new MyModule)
}