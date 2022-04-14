package io.qhquanghuy.btcbillionaire.adapter.database

import java.time.Instant

import scala.reflect.ClassTag
import scala.concurrent.ExecutionContext

import slick.basic.DatabaseConfig
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.PostgresProfile



final case class DonationRow(id: Option[Int], datetime: Instant, amount: BigDecimal)

final class DonationDAO(val config: DatabaseConfig[PostgresProfile]) {
  class DonationTable(tag: Tag) extends Table[DonationRow](tag, "donation") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def datetime = column[Instant]("datetime")

    def amount = column[BigDecimal]("amount")
    def * = (id.?, datetime, amount).mapTo[DonationRow]
  }

  val table = TableQuery[DonationTable]

}
