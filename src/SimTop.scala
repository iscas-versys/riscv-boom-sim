package testsoc

import chisel3.stage.{ChiselCli, ChiselGeneratorAnnotation, ChiselStage}
import firrtl.options.Shell
import firrtl.stage.FirrtlCli
import freechips.rocketchip.system._
import freechips.rocketchip.diplomacy.DisableMonitors


class FuzzStage extends ChiselStage {
  override val shell: Shell = new Shell("rocket-chip")
    with ChiselCli
    with FirrtlCli
}

// freechips.rocketchip.system.DefaultConfig
object SimMain {
  def main(args: Array[String]): Unit = {
    (new FuzzStage).execute(args, Seq(
      ChiselGeneratorAnnotation(() => {
        freechips.rocketchip.diplomacy.DisableMonitors(p => new TestHarness()(p))(new DefaultConfig)
      })
    ))
  }
}

// package testsoc

// import chisel3._
// import chisel3.stage.{ChiselGeneratorAnnotation, ChiselStage}
// import firrtl.options.Shell
// import firrtl.stage.FirrtlCli

// class SimpleCircuit extends Module {
//   val io = IO(new Bundle {
//     val in  = Input(Bool())
//     val out = Output(Bool())
//   })
//   io.out := io.in
// }

// object SimMain {
//   def main(args: Array[String]): Unit = {
//     println("Hello World!!!")
//     (new ChiselStage).execute(args, Seq(
//       ChiselGeneratorAnnotation(() => new SimpleCircuit)
//     ))
//   }
// }
