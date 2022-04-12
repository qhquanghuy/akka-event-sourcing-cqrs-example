import sbt._
object Dependencies {
  object akka {
    lazy val httpVersion = "10.2.9"
    lazy val version = "2.6.19"
    lazy val managementVersion = "1.1.3"


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
      discovery
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

  object refined {
    lazy val spray = "io.github.typeness" %% "spray-json-refined" % "0.1.0"

    def all = Seq(spray)
  }

  object test {
    lazy val scalatest = "org.scalatest" %% "scalatest" % "3.1.4" % Test

    lazy val httpTest = "com.typesafe.akka" %% "akka-http-testkit" % akka.httpVersion % Test
    lazy val actorTest = "com.typesafe.akka" %% "akka-actor-testkit-typed" % akka.version % Test

    def all = Seq(scalatest, httpTest, actorTest)
  }
}