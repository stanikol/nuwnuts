name := """dao"""

version := "5.0.0"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "io.getquill" %% "quill-async-postgres" % "2.3.1",
  jdbc,
//  cache,
//  ws,
//  evolutions,
  "com.mohiva" %% "play-silhouette-password-bcrypt" % "5.0.0",
  "com.mohiva" %% "play-silhouette-persistence" % "5.0.0",
  "org.postgresql" % "postgresql" % "9.4.1208"
//  "io.getquill" %% "quill-async-postgres" % "1.1.0"
//  "org.jsoup" % "jsoup" % "1.10.3"
)

//libraryDependencies += "org.jsoup" % "jsoup" % "1.10.3"