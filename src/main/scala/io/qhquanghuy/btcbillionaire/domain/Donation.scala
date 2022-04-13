package io.qhquanghuy.btcbillionaire.domain

import java.time.ZonedDateTime

final case class Donation(time: ZonedDateTime, amount: BTC)