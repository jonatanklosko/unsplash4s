package unsplash4s.entities

import io.circe.{Decoder, HCursor}

case class PhotoUrls(
  raw: String,
  full: String,
  regular: String,
  small: String,
  thumb: String
)

object PhotoUrls {
  implicit val photoUrlsDecoder: Decoder[PhotoUrls] = (c: HCursor) => {
    for {
      raw <- c.downField("raw").as[String]
      full <- c.downField("full").as[String]
      regular <- c.downField("regular").as[String]
      small <- c.downField("small").as[String]
      thumb <- c.downField("thumb").as[String]
    } yield {
      PhotoUrls(
        raw = raw,
        full = full,
        regular = regular,
        small = small,
        thumb = thumb
      )
    }
  }
}
