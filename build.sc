import mill._
import scalalib._
import $file.generators.`rocket-chip`.dependencies.hardfloat.{common => hardfloatCommon}
import $file.generators.`rocket-chip`.dependencies.cde.{common => cdeCommon}
import $file.generators.`rocket-chip`.dependencies.diplomacy.{common => diplomacyCommon}
import $file.generators.`rocket-chip`.{common => rocketChipCommon}

val chiselVersion = "3.6.1"
val defaultScalaVersion = "2.13.9"
val pwd = os.Path(sys.env("MILL_WORKSPACE_ROOT"))

object v {
  def chiselIvy: Option[Dep] = Some(ivy"edu.berkeley.cs::chisel3:${chiselVersion}")
  def chiselPluginIvy: Option[Dep] = Some(ivy"edu.berkeley.cs:::chisel3-plugin:${chiselVersion}")
}

trait HasThisChisel extends SbtModule {
  def chiselModule: Option[ScalaModule] = None
  def chiselPluginJar: T[Option[PathRef]] = None
  def chiselIvy: Option[Dep] = v.chiselIvy
  def chiselPluginIvy: Option[Dep] = v.chiselPluginIvy
  override def scalaVersion = defaultScalaVersion
  override def scalacOptions = super.scalacOptions() ++
    Agg("-language:reflectiveCalls", "-Ymacro-annotations", "-Ytasty-reader")
  override def ivyDeps = super.ivyDeps() ++ Agg(chiselIvy.get)
  override def scalacPluginIvyDeps = super.scalacPluginIvyDeps() ++ Agg(chiselPluginIvy.get)
}

object rocketchip extends RocketChip
trait RocketChip extends rocketChipCommon.RocketChipModule with HasThisChisel {
  override def scalaVersion: T[String] = T(defaultScalaVersion)
  override def millSourcePath = pwd / "generators" / "rocket-chip"
  def dependencyPath = pwd / "generators" / "rocket-chip" / "dependencies"
  def macrosModule = macros
  def hardfloatModule = hardfloat
  def cdeModule = cde
  def diplomacyModule = diplomacy
  def diplomacyIvy = None
  def mainargsIvy = ivy"com.lihaoyi::mainargs:0.5.4"
  def json4sJacksonIvy = ivy"org.json4s::json4s-jackson:4.0.6"
  override def moduleDeps = super.moduleDeps ++ Seq(difftest)
  object macros extends Macros
  trait Macros extends rocketChipCommon.MacrosModule with SbtModule {
    def scalaVersion: T[String] = T(defaultScalaVersion)
    def scalaReflectIvy = ivy"org.scala-lang:scala-reflect:${defaultScalaVersion}"
  }

  object hardfloat extends Hardfloat
  trait Hardfloat extends hardfloatCommon.HardfloatModule with HasThisChisel {
    override def scalaVersion: T[String] = T(defaultScalaVersion)
    override def millSourcePath = dependencyPath / "hardfloat" / "hardfloat"
  }

  object cde extends CDE
  trait CDE extends cdeCommon.CDEModule with ScalaModule {
    def scalaVersion: T[String] = T(defaultScalaVersion)
    override def millSourcePath = dependencyPath / "cde" / "cde"
  }

  object diplomacy extends Diplomacy
  trait Diplomacy extends diplomacyCommon.DiplomacyModule with ScalaModule {
    def scalaVersion: T[String] = T(defaultScalaVersion)
    override def millSourcePath = dependencyPath / "diplomacy" / "diplomacy"

    def chiselModule: Option[ScalaModule] = None
    def chiselPluginJar: T[Option[PathRef]] = None
    def chiselIvy: Option[Dep] = v.chiselIvy
    def chiselPluginIvy: Option[Dep] = v.chiselPluginIvy

    def cdeModule = cde
    def sourcecodeIvy = ivy"com.lihaoyi::sourcecode:0.3.1"
  }
}

trait testSoCModule extends ScalaModule {
  def rocketModule: ScalaModule
  override def moduleDeps = super.moduleDeps ++ Seq(
    rocketModule,
  )
}

object testsoc extends testSoC
trait testSoC extends testSoCModule with HasThisChisel {
  override def millSourcePath = pwd
  override def sources = Task.Sources(millSourcePath / "src")
  def rocketModule = rocketchip
}

trait Boom extends ScalaModule with HasThisChisel {
  override def millSourcePath = pwd / "generators" / "riscv-boom"
  override def scalaVersion = defaultScalaVersion
  override def scalacOptions = Seq(
    "-language:reflectiveCalls",
    "-deprecation",
    "-feature",
    "-Xcheckinit",
    "-P:chiselplugin:genBundleElements"
  )

  def rocketModule: ScalaModule = rocketchip

  override def moduleDeps = super.moduleDeps ++ Seq(rocketModule) ++ Seq(difftest)

  override def ivyDeps = Agg(
    ivy"edu.berkeley.cs::chisel3:$chiselVersion",
    ivy"ch.epfl.scala::bloop-config:2.0.3"
  )

  override def scalacPluginIvyDeps = Agg(
    ivy"edu.berkeley.cs:::chisel3-plugin:$chiselVersion",
  )
}

object boom extends Boom

trait Difftest extends ScalaModule with HasThisChisel{
  def scalaVersion = defaultScalaVersion
  def millSourcePath = pwd / "difftest"
  def chiselModule: Option[ScalaModule] = None
  def chiselPluginJar: T[Option[PathRef]] = None
  def chiselIvy: Option[Dep] = v.chiselIvy
  def chiselPluginIvy: Option[Dep] = v.chiselPluginIvy
}

object difftest extends Difftest