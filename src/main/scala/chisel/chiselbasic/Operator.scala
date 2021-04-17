// See README.md for license details.

package chisel.chiselbasic

import chisel3._
import chisel._
import chisel3.util.Cat

class Operator extends RawModule{
  val in1 = IO(Input(UInt(4.W)))
  val in2 = IO(Input(UInt(4.W)))
  val out = IO(Output(UInt(4.W)))
  val out2 = IO(Output(UInt())) // 由下面的运算自动推断为5位宽度

  // 针对硬件组件（信号）的运算符都对应着实际电路，在综合时自动转换为对应电路结构
  // 不同类型信号一般不能直接运算，可以使用 as* 转换为同类型
  // https://github.com/freechipsproject/chisel3/wiki/Builtin-Operators
  // 相等和不等为区别于 scala 自己的符号，使用 === 和 =/=
  // 加法和减法有多个运算符，其中带 % 号和不带一样，表示结果没有位扩展，带 & 号表示结果带位扩展
  // 逻辑运算符 &&、||、! 仅用于 Bool 类型，位运算 &、|、~ 可用于 SInt, UInt, Bool
  // 建议参考 firrtl 手册 元操作 类型了解
//  out := in1 + in2
  out := in1 +% in2 // out 被多次复制，最后一次生效
  out2 := in1 +& in2  // 结果为 5 位

//  out := in1 / in2
//  out := clb(in1,in2)

  // 位提取，从高位到低位，索引从0开始，和 verilog 一致
//  out := in1(3,2)

  // Cat 工具可以合并多个信号，从 msb 到 lsb
//  y := Cat(a,b,c)
  // 可以将可复用的电路组件定义为函数，返回类型可以省略
  def clb(a: UInt, b: UInt): UInt = a & b
}

object Operator extends App {
  getVerilog(new Operator)
}
