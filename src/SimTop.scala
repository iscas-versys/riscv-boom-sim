package testsoc

import chisel3._
import chisel3.stage.{ChiselGeneratorAnnotation, ChiselStage}
import firrtl.options.Shell
import firrtl.stage.FirrtlCli

class SimpleCircuit extends Module {
  val io = IO(new Bundle {
    val in  = Input(Bool())
    val out = Output(Bool())
  })
  io.out := io.in
}

object SimMain {
  def main(args: Array[String]): Unit = {
    println("Hello World!!!")
    (new ChiselStage).execute(args, Seq(
      ChiselGeneratorAnnotation(() => new SimpleCircuit)
    ))
  }
}