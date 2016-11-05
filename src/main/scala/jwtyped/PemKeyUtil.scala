package jwtyped

import java.security.{PrivateKey, PublicKey}
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64
import java.security.spec.X509EncodedKeySpec
import java.security.KeyFactory

object PemKeyUtil {

  def decodePublicKey(pem: String): PublicKey = {
    val bytes = pemToDer(pem)
    decodePublicKey(bytes)
  }

  def decodePrivateKey(pem: String): PrivateKey = {
    val bytes = pemToDer(pem)
    decodePrivateKey(bytes)
  }

  def pemToDer(pem: String): Array[Byte] = {
    Base64.getDecoder.decode(trimBeginEnd(pem))
  }

  def trimBeginEnd(pem: String) = {
    pem.replaceAll("-----BEGIN (.*)-----", "")
      .replaceAll("-----END (.*)----", "")
      .replaceAll("\r\n", "")
      .replaceAll("\n", "")
      .trim()
  }

  def decodePublicKey(der: Array[Byte]): PublicKey = {
    val spec = new X509EncodedKeySpec(der)
    val rsa = KeyFactory.getInstance("RSA")
    rsa.generatePublic(spec)
  }

  def decodePrivateKey(der: Array[Byte]): PrivateKey = {
    val spec = new PKCS8EncodedKeySpec(der)
    val rsa = KeyFactory.getInstance("RSA")
    rsa.generatePrivate(spec)
  }

}
