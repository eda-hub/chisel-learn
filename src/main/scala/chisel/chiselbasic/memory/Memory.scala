// See README.md for license details.

package chisel.chiselbasic.memory

import chisel3._
import chisel._

class Memory extends MultiIOModule {
  val io = IO(new Bundle {
    val rdAddr = Input(UInt(10.W))
    val rdData = Output(UInt(8.W))
    
    val wrEna = Input(Bool())
    val wrData = Input(UInt(8.W))
    val wrAddr = Input(UInt(10.W))
  })
  val mem = SyncReadMem(1024, UInt(8.W))
  io.rdData := mem.read(io.rdAddr)
  when(io.wrEna) {
    mem.write(io.wrAddr , io.wrData)
  }
}

object Memory extends App {
  getVerilog(new Memory, args)
}
