import mill._
import scalalib._
import scalafmt._
// 默认 Scala 版本
val defaultScalaVersion = "2.13.15"
// 当前工作目录
val pwd = os.pwd
// 默认依赖版本（如果需要远程依赖）
def defaultVersions = Map(
  "chiseltest" -> ivy"edu.berkeley.cs::chiseltest:6.0.0"
)

// HasChisel Trait，用于定义 Chisel 相关的配置
trait HasChisel extends SbtModule {
  def chiselModule: Option[ScalaModule] = Some(chisel) // 使用本地 chisel 模块
  def chiselPluginJar: T[Option[PathRef]] = None
  def chiselIvy: Option[Dep] = None // 不再使用远程依赖
  def chiselPluginIvy: Option[Dep] = None // 不再使用远程依赖
  override def scalaVersion = defaultScalaVersion
  override def scalacOptions = super.scalacOptions() ++
    Agg("-language:reflectiveCalls", "-Ymacro-annotations", "-Ytasty-reader")
  override def ivyDeps = super.ivyDeps()
  override def scalacPluginIvyDeps = super.scalacPluginIvyDeps()
}

// Macros 模块
object macros extends ScalaModule {
  override def millSourcePath = pwd / "generators" / "rocket-chip" / "macros"
  override def scalaVersion = defaultScalaVersion
  override def ivyDeps = super.ivyDeps() ++ Agg(
    ivy"org.scala-lang:scala-reflect:${defaultScalaVersion}"
  )
}

// CDE 模块（本地依赖）
object cde extends ScalaModule {
  override def millSourcePath = pwd / "generators" / "rocket-chip" / "dependencies" / "cde"
  override def scalaVersion = defaultScalaVersion
}

// Diplomacy 模块（本地依赖）
object diplomacy extends ScalaModule {
  override def millSourcePath = pwd / "generators" / "rocket-chip" / "dependencies" / "diplomacy"
  override def scalaVersion = defaultScalaVersion
}

// Hardfloat 模块（本地依赖）
object hardfloat extends ScalaModule {
  override def millSourcePath = pwd / "generators" / "rocket-chip" / "dependencies" / "hardfloat"
  override def scalaVersion = defaultScalaVersion
}

// Chisel 模块（本地依赖）
object chisel extends ScalaModule {
  override def millSourcePath = pwd / "generators" / "rocket-chip" / "dependencies" / "chisel"
  override def scalaVersion = defaultScalaVersion
}

// RocketChip 模块
object rocketchip extends ScalaModule with HasChisel {
  override def millSourcePath = pwd / "generators" / "rocket-chip"
  override def scalaVersion = defaultScalaVersion
  override def moduleDeps = super.moduleDeps ++ Seq(
    macros, cde, diplomacy, hardfloat, chisel // 添加所有本地依赖模块
  )
  override def ivyDeps = super.ivyDeps() ++ Agg(
    defaultVersions("chiseltest") // 添加 chiseltest 依赖
  )
}

// 主项目模块
object myproject extends ScalaModule with HasChisel {
  override def millSourcePath = pwd

  override def scalaVersion = defaultScalaVersion
  override def moduleDeps = super.moduleDeps ++ Seq(
    rocketchip // 依赖 RocketChip 模块
  )
  override def ivyDeps = super.ivyDeps() ++ Agg(
    defaultVersions("chiseltest") // 添加 chiseltest 依赖
  )
  override def scalacOptions = super.scalacOptions() ++ Agg("-deprecation", "-feature")
  // 测试模块
  object test extends ScalaModule with TestModule.ScalaTest {
    override def scalaVersion = defaultScalaVersion
    override def moduleDeps = super.moduleDeps ++ Seq(
      rocketchip // 依赖 RocketChip 模块
    )
    override def ivyDeps = super.ivyDeps() ++ Agg(
      defaultVersions("chiseltest") // 添加 chiseltest 依赖
    )
    override def scalacOptions = super.scalacOptions() ++ Agg("-deprecation", "-feature")
  }
}