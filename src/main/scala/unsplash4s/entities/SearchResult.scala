package unsplash4s.entities

case class SearchResult[T](
  total: Int,
  totalPages: Int,
  results: Seq[T]
)
