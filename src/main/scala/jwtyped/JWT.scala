package jwtyped

import java.nio.charset.StandardCharsets
import java.util.Base64

import scala.util.Try


case class JWT[H, P](header: H, payload: P)

object JWT {
  def base64Encode(a: Array[Byte]): String = {
    new String(Base64.getUrlEncoder.withoutPadding().encode(a), StandardCharsets.UTF_8)
  }

  def decodeBase64(s: String, err: => JWTError): JWTError Either Array[Byte] = {
    Try(Base64.getUrlDecoder.decode(s))
      .map(Right(_))
      .getOrElse(Left(err))
  }

  def decode[H: Decoder, P: Decoder](token: String, algorithm: Algorithm): JWTError Either JWT[H, P] = {
    decode(token)(_ => Right(algorithm))
  }

  def decode[H: Decoder, P: Decoder](token: String)(validate: ((H, P)) => JWTError Either Algorithm): JWTError Either JWT[H, P] = {
    val parts = token.split('.')

    parts match {
      case Array(header, payload, signature) =>
        for {
          headerBytes <- decodeBase64(header, IllegalBase64Character("Header")).right
          payloadBytes <- decodeBase64(payload, IllegalBase64Character("Payload")).right
          signatureBytes <- decodeBase64(signature, IllegalBase64Character("Signature")).right
          h <- decode[H](headerBytes).right
          p <- decode[P](payloadBytes).right
          algorithm <- validate(h, p).right
          _ <- {
            if (algorithm.verify(Message(header, payload), Signature(signatureBytes)))
              Right(())
            else
              Left(InvalidSignature)
          }.right
        } yield {
          JWT[H, P](h, p)
        }
      case _ => Left(InvalidFormat("JWT should have header.payload.signature"))
    }
  }

  def decode[A](a: Array[Byte])(implicit decoder: Decoder[A]): JWTError Either A = {
    decoder.apply(a)
  }

  def encode(message: Message, algorithm: Algorithm): String = {
    val s = algorithm.sign(message)
    message.getMessage + '.' + base64Encode(s.value)
  }
}
