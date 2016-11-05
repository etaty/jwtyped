package jwtyped

import java.nio.charset.StandardCharsets

import jwtyped.Decoder.Result

trait Decoder[A] {
  self =>

  def apply(bytes: Array[Byte]): Decoder.Result[A]

  def map[B](f: A => B): Decoder[B] = new Decoder[B] {
    override def apply(bytes: Array[Byte]): Result[B] = self(bytes).right.map(f)
  }

  def flatMap[B](f: A => Decoder[B]): Decoder[B] = new Decoder[B] {
    override def apply(bytes: Array[Byte]): Result[B] = {
      self(bytes).right.flatMap(f(_)(bytes))
    }
  }
}

object Decoder {
  type Result[A] = Either[JWTError, A]

  implicit val stringDecoder: Decoder[String] = new Decoder[String] {
    override def apply(bytes: Array[Byte]): Result[String] = Right(new String(bytes, StandardCharsets.UTF_8))
  }
}