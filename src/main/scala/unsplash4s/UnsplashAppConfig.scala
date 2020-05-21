package unsplash4s

case class UnsplashAppConfig(
  applicationAccessKey: String,
  applicationSecret: Option[String] = None,
  oauthRedirectUri: Option[String] = None,
  apiUrl: String = UnsplashAppConfig.DefaultApiUrl,
  oauthUrl: String = UnsplashAppConfig.DefaultOAuthUrl,
  apiVersion: String = UnsplashAppConfig.DefaultApiVersion
)

object UnsplashAppConfig {
  val DefaultApiUrl = "https://api.unsplash.com"
  val DefaultOAuthUrl = "https://unsplash.com/oauth"
  val DefaultApiVersion = "v1"
}
