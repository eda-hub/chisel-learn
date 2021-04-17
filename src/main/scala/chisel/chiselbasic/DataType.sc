// See README.md for license details.

import chisel3._
import chisel3.experimental.Analog

/**
 * 数据类型是对流经 chisel 硬件电路组件中信号的抽象
 * 大多数情况下，其数据宽度会自动推断为能够包含当前数的最小位宽
 * 如果指定宽度大于所需位宽，对于 UInt 自动进行零扩展，对于 SInt 自动进行符号扩展
 * 以下转换用法一般仅在测试时，poke或peek数据时使用，或者用于电路中表示常量信号，即电源或地，如
 * val const = "b1111".U
 * out := const
 * 对应的 verilog
 * assign out = 4'hf;
 * 除了常量信号，通常我们需要定义特定类型的硬件，如 IO、Wire、Reg
 */
1.U       // scala Int 类型转 UInt
"ha".U    // 十六进制表示的数字 10 字符串转 UInt，注意：scala自身的十六进制数不需要引号，且以0x开头
"o12".U   // 八进制表示的数字 10 字符串转 UInt，注意：scala自身已经没有八进制字面量
"b1010".U // 二进制表示的数字 10 字符串转 UInt，注意：scala目前从未支持过二进制字面量
// 以上除第一个为1位外，其它三个的宽度自动推断为4位

5.S    // scala Int 类型转 SInt，宽度4位
5.U    // scala Int 类型转 UInt，宽度3位

8.U   // scala Int 类型转 UInt，宽度4位
-8.S   // 负数，scala Int 类型转 SInt，宽度4位，补码表示，包括符号位
8.U(5.W) // 按需要指定宽度，宽度由 scala 的 Int 类型转换为chisel Width 类型
-152.S(32.W) // 负数，scala Int 类型转 SInt，宽度32位

true.B // scala Boolean 类型转 Bool
false.B

"h_dead_beef".U   // 字符中中下划线仅方便阅读，没有其它作用

Analog(1.W) // 用于表示模拟信号、三态（inout）端口、电源线
