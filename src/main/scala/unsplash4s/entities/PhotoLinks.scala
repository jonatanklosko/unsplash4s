package unsplash4s.entities

import io.circe.{Decoder, HCursor}

case class PhotoLinks(
  self: String,
  html: String,
  download: String,
  downloadLocation: String
)

object PhotoLinks {
  implicit val photoLinksDecoder: Decoder[PhotoLinks] = (c: HCursor) => {
    for {
      self <- c.downField("self").as[String]
      html <- c.downField("html").as[String]
      download <- c.downField("download").as[String]
      downloadLocation <- c.downField("download_location").as[String]
    } yield {
      PhotoLinks(
        self = self,
        html = html,
        download = download,
        downloadLocation = downloadLocation
      )
    }
  }
}
