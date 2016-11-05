package jwtyped

import io.circe.generic.semiauto._

case class Header(typ: String, alg: String)

object Header {

  import io.circe.{Decoder, Encoder}

  implicit val decoder: Decoder[Header] = deriveDecoder
  implicit val encoder: Encoder[Header] = deriveEncoder
}
