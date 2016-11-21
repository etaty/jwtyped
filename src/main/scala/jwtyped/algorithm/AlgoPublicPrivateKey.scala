package jwtyped.algorithm

import java.security.{PrivateKey, PublicKey}

import jwtyped._

case class AlgoPublicKey(algorithm: String, publicKey: PublicKey)

object AlgoPublicKey {
  implicit val ecdsaVerify = new Verify[AlgoPublicKey] {
    override def verify(algorithm: AlgoPublicKey, message: Message, signature: Signature): Boolean = {
      val ecdsaVerify = java.security.Signature.getInstance(algorithm.algorithm)
      ecdsaVerify.initVerify(algorithm.publicKey)
      ecdsaVerify.update(message.getBytes)
      ecdsaVerify.verify(signature.value)
    }
  }
}

case class AlgoPrivateKey(algorithm: String, privateKey: PrivateKey)

object AlgoPrivateKey {
  implicit val ecdsaSign = new Sign[AlgoPrivateKey] {
    override def sign(algorithm: AlgoPrivateKey, message: Message): Signature = {
      val ecdsaSign = java.security.Signature.getInstance(algorithm.algorithm)
      ecdsaSign.initSign(algorithm.privateKey)
      ecdsaSign.update(message.getBytes)
      Signature(ecdsaSign.sign())
    }
  }
}

case class AlgoPublicPrivateKey(algorithm: String, publicKey: PublicKey, privateKey: PrivateKey)

object AlgoPublicPrivateKey {

  def toAlgoPublicKey(algo: AlgoPublicPrivateKey): AlgoPublicKey = AlgoPublicKey(algo.algorithm, algo.publicKey)

  def toAlgoPrivateKey(algo: AlgoPublicPrivateKey): AlgoPrivateKey = AlgoPrivateKey(algo.algorithm, algo.privateKey)

  implicit val algoPublicPrivateKeySign = new Sign[AlgoPublicPrivateKey] {
    override def sign(algorithm: AlgoPublicPrivateKey, message: Message): Signature = {
      Sign[AlgoPrivateKey].sign(AlgoPublicPrivateKey.toAlgoPrivateKey(algorithm), message)
    }
  }

  implicit val algoPublicPrivateKeyVerify = new Verify[AlgoPublicPrivateKey] {
    override def verify(algorithm: AlgoPublicPrivateKey, message: Message, signature: Signature): Boolean = {
      Verify[AlgoPublicKey].verify(AlgoPublicPrivateKey.toAlgoPublicKey(algorithm), message, signature)
    }
  }
}
