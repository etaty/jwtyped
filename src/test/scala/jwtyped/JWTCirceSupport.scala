package jwtyped

object JWTCirceSupport {
  implicit def decoderCirce[A: io.circe.Decoder]: Decoder[A] = Decoder.stringDecoder.flatMap { s =>
    import io.circe.parser._
    new Decoder[A] {
      override def apply(bytes: Array[Byte]): Decoder.Result[A] = {
        decode[A](s).leftMap[JWTError](e => InvalidFormat(e.getMessage)).toEither
      }
    }
  }

  implicit def encoderCirce[A: io.circe.Encoder]: Encoder[A] = Encoder.stringEncoder.contramap { a =>
    import io.circe.syntax._
    a.asJson.noSpaces
  }
}
