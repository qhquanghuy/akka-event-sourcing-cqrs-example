package io.qhquanghuy.btcbillionaire.utils

trait CborSerializable
final case class CborSerialize[T](value: T) extends CborSerializable