// See README.md for license details.

package chisel.chiseladvance

import chisel3._
import chisel3.util._
import chisel._

class ParamFunction  extends RawModule {

  val sel = IO(Input(Bool()))
  val a = IO(Input(UInt(4.W)))
  val b = IO(Input(UInt(4.W)))
  val y = IO(Output(UInt()))

  y := myMux(sel,a,b)

  def myMux[T <: Data](sel:Bool, tPath : T, fPath: T): T = {
//    var ret = fPath
//    when(sel) {
//      ret = tPath
//    }
    // 以上代码可以编译通过，但 ret 始终连接到 tPath

    // 必须使用硬件的方式
//    val ret = WireDefault(fPath)  // WireInit 等于 WireDefault

    // Wire 只能接收 Chisel 原始类型作为参数，而不能是硬件对象（具有硬件类型的 chisel 类型）
    val ret = Wire(chiselTypeOf(fPath)) // 等价于 fPath.cloneType，例化？代码不能使用 Wire(T)
    ret := fPath

    when(sel) {
      ret := tPath
    }
    ret
  }
}

object ParamFunction extends App {
  getVerilog(new ParamFunction, args)
}