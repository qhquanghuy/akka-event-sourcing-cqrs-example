package io.qhquanghuy.btcbillionaire

import java.nio.file.Paths

import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration._

import akka.Done
import akka.actor.typed.ActorSystem
import akka.persistence.jdbc.testkit.scaladsl.SchemaUtils
import akka.projection.slick.SlickProjection

import org.slf4j.LoggerFactory

import slick.basic.DatabaseConfig
import slick.jdbc.PostgresProfile
import slick.driver.JdbcProfile
import scala.reflect.ClassTag
import scala.io.Source

object CreateTableTestUtils {
  def dropAndRecreateTables[P <: JdbcProfile : ClassTag](system: ActorSystem[_], dbConfig: DatabaseConfig[P]): Unit = {
    implicit val sys: ActorSystem[_] = system
    implicit val ec: ExecutionContext = system.executionContext
    Await.result(
      for {
        _ <- SchemaUtils.dropIfExists()
        _ <- SchemaUtils.createIfNotExists()
        _ <- SlickProjection.createTablesIfNotExists(dbConfig)
        _ <- dropAndRecreateDonationTable(dbConfig)
      } yield Done,
      30.seconds)
  }

  def dropAndRecreateDonationTable[P <: JdbcProfile](dbConfig: DatabaseConfig[P]) = {
    import dbConfig.profile.api._
    val sqlScript = Source.fromFile("ddl-scripts/read-db/V2__donation.sql")
      .getLines()
      .mkString

    val action = sqlu"#$sqlScript".asTry
    dbConfig.db.run(action)
  }
}
