name := """admin"""

version := "5.0.0"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
    Dependencies.quill
    , Dependencies.postgresql
    , Dependencies.logback
    , Dependencies.scalatags
    , Dependencies.playBootstrap
    , Dependencies.bootstrap
    , jdbc
    , ehcache ,
      "org.webjars" %% "webjars-play" % "2.6.1",
//      "org.webjars" % "bootstrap" % "3.3.7-1" exclude("org.webjars", "jquery"),
      "org.webjars" % "jquery" % "3.2.1"
)

libraryDependencies ++= Dependencies.cats
//