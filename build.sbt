lazy val akkaHttpVersion = "10.2.9"
lazy val akkaVersion = "2.6.19"
lazy val akkaManagementVersion = "1.1.3"

// Run in a separate JVM, to make sure sbt waits until all threads have
// finished before returning.
// If you want to keep the application running while executing other
// sbt tasks, consider https://github.com/spray/sbt-revolver/
fork := true

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "io.qhquanghuy",
      scalaVersion    := "2.13.8"
    )),
    name := "BTCBillionaire",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"                    % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json"         % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor-typed"             % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"                  % akkaVersion,
      "ch.qos.logback"    % "logback-classic"               % "1.2.3",

      "com.typesafe.akka" %% "akka-cluster-typed"           % akkaVersion,
      "com.typesafe.akka" %% "akka-cluster-sharding-typed"  % akkaVersion,
      "com.lightbend.akka.management" %% "akka-management" % akkaManagementVersion,
      "com.lightbend.akka.management" %% "akka-management-cluster-http" % akkaManagementVersion,
      "com.lightbend.akka.management" %% "akka-management-cluster-bootstrap" % akkaManagementVersion,
      "com.typesafe.akka" %% "akka-discovery" % akkaVersion,


      "com.softwaremill.macwire" %% "macros" % "2.5.6",


      "com.typesafe.akka" %% "akka-http-testkit"            % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed"     % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"                    % "3.1.4"         % Test
    )
  )
