package unsplash4s.utils

import io.circe.Decoder
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalamock.scalatest.MockFactory
import unsplash4s.HttpClient

class BaseUnitSpec extends AnyFlatSpec with Matchers with MockFactory {
  def httpClientExpectingApiGet[T](path: String, params: Map[String, String] = Map()): HttpClient = {
    val httpClient = mock[HttpClient]
    (httpClient.apiGet[T](_: String, _: Map[String, String])(_: Decoder[T])).expects(path, params, *).once
    httpClient
  }
}
