import play.sbt.routes.RoutesKeys

name := "tpscalavillage"

version := "1.0"

lazy val `tpscalavillage` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(jdbc, ehcache, ws, specs2 % Test, guice, "org.reactivemongo" %% "play2-reactivemongo" % "0.20.3-play28", "com.typesafe.play" %% "play-iteratees-reactive-streams" % "2.6.1", "org.apache.kafka" % "kafka-clients" % "2.4.1", "com.typesafe.play" %% "play-json-joda" % "2.8.1", "net.liftweb" %% "lift-json" % "3.4.1")
routesGenerator := InjectedRoutesGenerator
RoutesKeys.routesImport += "play.modules.reactivemongo.PathBindables._"
unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")