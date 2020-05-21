package unsplash4s.entities

import java.time.Instant

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
