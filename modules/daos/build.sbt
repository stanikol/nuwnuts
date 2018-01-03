name := """daos"""

version := "5.0.0"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
    Dependencies.quill
    , Dependencies.postgresql
    , Dependencies.logback
    , Dependencies.scalatags
    , jdbc
    , ehcache
)

libraryDependencies ++= Dependencies.cats
//