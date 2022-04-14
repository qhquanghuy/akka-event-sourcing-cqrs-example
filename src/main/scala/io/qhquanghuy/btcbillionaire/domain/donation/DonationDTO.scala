package io.qhquanghuy.btcbillionaire.domain.donation

import java.time.ZonedDateTime

final case class DonationDTO(datetime: ZonedDateTime, amount: BigDecimal)