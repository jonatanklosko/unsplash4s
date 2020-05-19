package unsplash4s.entities

import io.circe.{Decoder, HCursor}

case class ProfileImage(
  small: String,
  medium: String,
  large: String
)

object ProfileImage {
  implicit val profileImageDecoder: Decoder[ProfileImage] = (c: HCursor) => {
    for {
      small <- c.downField("small").as[String]
      medium <- c.downField("medium").as[String]
      large <- c.downField("large").as[String]
    } yield {
      ProfileImage(
        small = small,
        medium = medium,
        large = large
      )
    }
  }
}
