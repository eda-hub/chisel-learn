// See README.md for license details.

import chisel3._
import chisel3.stage.ChiselGeneratorAnnotation
import firrtl.{EmittedFirrtlCircuitAnnotation, EmittedFirrtlModuleAnnotation}
import layered.stage.ElkStage

package object chisel {

  def printHelp(): Unit = {
    (new chisel3.stage.ChiselStage).execute(Array("--help"), Seq.empty)
  }

  def genGraph(gen: => RawModule, args: Array[String] = Array.empty): Unit = {
    (new ElkStage).execute(args.union(Array("-td", "vout")).distinct,
      Seq(ChiselGeneratorAnnotation(() => gen))
    )
  }

  def getVerilog(gen: => RawModule, args: Array[String] = Array.empty): String = {
    (new chisel3.stage.ChiselStage).emitVerilog(gen, args.union(Array("-td", "vout")).distinct)
  }

  def getLowFirrtl(gen: => RawModule, args: Array[String] = Array.empty): String = {
    (new chisel3.stage.ChiselStage).execute(args.union(Array("-td", "vout", "-X", "low")).distinct,
      Seq(ChiselGeneratorAnnotation(() => gen)))
      .collect {
        case EmittedFirrtlCircuitAnnotation(a) => a
        case EmittedFirrtlModuleAnnotation(a) => a
      }.map(_.value)
      .mkString("")
  }
}
