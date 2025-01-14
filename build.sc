import mill._
import scalalib._
import scalafmt._

val defaultScalaVersion = "2.13.15"
val pwd = os.pwd // 使用当前目录作为工作区根目录

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

// 定义本地模块
object macros extends ScalaModule {
  override def millSourcePath = pwd / "generators" / "rocket-chip" / "macros"

  override def scalaVersion = defaultScalaVersion

  override def ivyDeps = super.ivyDeps() ++ Agg(
    ivy"org.scala-lang:scala-reflect:${defaultScalaVersion}"
  )
}

object cde extends ScalaModule {
  override def millSourcePath = pwd / "generators" / "rocket-chip" / "cde"

  override def scalaVersion = defaultScalaVersion
}

object diplomacy extends ScalaModule {
  override def millSourcePath = pwd / "generators" / "rocket-chip" / "diplomacy"

  override def scalaVersion = defaultScalaVersion
}

object hardfloat extends ScalaModule {
  override def millSourcePath = pwd / "generators" / "rocket-chip" / "hardfloat"

  override def scalaVersion = defaultScalaVersion
}

// 定义 RocketChip 模块
object rocketchip extends ScalaModule with HasChisel {
  override def millSourcePath = pwd / "generators" / "rocket-chip"

  override def scalaVersion = defaultScalaVersion

  override def moduleDeps = super.moduleDeps ++ Seq(
    macros, cde, diplomacy, hardfloat
  )

  override def ivyDeps = super.ivyDeps() ++ Agg(
    defaultVersions("chiseltest")
  )
}

// 定义主项目模块
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
    override def scalaVersion = defaultScalaVersion

    override def moduleDeps = super.moduleDeps ++ Seq(
      rocketchip
    )

    override def ivyDeps = super.ivyDeps() ++ Agg(
      defaultVersions("chiseltest")
    )

    override def scalacOptions = super.scalacOptions() ++ Agg("-deprecation", "-feature")
  }
}