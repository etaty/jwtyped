package jwtyped

import io.circe.generic.semiauto._

case class Payload(sub: String, name: String, admin: Boolean)

object Payload {

  import io.circe.{Decoder, Encoder}

  implicit val decoder: Decoder[Payload] = deriveDecoder
  implicit val encoder: Encoder[Payload] = deriveEncoder
}
