package testsoc

import chisel3.stage.{ChiselCli, ChiselGeneratorAnnotation, ChiselStage}
import firrtl.options.Shell
import firrtl.stage.FirrtlCli
import freechips.rocketchip.system._
import freechips.rocketchip.diplomacy.DisableMonitors

import org.chipsalliance.cde.config.Config
import freechips.rocketchip.rocket.{WithNBigCores}
import freechips.rocketchip.subsystem.{WithCoherentBusTopology}
import boom.v3.common.{WithNSmallBooms, BoomTileAttachParams}
import freechips.rocketchip.subsystem._
import freechips.rocketchip.devices.debug.{Debug, DebugModuleKey}
import freechips.rocketchip.devices.tilelink._
import firrtl.transforms.formal.DontAssertSubmoduleAssumptionsAnnotation

class RocketDefaultConfig extends Config(new WithNBigCores(1) ++ new WithCoherentBusTopology ++ new BaseConfig)
class BOOMDefaultConfig   extends Config(new WithNSmallBooms(1) ++ new WithCoherentBusTopology ++ new BaseConfig)

class SimBOOMConfig extends Config(
  new WithNSmallBooms(1).alter((site, _, up) => {
    case TilesLocated(InSubsystem) => up(TilesLocated(InSubsystem), site).map {
      case tp: BoomTileAttachParams => tp.copy(tileParams = tp.tileParams.copy(
        core = tp.tileParams.core.copy(
          // haveNemuTrap = true,
          // haveCease = false,
          nPMPs = 0,
          nBreakpoints = 0
        )
      ))
    }
  }) ++
  new WithCoherentBusTopology ++
  new BaseConfig().alter((site, _, up) => {
    case DebugModuleKey => None
    // case CLINTKey => None
    // case PLICKey => None
    case BootROMLocated(InSubsystem) => Some(BootROMParams(
      contentFileName = "./bootrom/bootrom.img",
      address = 0x10000000,
      hang = 0x10000000,
    ))
    case ExtMem => Some(MemoryPortParams(MasterPortParams(
      base = BigInt("80000000", 16),
      size = BigInt("80000000", 16),
      beatBytes = site(MemoryBusKey).beatBytes,
      idBits = 4), 1))
    case ControlBusKey => up(ControlBusKey, site).copy(
      errorDevice = None,
      zeroDevice = None,
    )
  })
)
class SimRocketConfig extends Config(
  new WithNBigCores(1).alter((site, _, up) => {
    case TilesLocated(InSubsystem) => up(TilesLocated(InSubsystem), site).map {
      case tp: RocketTileAttachParams => tp.copy(tileParams = tp.tileParams.copy(
        core = tp.tileParams.core.copy(
          // haveNemuTrap = true,
          haveCease = false,
          nPMPs = 0,
          nBreakpoints = 0,
          // chickenCSR = false,
        )
      ))
    }
  }) ++
  new WithCoherentBusTopology ++
  new BaseConfig().alter((site, _, up) => {
    case DebugModuleKey => None
    // case CLINTKey => None
    // case PLICKey => None
    case BootROMLocated(InSubsystem) => Some(BootROMParams(
      contentFileName = "./bootrom/bootrom.img",
      address = 0x10000000,
      hang = 0x10000000,
      // withDTB = false,
    ))
    case ExtMem => Some(MemoryPortParams(MasterPortParams(
      base = BigInt("80000000", 16),
      size = BigInt("80000000", 16),
      beatBytes = site(MemoryBusKey).beatBytes,
      idBits = 4), 1))
    case ControlBusKey => up(ControlBusKey, site).copy(
      errorDevice = None,
      zeroDevice = None,
    )
  })
)

class FuzzStage extends ChiselStage {
  override val shell: Shell = new Shell("rocket-chip")
    with ChiselCli
    with FirrtlCli
}

// freechips.rocketchip.system.DefaultConfig
object SimMain {
  def main(args: Array[String]): Unit = {
    (new FuzzStage).execute(args, Seq(
      DontAssertSubmoduleAssumptionsAnnotation,
      ChiselGeneratorAnnotation(() => {
        
        // freechips.rocketchip.diplomacy.DisableMonitors(p => new SimTop()(p))(new SimRocketConfig)
        freechips.rocketchip.diplomacy.DisableMonitors(p => new SimTop()(p))(new SimBOOMConfig)
        // freechips.rocketchip.diplomacy.DisableMonitors(p => new SimTop()(p))(new RocketDefaultConfig)
        // freechips.rocketchip.diplomacy.DisableMonitors(p => new TestHarness()(p))(new BOOMDefaultConfig)
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
