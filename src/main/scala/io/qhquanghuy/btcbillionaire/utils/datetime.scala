package io.qhquanghuy.btcbillionaire.utils

import java.time.ZonedDateTime

object datetime {
  def beginningOfHour(zonedDateTime: ZonedDateTime) = {
    zonedDateTime
      .withMinute(0)
      .withSecond(0)
      .withNano(0)
  }

}
