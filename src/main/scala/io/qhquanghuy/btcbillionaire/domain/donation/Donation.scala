package io.qhquanghuy.btcbillionaire.domain.donation

import java.time.ZonedDateTime
import io.qhquanghuy.btcbillionaire.domain.BTC

final case class Donation(time: ZonedDateTime, amount: BTC)