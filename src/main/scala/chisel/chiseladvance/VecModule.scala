// See README.md for license details.

package chisel.chiseladvance

import chisel3._
import chisel3.util._
import chisel._
import chisel3.iotesters._

import scala.io.Source

/**
 * Vec 被翻译为级联复用器，综合器会转为 ROM？
 */
class VecModule extends Module {
  val io = IO(new Bundle {
    val address = Input(UInt(8.W))
    val data = Output(UInt(8.W))
  })
  val array = new Array[Int](4)
  var idx = 0
  // read the data into a Scala array
  val source = Source.fromResource("data.txt")
  for (line <- source.getLines()) {
    array(idx) = line.toInt
    idx += 1
  }
  println(array.mkString(" "))
  // convert the Scala integer array
  // into a vector of Chisel UInt
  val table = VecInit(array.map(_.U(8.W)))

  // use the table
  io.data := table(io.address)
}

class TableTester(dut: VecModule) extends PeekPokeTester(dut) {
  poke(dut.io.address,0)
  expect(dut.io.data,4)

  poke(dut.io.address,1)
  expect(dut.io.data,255)

  poke(dut.io.address,2)
  expect(dut.io.data,0)

  poke(dut.io.address,3)
  expect(dut.io.data,1)
}

object VecModule extends App {
 iotesters.Driver.execute(Array(),() => new VecModule) {
   c => new TableTester(c)
 }
}

class BcdTable extends Module {
  val io = IO(new Bundle {
    val address = Input(UInt(8.W))
    val data = Output(UInt(8.W))
  })
  val table = VecInit(1.U, 2.U, 4.U, 8.U)
//  val table = Wire(Vec(100, UInt(8.W)))
//  // Convert binary to BCD
//  for (i <- 0 until 100) {
//    table(i) := (((i/10) <<4) + i%10).U
//  }

  io.data := table(io.address)
}

object BcdTable extends App {
  getVerilog(new BcdTable, args)
}