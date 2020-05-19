package unsplash4s.entities

import io.circe.{Decoder, HCursor}

case class Exif(
  make: String,
  model: String,
  exposureTime: String,
  aperture: String,
  focalLength: String,
  iso: Int
)

object Exif {
  implicit val exifDecoder: Decoder[Exif] = (c: HCursor) => {
    for {
      make <- c.downField("make").as[String]
      model <- c.downField("model").as[String]
      exposureTime <- c.downField("exposure_time").as[String]
      aperture <- c.downField("aperture").as[String]
      focalLength <- c.downField("focal_length").as[String]
      iso <- c.downField("iso").as[Int]
    } yield {
      Exif(
        make = make,
        model = model,
        exposureTime = exposureTime,
        aperture = aperture,
        focalLength = focalLength,
        iso = iso
      )
    }
  }
}
