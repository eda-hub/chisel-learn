// See README.md for license details.

package chisel.chiseladvance

import chisel3._
import chisel3.util._
import chisel._

class ParamAdder(n: Int) extends RawModule {
  val io = IO(new Bundle{
    val a = Input(UInt(n.W))
    val b = Input(UInt(n.W))
    val c = Output(UInt(n.W))
  })
  io.c := io.a + io.b
}

object ParamAdder extends App {
  getVerilog(new ParamAdder(4), args)
}