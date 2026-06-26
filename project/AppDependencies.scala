import sbt.*
import play.sbt.PlayImport.ws
import play.sbt.PlayImport.caffeine

object AppDependencies {

  val bootstrapPlayVersion = "10.5.0"
  val hmrcMongoVersion = "2.12.0"

  val compile = Seq(
    ws,
    "uk.gov.hmrc"       %% "bootstrap-frontend-play-30" % bootstrapPlayVersion,
    "uk.gov.hmrc"       %% "play-partials-play-30"      % "10.2.0",
    "uk.gov.hmrc"       %% "play-frontend-hmrc-play-30" % "12.31.0",
    "uk.gov.hmrc"       %% "crypto-json-play-30"        % "8.4.0",
    "uk.gov.hmrc.mongo" %% "hmrc-mongo-play-30"         % hmrcMongoVersion,
    "org.typelevel"     %% "cats-core"                   % "2.13.0",
    "org.jsoup"         % "jsoup"                        % "1.22.2"
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-30"         % bootstrapPlayVersion  % Test,
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-test-play-30"        % hmrcMongoVersion      % Test,
    "org.scalacheck"          %% "scalacheck"                     % "1.19.0"              % Test,
    "org.scalatestplus"       %% "scalacheck-1-15"                % "3.2.11.0"            % Test,
    "org.scalatestplus.play"  %% "scalatestplus-play"             % "7.0.2"               % Test,
    "org.scalamock"           %% "scalamock"                      % "7.5.5"               % Test,
    "org.mockito"             % "mockito-core"                    % "5.23.0"              % Test,
    caffeine,
  )

  val it = Seq(
    "com.github.tomakehurst" % "wiremock" % "3.0.1" % Test,
  )
}