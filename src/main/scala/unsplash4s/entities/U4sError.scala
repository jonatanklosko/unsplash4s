package unsplash4s.entities

sealed abstract class U4sError(message: String) extends Exception {
  final override def getMessage: String = message
}

object U4sError {
  final case class UnhandledResponseError(
    message: String,
    body: String
  ) extends U4sError(message) {
    override def toString: String = s"UnhandledResponseError($message, $body)"
  }

  final case class UnauthorizedError(
    message: String
  ) extends U4sError(message) {
    override def toString: String = s"UnauthorizedError($message)"
  }

  final case class ForbiddenError(
    message: String
  ) extends U4sError(message) {
    override def toString: String = s"ForbiddenError($message)"
  }

  final case class NotFoundError(
    message: String
  ) extends U4sError(message) {
    override def toString: String = s"NotFoundError($message)"
  }

  final case class JsonParsingError(
    message: String,
    cause: Option[Throwable]
  ) extends U4sError(message) {
    override def toString: String = s"JsonParsingError($message, $cause)"
  }
}
