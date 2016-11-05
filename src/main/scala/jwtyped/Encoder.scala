package jwtyped

import java.nio.charset.StandardCharsets

trait Encoder[A] {
  self =>

  def apply(a: A): Array[Byte]

  def contramap[B](f: B => A): Encoder[B] = new Encoder[B] {
    override def apply(b: B): Array[Byte] = {
      self(f(b))
    }
  }

}

object Encoder {
  implicit val stringEncoder = new Encoder[String] {
    override def apply(a: String): Array[Byte] = a.getBytes(StandardCharsets.UTF_8)
  }
}
