package io.qhquanghuy.btcbillionaire.domain

import java.time.ZonedDateTime

final case class DonationDTO(datetime: ZonedDateTime, amount: BigDecimal)