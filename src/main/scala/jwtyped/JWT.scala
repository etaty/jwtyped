package jwtyped

import jwtyped.algorithm.{Sign, VerifyJWT}


case class JWT[H, P](header: H, payload: P)

object JWT {

  def decode[H: Decoder, P: Decoder](token: String, algorithm: VerifyJWT): JWTError Either JWT[H, P] = {
    decode(token)(_ => Right(algorithm))
  }

  def decode[H: Decoder, P: Decoder](token: String)(validate: ((H, P)) => JWTError Either VerifyJWT): JWTError Either JWT[H, P] = {
    def decode[A](a: Array[Byte])(implicit decoder: Decoder[A]): JWTError Either A = {
      decoder.apply(a)
    }

    val parts = token.split('.')

    parts match {
      case Array(header, payload, signature) =>
        for {
          headerBytes <- Base64Helper.decode(header, IllegalBase64Character("Header")).right
          payloadBytes <- Base64Helper.decode(payload, IllegalBase64Character("Payload")).right
          signatureBytes <- Base64Helper.decode(signature, IllegalBase64Character("Signature")).right
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


  def encode[Algo: Sign](message: Message, algorithm: Algo): String = {
    val s = Sign[Algo].sign(algorithm, message)
    message.getMessage + '.' + Base64Helper.encode(s.value)
  }
}
