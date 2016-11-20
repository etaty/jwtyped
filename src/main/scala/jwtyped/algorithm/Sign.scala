package jwtyped.algorithm

import jwtyped.{Message, Signature}

trait Sign[T] {
  def sign(algorithm: T, message: Message): Signature
}

object Sign {
  def apply[T](implicit sign: Sign[T]): Sign[T] = sign
}
