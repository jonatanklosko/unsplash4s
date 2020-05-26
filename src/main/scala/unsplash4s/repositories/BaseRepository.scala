package unsplash4s.repositories

abstract class BaseRepository {
  protected def queryParamsMap(elems: (String, Any)*): Map[String, String] = {
    Map.from(elems).collect {
      case (key, Some(value)) => key -> value.toString
      case (key, value) if value != None => key -> value.toString
    }
  }
}
