package unsplash4s.entities

import java.time.Instant

import io.circe.{Decoder, HCursor}

case class Photo(
  id: String,
  createdAt: Instant,
  updatedAt: Instant,
  promotedAt: Option[Instant],
  width: Int,
  height: Int,
  color: String,
  likes: Int,
  likedByUser: Boolean,
  description: Option[String],
  altDescription: Option[String],
  urls: PhotoUrls,
  links: PhotoLinks,
  user: User,
  exif: Option[Exif]
)

object Photo {
  implicit val userDecoder: Decoder[Photo] = (c: HCursor) => {
    for {
      id <- c.downField("id").as[String]
      createdAt <- c.downField("created_at").as[Instant]
      updatedAt <- c.downField("updated_at").as[Instant]
      promotedAt <- c.downField("promoted_at").as[Option[Instant]]
      width <- c.downField("width").as[Int]
      height <- c.downField("height").as[Int]
      color <- c.downField("color").as[String]
      likes <- c.downField("likes").as[Int]
      likedByUser <- c.downField("liked_by_user").as[Boolean]
      description <- c.downField("description").as[Option[String]]
      altDescription <- c.downField("alt_description").as[Option[String]]
      urls <- c.downField("urls").as[PhotoUrls]
      links <- c.downField("links").as[PhotoLinks]
      user <- c.downField("user").as[User]
      exif <- c.downField("exif").as[Option[Exif]]
    } yield {
      Photo(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        promotedAt = promotedAt,
        width = width,
        height = height,
        color = color,
        likes = likes,
        likedByUser = likedByUser,
        description = description,
        altDescription = altDescription,
        urls = urls,
        links = links,
        user = user,
        exif = exif
      )
    }
  }
}
