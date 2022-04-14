import sbt._
object Dependencies {
  object akka {
    lazy val httpVersion = "10.2.9"
    lazy val version = "2.6.19"
    lazy val managementVersion = "1.1.3"
    lazy val persistenceJdbcVersion = "5.0.4"
    lazy val projectionVersion = "1.2.3"


    lazy val http = "com.typesafe.akka" %% "akka-http" % httpVersion
    lazy val httpSpray = "com.typesafe.akka" %% "akka-http-spray-json" % httpVersion
    lazy val actor = "com.typesafe.akka" %% "akka-actor-typed" % version
    lazy val stream = "com.typesafe.akka" %% "akka-stream" % version
    lazy val cluster = "com.typesafe.akka" %% "akka-cluster-typed" % version
    lazy val clusterSharding = "com.typesafe.akka" %% "akka-cluster-sharding-typed" % version
    lazy val management = "com.lightbend.akka.management" %% "akka-management" % managementVersion
    lazy val managementClusterHttp = "com.lightbend.akka.management" %% "akka-management-cluster-http" % managementVersion
    lazy val managementClusterBootstrap = "com.lightbend.akka.management" %% "akka-management-cluster-bootstrap" % managementVersion
    lazy val discovery = "com.typesafe.akka" %% "akka-discovery" % version
    lazy val persistence = "com.typesafe.akka" %% "akka-persistence-typed" % version
    lazy val serialization = "com.typesafe.akka" %% "akka-serialization-jackson" % version
    lazy val persistenceJdbc = "com.lightbend.akka" %% "akka-persistence-jdbc" % persistenceJdbcVersion
    lazy val persistenceQuery = "com.typesafe.akka" %% "akka-persistence-query" % version
    lazy val projectionEventsourced = "com.lightbend.akka" %% "akka-projection-eventsourced" % projectionVersion
    lazy val projectionSlick = "com.lightbend.akka" %% "akka-projection-slick" % projectionVersion



    def all = Seq(
      http,
      httpSpray,
      actor,
      stream,
      cluster,
      clusterSharding,
      management,
      managementClusterHttp,
      managementClusterBootstrap,
      discovery,
      persistence,
      serialization,
      persistenceJdbc,
      persistenceQuery,
      projectionEventsourced,
      projectionSlick
    )

  }


  object cats {
    lazy val version = "2.3.0"
    lazy val core = "org.typelevel" %% "cats-core" % version

    def all = Seq(core)
  }

  object macwire {
    lazy val version = "2.5.6"
    lazy val macros = "com.softwaremill.macwire" %% "macros" % version

    def all = Seq(macros)
  }
  object slick {
    lazy val version = "3.3.3"
    lazy val slick = "com.typesafe.slick" %% "slick" % version
    lazy val slf4j = "org.slf4j" % "slf4j-nop" % "1.6.4"
    lazy val hikaricp = "com.typesafe.slick" %% "slick-hikaricp" % version

    def all = Seq(slick, slf4j, hikaricp)
  }

  object logback {
    lazy val classic = "ch.qos.logback" % "logback-classic" % "1.2.3"
    def all = Seq(classic)
  }

  object postgres {
    lazy val postgresql = "org.postgresql" % "postgresql" % "42.2.18"
    def all = Seq(postgresql)
  }

  object libtest {
    lazy val scalatest = "org.scalatest" %% "scalatest" % "3.1.4" % Test

    lazy val akkaHttp = "com.typesafe.akka" %% "akka-http-testkit" % akka.httpVersion % Test
    lazy val akkaActor = "com.typesafe.akka" %% "akka-actor-testkit-typed" % akka.version % Test
    lazy val akkaPersistence = "com.typesafe.akka" %% "akka-persistence-testkit" % akka.version % Test
    lazy val h2 = "com.h2database" % "h2" % "1.4.192" % Test

    def all = Seq(akkaHttp, akkaActor, akkaPersistence, scalatest, h2)
  }
}