// See README.md for license details.

package chisel.chiselbasic

import chisel3._
import chisel3.iotesters.PeekPokeTester

class TestModule extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(2.W))
    val b = Input(UInt(2.W))
    val out = Output(UInt(2.W))
  })
  io.out := io.a & io.b

  // https://github.com/freechipsproject/chisel-bootcamp/blob/master/2.1_first_module.ipynb
  // https://github.com/freechipsproject/chisel3/wiki/Printing-in-Chisel
  // 在测试的时候执行，仅可打印输入，打印输出需要在测试代码里通过 peek 获取
  printf("Print during simulation: Input is %d\n", io.a)
  // chisel printf has its own string interpolator too
  printf(p"Print during simulation: IO is $io\n")

  // 这里的 scala 自身的，没什么用，注意前缀区别
  println(s"Print during generation: Input is ${io.a}")
}

/**
 * 在第几个位置的期望     得到值     期望值   结果
 * [info] [0.014] EXPECT AT 2   io_out got 2 expected 1 FAIL
 * 共几个 expect 通过测试，测试周期包含启动(reset)时间
 * test TestModule Success: 1 tests passed in 7 cycles taking 0.032428 seconds
 * 运行了 几个 周期，不含启动(reset)时间，第一次失败 在第几个 step
 * [info] [0.016] RAN 2 CYCLES FAILED FIRST AT CYCLE 2
 *
 * 测试开始默认有一段启动(reset)时间
 * @param dut
 */
class Tester1(dut: TestModule) extends PeekPokeTester(dut) {
  poke(dut.io.a, 0.U)
  poke(dut.io.b, 1.U)
  step(1)
  // PeekPokeTester 重写了 Scala 自己的 println，导致默认的失效，需要通过包名使用 Predef.println
//  println("Result is: " + peek(dut.io.out).toString)
  expect(dut.io.out,0)
  poke(dut.io.a, 3.U)
  poke(dut.io.b, 2.U)
  // 在原有的 step 数基础上继续
  step(1)
//  println("Result is: " + peek(dut.io.out).toString)
  // error
  expect(dut.io.out,1)
}

class Tester2(dut: TestModule) extends PeekPokeTester(dut) {
  poke(dut.io.a, 0.U)
  poke(dut.io.b, 1.U)
  step(1)
  expect(dut.io.out,0)
}

// 通过简单的 main 方法 启动测试
// 因为 iotesters 要仿真 dut，所以会生成 low firrtl
// execute 只接收 MultiIOModule(包括 Module) 模块，不支持 RawModule
object Tester extends App {
//  chisel3.iotesters.Driver(() => new TestModule()) { c =>
//    new Tester1(c)
//    // RAN 1 CYCLES bug，不建议这样搞
//    new Tester2(c)
//  }

  iotesters.Driver.execute(Array(), () => new TestModule) {
    c => new Tester1(c)
  }
}
