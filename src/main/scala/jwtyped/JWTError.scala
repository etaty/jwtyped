package jwtyped

sealed trait JWTError

case class InvalidFormat(message: String) extends JWTError

case class IllegalBase64Character(part: String) extends JWTError

case object InvalidSignature extends JWTError
