package unsplash4s.entities

case class Historical(
   change: Int,
   resolution: String,
   quantity: Int,
   values: Seq[Value]
)
