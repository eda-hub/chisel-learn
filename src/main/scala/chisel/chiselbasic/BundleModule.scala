// See README.md for license details.

package chisel.chiselbasic

import chisel3._
import chisel._

// 可以根据需要将多路信号组成一组，以下用于 Wire
//class MyFloat extends Bundle {
//  val sign        = Bool()
//  val exponent    = UInt(8.W)
//  val significand = UInt(23.W)
//}

// 如果将 Bundle 类型用于定义 IO，则必须声明信号方向
class MyBundle extends Bundle {
  val a = Input(Bool())
  val b = Output(Bool())
}

class MyModule1 extends RawModule {
  val in = IO(Input(Bool()))
  val io = IO(new MyBundle)
  io.b := io.a + in
}

// 使用 Flipped 可以将 MyBundle 内所有信号方向取反
class MyModule2 extends Module {
  val io = IO(Flipped(new MyBundle))

//  val reg = RegInit(0.U)
//  reg := io.b
//  io.a := reg
  // 或者
  val reg = RegNext(io.b,0.U(8.W))
  io.a := reg
}

class BundleModule extends Module {

//  val floatConst = Wire(new MyFloat)
//  floatConst.sign := true.B
//  floatConst.exponent := 10.U
//  floatConst.significand := 128.U

  val io = IO(new Bundle{
    val hi = new MyBundle
    val out: Bool = Output(Bool())
  })
  io := DontCare

  val f1 = Module(new MyModule1)
  val f2 = Module(new MyModule2)

  f1.in := DontCare
  f2.io := DontCare

  // 批量连接符号 <> 将Bundle内的同名的 IO 信号连接起来，父子模块同名同向，同级模块同名异向
  // 父子模块间批量连接使用相同的 Bundle 定义
//  f1.io <> io.hi

  // 同级模块间批量连接使用 Flipped 后的 Bundle 接口
  f1.in := io.hi.a
  f1.io <> f2.io
  io.hi.b <> f2.io.a
}

object BundleModule extends App {
//  getLowFirrtl(new BundleModule)
  genGraph(new BundleModule)
}