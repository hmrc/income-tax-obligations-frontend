import play.sbt.routes.RoutesKeys
import sbt.*
import sbt.Keys.libraryDependencySchemes
import uk.gov.hmrc.DefaultBuildSettings
import uk.gov.hmrc.DefaultBuildSettings.*
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion

val appName = "income-tax-obligations-frontend"
ThisBuild / majorVersion := 0

val bootstrapPlayVersion = "10.7.0"
val playPartialsVersion = "10.2.0"
val playFrontendHMRCVersion = "12.32.0"
val catsVersion = "2.13.0"
val jsoupVersion = "1.22.1"
val mockitoVersion = "5.23.0"
val scalaMockVersion = "7.5.5"
val wiremockVersion = "3.0.1"
val hmrcMongoVersion = "2.12.0"
val currentScalaVersion = "2.13.16"
val playVersion = "play-30"

scalacOptions ++= Seq(
  "-feature",
  "-Wconf:src=target/.*:silent")

lazy val plugins: Seq[Plugins] = Seq.empty
lazy val playSettings: Seq[Setting[_]] = Seq.empty

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(playSettings *)
  .settings(scalaSettings *)
  .settings(scalaVersion := currentScalaVersion)
  .settings(
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
    retrieveManaged := true
  )
  .settings(CodeCoverageSettings.settings: _*)
  .settings(defaultSettings() *)
  .settings(
    Test / Keys.fork := true,
    Test / javaOptions += "-Dlogger.resource=logback-test.xml",
    libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always,
    PlayKeys.playDefaultPort := 9075
  )
  .settings(
    Keys.fork := false,
    TwirlKeys.templateImports ++= Seq(
      "uk.gov.hmrc.govukfrontend.views.html.components.implicits._",
      "uk.gov.hmrc.hmrcfrontend.views.html.helpers._",
      "uk.gov.hmrc.hmrcfrontend.views.html.components.implicits._"
    )
  )

def test(scope: String = "test"): Seq[ModuleID] = Seq(
  "org.scalamock" %% "scalamock" % scalaMockVersion % scope,
  "org.jsoup" % "jsoup" % jsoupVersion % scope,
  "org.mockito" % "mockito-core" % mockitoVersion % scope,
  "uk.gov.hmrc.mongo" %% s"hmrc-mongo-test-$playVersion" % hmrcMongoVersion % scope,
  "org.scalacheck" %% "scalacheck" % "1.19.0" % scope,
  "org.scalatestplus" %% "scalacheck-1-15" % "3.2.11.0" % scope,
  "uk.gov.hmrc" %% s"bootstrap-test-$playVersion" % bootstrapPlayVersion % "test",
  caffeine,
  "uk.gov.hmrc" %% s"crypto-json-$playVersion" % "8.1.0"
)

lazy val it = project
  .dependsOn(microservice % "test->test")
  .settings(DefaultBuildSettings.itSettings().head)
  .enablePlugins(play.sbt.PlayScala)
  .settings(
    publish / skip := true
  )
  .settings(scalaVersion := currentScalaVersion)
  .settings(
    testForkedParallel := true
  )
  .settings(libraryDependencies ++= AppDependencies.it)

addCommandAlias("compileAll", "compile ; test:compile ; it/test:compile")