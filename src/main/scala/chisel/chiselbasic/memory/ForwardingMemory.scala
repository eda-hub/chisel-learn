// See README.md for license details.

package chisel.chiselbasic.memory

import chisel3._
import chisel._

// read-during-write 同时读写同一地址
// 该逻辑需要自己实现 还是由 memory compiler 提供？
class ForwardingMemory extends MultiIOModule {
  val io = IO(new Bundle {
    val rdAddr = Input(UInt(10.W))
    val rdData = Output(UInt(8.W))

    val wrEna = Input(Bool())
    val wrData = Input(UInt(8.W))
    val wrAddr = Input(UInt(10.W))
  })
  val mem = SyncReadMem(1024, UInt(8.W))

  // 因为 memory 是 同步读写的，这里需要加同步寄存器，如果不加一个寄存器则
  // 当地址由不同变为相同时，输出立即生效，此时相当于异步输出，影响应该不大
  // 当地址由相同变为不同时，输出立即生效且为 memory 的输出，但此时 memory 输出为上一周期的输出，经过一定延时后又变为当前地址的输出
  val wrDataReg = RegNext(io.wrData)
  val doForwardReg = RegNext(io.wrAddr === io.rdAddr && io.wrEna)

  val memData = mem.read(io.rdAddr)
  when(io.wrEna) {
    mem.write(io.wrAddr , io.wrData)
  }
  io.rdData := Mux(doForwardReg , wrDataReg , memData)
}

object ForwardingMemory extends App {
  getVerilog(new ForwardingMemory, args)
}