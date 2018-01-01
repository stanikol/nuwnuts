import com.typesafe.sbt.SbtScalariform._

import scalariform.formatter.preferences._

name := "newnuts"

version := "5.0.0"

scalaVersion := "2.12.3"

resolvers += Resolver.jcenterRepo

resolvers += "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"


lazy val daos = (project in file("modules/daos"))

lazy val usr = (project in file("modules/usr"))
		.enablePlugins(PlayScala)
      .aggregate(daos).dependsOn(daos)



lazy val root = (project in file("."))
		.enablePlugins(PlayScala)
		.aggregate(usr).dependsOn(usr)
		
