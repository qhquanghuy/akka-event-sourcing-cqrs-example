package io.qhquanghuy.btcbillionaire.domain

trait DomainError { self =>
  def code: String = self.getClass().getName()
  def msg: String
}
