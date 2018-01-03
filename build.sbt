import com.typesafe.sbt.SbtScalariform._

import scalariform.formatter.preferences._

name := "newnuts"

version := "5.0.0"

scalaVersion := "2.12.3"

resolvers += Resolver.jcenterRepo

resolvers += "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"


lazy val usr_storage = (project in file("modules/usr_storage"))

lazy val usr = (project in file("modules/usr"))
		.enablePlugins(PlayScala)
      .aggregate(usr_storage).dependsOn(usr_storage)

lazy val daos = (project in file("modules/daos"))

lazy val img = (project in file("modules/img"))
		.enablePlugins(PlayScala)
    .aggregate(daos).dependsOn(daos)

lazy val admin = (project in file("modules/admin"))
  .enablePlugins(PlayScala)
  .aggregate(usr).dependsOn(usr)
  .aggregate(daos).dependsOn(daos)
  .aggregate(img).dependsOn(img)




lazy val root = (project in file("."))
		.enablePlugins(PlayScala)
		  .aggregate(usr).dependsOn(usr)
		    .aggregate(img).dependsOn(img)
		      .aggregate(admin).dependsOn(admin)

