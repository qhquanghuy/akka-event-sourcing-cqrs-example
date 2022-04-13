package io.qhquanghuy.btcbillionaire

import java.nio.file.Paths

import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration._

import akka.Done
import akka.actor.typed.ActorSystem
import akka.persistence.jdbc.testkit.scaladsl.SchemaUtils
import org.slf4j.LoggerFactory

object CreateTableTestUtils {
  def dropAndRecreateTables(system: ActorSystem[_]): Unit = {
    implicit val sys: ActorSystem[_] = system
    implicit val ec: ExecutionContext = system.executionContext

    Await.result(
      for {
        _ <- SchemaUtils.dropIfExists()
        _ <- SchemaUtils.createIfNotExists()
      } yield Done,
      30.seconds)
  }
}
