package jwtyped

import java.nio.charset.StandardCharsets
import java.util.Base64

import scala.util.Try

object Base64Helper {
  def encode(a: Array[Byte]): String = {
    new String(Base64.getUrlEncoder.withoutPadding().encode(a), StandardCharsets.UTF_8)
  }

  def decode(s: String, err: => JWTError): JWTError Either Array[Byte] = {
    Try(Base64.getUrlDecoder.decode(s))
      .map(Right(_))
      .getOrElse(Left(err))
  }
}
