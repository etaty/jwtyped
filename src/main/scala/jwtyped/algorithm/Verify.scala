package jwtyped.algorithm

import jwtyped.{Message, Signature}

trait Verify[T] {
  def verify(algorithm: T, message: Message, signature: Signature): Boolean
}

object Verify {
  def apply[T](implicit verify: Verify[T]): Verify[T] = verify
}

trait VerifyJWT {
  def verify(message: Message, signature: Signature): Boolean
}

object VerifyJWT {
  implicit def toVerify[T](algo: T)(implicit _verify: Verify[T]): VerifyJWT = new VerifyJWT {
    def verify(message: Message, signature: Signature): Boolean = {
      _verify.verify(algo, message, signature)
    }
  }
}