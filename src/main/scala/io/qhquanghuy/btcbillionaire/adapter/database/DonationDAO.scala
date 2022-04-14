package io.qhquanghuy.btcbillionaire.adapter.database

import java.time.Instant
import java.time.ZonedDateTime
import scala.concurrent.Future

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
import java.time.ZoneId




final case class DonationRow(id: Option[Int], datetime: Long, amount: BigDecimal)

final class DonationDAO[P <: JdbcProfile](config: DatabaseConfig[P]) {
  import config.profile.api._
  private class DonationTable(tag: Tag) extends Table[DonationRow](tag, "donation") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def datetime = column[Long]("datetime")

    def amount = column[BigDecimal]("amount")
    def * = (id.?, datetime, amount).mapTo[DonationRow]
  }

  private val table = TableQuery[DonationTable]

  def insertAction(row: DonationRow) = {
    table += row
  }

  def findByLeftCloseRightOpen(startTime: Instant, endTime: Instant): Future[Seq[DonationRow]] = {
    config.db.run {
      table
        .filter(row => (row.datetime >= startTime.toEpochMilli()) && (row.datetime < endTime.toEpochMilli()))
        .result
    }
  }

}
