// See README.md for license details.

package chisel.chiselbasic

import chisel3.iotesters.ChiselFlatSpec
import chisel.chiselbasic.{TestModule, Tester1, Tester2}

/**
 * 必须放在 test 目录下，包名可以和待测试的类所在包同名，这样就不需要导入待测试类
 * sbt 下
 * testOnly chiselbasic.SpecTester 运行所有测试
 * testOnly chiselbasic.SpecTester -- -t "Basic test using Driver.execute should test2" 精确运行某个测试
 * testOnly chiselbasic.SpecTester -- -z "test2" 运行测试名称包含某字符串的测试
 */
class OldTester extends ChiselFlatSpec {

  // 出错直接报异常，sbt 下只报错
  // 多个测试的 dut(new TestModule) 不能拿出来
  // should 和 in 都是方法，should左侧 + "should" + should右侧 构成测试的名称
  "Basic test using Driver.execute" should "be used as an alternative way to run specification" in {
    chisel3.iotesters.Driver.execute(Array(), () => new TestModule) {
      c => new Tester1(c)
    } should be (true)
  }

  "Basic test using Driver.execute" should "test2" in {
    // --generate-vcd-output 或者 -tgvo 生成 vcd 文件
    chisel3.iotesters.Driver.execute(Array("-tgvo", "on"), () => new TestModule) {
      c => new Tester2(c)
    } should be (true)
  }
}
