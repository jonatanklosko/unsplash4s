package unsplash4s

import java.time.Instant

import io.circe.{Decoder, HCursor}
import unsplash4s.entities.{AccessToken, Collection, Exif, Photo, PhotoLinks, PhotoUrls, ProfileImage, SearchResult, User}
import unsplash4s.entities.AccessToken.Scope

object Decoders {
  implicit val accessTokenDecoder: Decoder[AccessToken] = (c: HCursor) => {
    for {
      accessToken <- c.downField("access_token").as[String]
      tokenType <- c.downField("token_type").as[String]
      scope <- c.downField("scope").as[String].map(scopeString => scopeString.split(" ").map(Scope.withName).toSet)
      createdAt <- c.downField("created_at").as[Int].map(Instant.ofEpochSecond(_))
    } yield {
      AccessToken(
        accessToken = accessToken,
        tokenType = tokenType,
        scope = scope,
        createdAt = createdAt
      )
    }
  }

  implicit val collectionDecoder: Decoder[Collection] = (c: HCursor) => {
    for {
      id <- c.downField("id").as[Int]
      title <- c.downField("title").as[String]
      description <- c.downField("description").as[Option[String]]
      publishedAt <- c.downField("published_at").as[Instant]
      updatedAt <- c.downField("updated_at").as[Instant]
      curated <- c.downField("curated").as[Boolean]
      featured <- c.downField("featured").as[Boolean]
      totalPhotos <- c.downField("total_photos").as[Int]
      isPrivate <- c.downField("private").as[Boolean]
      shareKey <- c.downField("share_key").as[String]
      user <- c.downField("user").as[User]
      coverPhoto <- c.downField("cover_photo").as[Option[Photo]]
    } yield {
      Collection(
        id = id,
        title = title,
        description = description,
        publishedAt = publishedAt,
        updatedAt = updatedAt,
        curated = curated,
        featured = featured,
        totalPhotos = totalPhotos,
        `private` = isPrivate,
        shareKey = shareKey,
        user = user,
        coverPhoto = coverPhoto
      )
    }
  }

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

  implicit val photoDecoder: Decoder[Photo] = (c: HCursor) => {
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
      exif <- c.downField("exis").as[Option[Exif]]
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

  implicit def searchResultDecoder[T](implicit D: Decoder[T]): Decoder[SearchResult[T]] = (c: HCursor) => {
    for {
      total <- c.downField("total").as[Int]
      totalPages <- c.downField("total_pages").as[Int]
      results <- c.downField("results").as[Seq[T]]
    } yield {
      SearchResult(
        total = total,
        totalPages = totalPages,
        results = results
      )
    }
  }

  implicit val userDecoder: Decoder[User] = (c: HCursor) => {
    for {
      id <- c.downField("id").as[String]
      username <- c.downField("username").as[String]
      name <- c.downField("name").as[String]
      firstName <- c.downField("first_name").as[String]
      lastName <- c.downField("last_name").as[Option[String]]
      bio <- c.downField("bio").as[Option[String]]
      location <- c.downField("location").as[Option[String]]
      portfolioUrl <- c.downField("portfolio_url").as[Option[String]]
      twitterUsername <- c.downField("twitter_username").as[Option[String]]
      instagramUsername <- c.downField("instagram_username").as[Option[String]]
      profileImage <- c.downField("profile_image").as[ProfileImage]
      totalCollections <- c.downField("total_collections").as[Int]
      totalLikes <- c.downField("total_likes").as[Int]
      totalPhotos <- c.downField("total_photos").as[Int]
      acceptedTos <- c.downField("accepted_tos").as[Boolean]
    } yield {
      User(
        id = id,
        username = username,
        name = name,
        firstName = firstName,
        lastName = lastName,
        bio = bio,
        location = location,
        portfolioUrl  = portfolioUrl,
        twitterUsername = twitterUsername,
        instagramUsername = instagramUsername,
        profileImage = profileImage,
        totalCollections = totalCollections,
        totalLikes = totalLikes,
        totalPhotos = totalPhotos,
        acceptedTos = acceptedTos
      )
    }
  }
}
