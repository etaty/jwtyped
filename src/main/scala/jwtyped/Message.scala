package jwtyped

import java.nio.charset.StandardCharsets

case class Message(header: String, payload: String) {
  def getMessage = header + "." + payload

  def getBytes: Array[Byte] = getMessage.getBytes(StandardCharsets.UTF_8)
}

object Message {
  def from[H: Encoder, P: Encoder](header: H, payload: P)(implicit encoderH: Encoder[H], encoderP: Encoder[P]): Message = {
    Message(Base64Helper.encode(encoderH(header)), Base64Helper.encode(encoderP(payload)))
  }
}
