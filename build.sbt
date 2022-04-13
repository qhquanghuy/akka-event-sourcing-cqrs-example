import Dependencies._

// Run in a separate JVM, to make sure sbt waits until all threads have
// finished before returning.
// If you want to keep the application running while executing other
// sbt tasks, consider https://github.com/spray/sbt-revolver/
fork := true

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "io.qhquanghuy",
      scalaVersion    := "2.13.8"
    )),
    name := "BTCBillionaire",
    libraryDependencies ++= akka.all,
    libraryDependencies ++= cats.all,
    libraryDependencies ++= macwire.all,
    libraryDependencies ++= logback.all,
    libraryDependencies ++= postgre.all
  )
