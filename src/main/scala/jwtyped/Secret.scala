package jwtyped

import java.nio.charset.StandardCharsets
import java.util.Base64

case class Secret(bytes: Array[Byte]) extends AnyVal

object Secret {
  def from64UrlEncoded(key: String) = {
    Secret(Base64.getUrlDecoder.decode(key))
  }

  def fromString(key: String) = {
    Secret(key.getBytes(StandardCharsets.UTF_8))
  }
}
