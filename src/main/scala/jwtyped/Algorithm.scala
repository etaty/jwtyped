package jwtyped

import java.security.{KeyPair, MessageDigest, PrivateKey, PublicKey}
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


trait Algorithm {
  def sign(message: Message): Signature

  def verify(message: Message, signature: Signature): Boolean
}

class HmacSHA(algorithm: String, secret: Secret) extends Algorithm {
  val secretKeySpec: SecretKeySpec = new SecretKeySpec(secret.bytes, algorithm)

  def verify(message: Message, signature: Signature): Boolean = {
    val s = sign(message)
    MessageDigest.isEqual(s.value, signature.value)
  }

  def sign(message: Message): Signature = {
    val mac: Mac = Mac.getInstance(algorithm)
    mac.init(secretKeySpec)
    Signature(mac.doFinal(message.getBytes))
  }
}

case class HS256(secret: Secret) extends HmacSHA("HmacSHA256", secret)

case class HS384(secret: Secret) extends HmacSHA("HmacSHA384", secret)

case class HS512(secret: Secret) extends HmacSHA("HmacSHA512", secret)

class Rsa(algorithm: String, publicKey: PublicKey, privateKey: PrivateKey) extends Algorithm {
  def verify(message: Message, signature: Signature): Boolean = {
    import java.security.Signature
    val rsa = Signature.getInstance(algorithm)
    rsa.initVerify(publicKey)
    rsa.update(message.getBytes)
    rsa.verify(signature.value)
  }

  def sign(message: Message): Signature = {
    val rsa = java.security.Signature.getInstance(algorithm)
    rsa.initSign(privateKey)
    rsa.update(message.getBytes)
    Signature(rsa.sign())
  }
}

case class RS256(publicKey: PublicKey, privateKey: PrivateKey) extends Rsa("SHA256withRSA", publicKey, privateKey)

case class RS384(publicKey: PublicKey, privateKey: PrivateKey) extends Rsa("SHA384withRSA", publicKey, privateKey)

case class RS512(publicKey: PublicKey, privateKey: PrivateKey) extends Rsa("SHA512withRSA", publicKey, privateKey)

class Ecdsa(algorithm: String, keyPair: KeyPair) extends Algorithm {
  override def verify(message: Message, signature: Signature): Boolean = {
    val ecdsaVerify = java.security.Signature.getInstance(algorithm)
    ecdsaVerify.initVerify(keyPair.getPublic)
    ecdsaVerify.update(message.getBytes)
    ecdsaVerify.verify(signature.value)
  }

  override def sign(message: Message): Signature = {
    val ecdsaSign = java.security.Signature.getInstance(algorithm)
    ecdsaSign.initSign(keyPair.getPrivate)
    ecdsaSign.update(message.getBytes)
    Signature(ecdsaSign.sign())
  }
}

case class ES256(keyPair: KeyPair) extends Ecdsa("SHA256withECDSA", keyPair)

case class ES384(keyPair: KeyPair) extends Ecdsa("SHA384withECDSA", keyPair)

case class ES512(keyPair: KeyPair) extends Ecdsa("SHA512withECDSA", keyPair)