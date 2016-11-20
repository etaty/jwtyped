package jwtyped.algorithm

import java.security.MessageDigest
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import jwtyped.{Message, Signature}


case class AlgoSecretKey(algorithm: String, secret: Secret) {
  val secretKeySpec: SecretKeySpec = new SecretKeySpec(secret.bytes, algorithm)
}

object AlgoSecretKey {
  implicit val hmacSHASign = new Sign[AlgoSecretKey] {
    override def sign(algorithm: AlgoSecretKey, message: Message): Signature = {
      val mac: Mac = Mac.getInstance(algorithm.algorithm)
      mac.init(algorithm.secretKeySpec)
      Signature(mac.doFinal(message.getBytes))
    }
  }

  implicit def hmacSHAVerify(implicit hmacSHASign: Sign[AlgoSecretKey]) = new Verify[AlgoSecretKey] {
    override def verify(algorithm: AlgoSecretKey, message: Message, signature: Signature): Boolean = {
      val s = hmacSHASign.sign(algorithm, message)
      MessageDigest.isEqual(s.value, signature.value)
    }
  }
}