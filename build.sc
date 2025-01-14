import mill._
import scalalib._
import scalafmt._
import $file.generators.`rocket-chip`.common
import $file.generators.`rocket-chip`.dependencies.cde.common
import $file.generators.`rocket-chip`.dependencies.hardfloat.common

val defaultScalaVersion = "2.13.15"
val pwd = os.Path(sys.env("MILL_WORKSPACE_ROOT"))

def defaultVersions = Map(
  "chisel"        -> ivy"org.chipsalliance::chisel:6.6.0",
  "chisel-plugin" -> ivy"org.chipsalliance:::chisel-plugin:6.6.0",
  "chiseltest"    -> ivy"edu.berkeley.cs::chiseltest:6.0.0"
)

trait HasChisel extends SbtModule {
  def chiselModule: Option[ScalaModule] = None

  def chiselPluginJar: T[Option[PathRef]] = None

  def chiselIvy: Option[Dep] = Some(defaultVersions("chisel"))

  def chiselPluginIvy: Option[Dep] = Some(defaultVersions("chisel-plugin"))

  override def scalaVersion = defaultScalaVersion

  override def scalacOptions = super.scalacOptions() ++
    Agg("-language:reflectiveCalls", "-Ymacro-annotations", "-Ytasty-reader")

  override def ivyDeps = super.ivyDeps() ++ Agg(chiselIvy.get)

  override def scalacPluginIvyDeps = super.scalacPluginIvyDeps() ++ Agg(chiselPluginIvy.get)
}

val rocketChipPath = pwd / "generators" / "rocket-chip" / "common.sc"
object rocketchip extends $file.rocketChipPath.RocketChipModule with HasChisel {
  override def millSourcePath = pwd / "generators" / "rocket-chip"
  override def scalaVersion = defaultScalaVersion
}

object myproject extends ScalaModule with HasChisel {
  override def millSourcePath = pwd

  override def scalaVersion = defaultScalaVersion

  override def moduleDeps = super.moduleDeps ++ Seq(
    rocketchip
  )

  override def ivyDeps = super.ivyDeps() ++ Agg(
    defaultVersions("chiseltest")
  )

  override def scalacOptions = super.scalacOptions() ++ Agg("-deprecation", "-feature")

  object test extends ScalaModule with TestModule.ScalaTest {
    override def moduleDeps = super.moduleDeps ++ Seq(
      rocketchip
    )

    override def ivyDeps = super.ivyDeps() ++ Agg(
      defaultVersions("chiseltest")
    )

    override def scalacOptions = super.scalacOptions() ++ Agg("-deprecation", "-feature")
  }
}