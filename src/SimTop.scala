// See LICENSE.SiFive for license details.
package testsoc

import chisel3._
import difftest.DifftestModule
import org.chipsalliance.cde.config._
import org.chipsalliance.diplomacy.lazymodule._

import freechips.rocketchip.devices.debug.Debug
import freechips.rocketchip.util.AsyncResetReg
import freechips.rocketchip.system.{ExampleRocketSystem, SimAXIMem}
import freechips.rocketchip.subsystem._
import freechips.rocketchip.devices.tilelink._
import freechips.rocketchip.util._
// package freechips.rocketchip.system
class ExampleSimSystem(implicit p: Parameters) extends RocketSubsystem
  with CanHaveMasterAXI4MemPort
{
  // Changed from ExampleRocketSystem
  // delete "CanHaveMasterAXI4MMIOPort", "HasAsyncExtInterrupts", "CanHaveSlaveAXI4Port"
  // optionally add ROM devices
  // Note that setting BootROMLocated will override the reset_vector for all tiles
  val bootROM  = p(BootROMLocated(location)).map { BootROM.attach(_, this, CBUS) }
  val maskROMs = p(MaskROMLocated(location)).map { MaskROM.attach(_, this, CBUS) }

  override lazy val module = new ExampleSimSystemImp(this)
}

class ExampleSimSystemImp[+L <: ExampleSimSystem](_outer: L) extends RocketSubsystemModuleImp(_outer) {
  _outer.clintTickOpt.foreach { _ := false.B } // 或者根据需求提供其他逻辑
}

class SimTop()(implicit p: Parameters) extends Module {
  // val io = IO(new Bundle {
  //   val success = Output(Bool())
  // })

  // val ldut = LazyModule(new ExampleRocketSystem)
  val ldut = LazyModule(new ExampleSimSystem)
  val dut = Module(ldut.module)

  val dut_reset = (reset.asBool | ldut.debug.map { debug => AsyncResetReg(debug.ndreset) }.getOrElse(false.B)).asBool

  SimAXIMem.connectMem(ldut)
  val success = WireInit(false.B)
  Debug.connectDebug(ldut.debug, ldut.resetctrl, ldut.psd, clock, reset.asBool, success)

  ldut.io_clocks.get.elements.values.foreach(_.clock := clock)
  // Allow the debug ndreset to reset the dut, but not until the initial reset has completed
  ldut.io_clocks.get.elements.values.foreach(_.reset := dut_reset)
  ldut.module.meip.foreach(_.foreach(_ := false.B))
  ldut.module.seip.foreach(_.foreach(_ := false.B))
  // dut.dontTouchPorts()
  // dut.tieOffInterrupts()
  // SimAXIMem.connectMMIO(ldut)
  // ldut.l2_frontend_bus_axi4.foreach( a => {
  //   a.ar.valid := false.B
  //   a.ar.bits := DontCare
  //   a.aw.valid := false.B
  //   a.aw.bits := DontCare
  //   a.w.valid := false.B
  //   a.w.bits := DontCare
  //   a.r.ready := false.B
  //   a.b.ready := false.B
  // })
  //ldut.l2_frontend_bus_axi4.foreach(_.tieoff)
  // Debug.connectDebug(ldut.debug, ldut.resetctrl, ldut.psd, clock, reset.asBool, io.success)
  // ldut.module.meip.foreach(_.foreach(_ := false.B))
  // ldut.module.seip.foreach(_.foreach(_ := false.B))
  // val difftest = DifftestModule.finish("rocket-chip")
  val difftest = DifftestModule.finish("riscv-boom")
}
