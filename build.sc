import mill._
import mill.scalalib._
import mill.scalalib.publish._
import coursier.maven.MavenRepository

// 修正路径，使用反引号包裹 rocket-chip
import $file.generators.`rocket-chip`.build

// 定义项目的版本和依赖
object v {
  val scala = "2.13.10" // 根据需要调整 Scala 版本
  val chiselVersion = "3.6.0" // 根据需要调整 Chisel 版本
  val chiselIvy = ivy"edu.berkeley.cs::chisel3:$chiselVersion"
  val chiselPluginIvy = ivy"edu.berkeley.cs:::chisel3-plugin:$chiselVersion"
}

// 定义根项目模块
object rocketChipImport extends ScalaModule with PublishModule {
  def scalaVersion = T(v.scala)

  // 添加 rocket-chip 作为依赖
  def moduleDeps = Seq(
    generators.`rocket-chip`.build.rocketChip // 引用 rocket-chip 的模块
  )

  // 添加其他依赖项
  def ivyDeps = Agg(
    v.chiselIvy,
    v.chiselPluginIvy
  )

}