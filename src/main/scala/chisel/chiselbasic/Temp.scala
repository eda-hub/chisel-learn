// See README.md for license details.

package chisel.chiselbasic

import chisel3._
import chisel._
import chisel3.util.Cat

class Temp extends RawModule {
  val a = IO(Input(UInt(4.W)))
  val b = IO(Input(Bool()))
  val c = IO(Input(Bool()))
  val y = IO(Output(UInt()))

//  y := (!a && !b && c) || (!a && b && c) || (a && !b && c) || (a && b && !c) || (a && b && c)
  y := Cat(a,b,c)
}

object Temp extends App {
  getVerilog(new Temp)
}