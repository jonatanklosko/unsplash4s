package unsplash4s.entities

import java.time.Instant

case class Collection(
  id: Int,
  title: String,
  description: Option[String],
  publishedAt: Instant,
  updatedAt: Instant,
  curated: Boolean,
  featured: Boolean,
  totalPhotos: Int,
  `private`: Boolean,
  shareKey: String,
  user: User,
  coverPhoto: Option[Photo]
)
