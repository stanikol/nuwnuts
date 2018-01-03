import sbt._

object Dependencies {

  val cats = Seq(
    "org.typelevel" %% "cats-core" % "1.0.1"
  )

  val scalatags = "com.lihaoyi" %% "scalatags" % "0.6.7"

  val postgresql = "org.postgresql" % "postgresql" % "9.4.1208"

  val quill = "io.getquill" %% "quill-async-postgres" % "2.3.1"

  val logback = "ch.qos.logback" % "logback-classic" % "1.2.3" % Test

//  val playBootstrap = "com.adrianhurt" %% "play-bootstrap" % "1.2-P26-B4"
  val playBootstrap = "com.adrianhurt" %% "play-bootstrap" % "1.2-P26-B3"

//  val bootstrap = "org.webjars" % "bootstrap" % "4.0.0-beta.3" exclude("org.webjars", "jquery")//"org.webjars" % "bootstrap" % "3.3.7-1" exclude("org.webjars", "jquery"),
  val bootstrap = "org.webjars" % "bootstrap" % "3.3.7-1" exclude("org.webjars", "jquery")

}
