import play.sbt.routes.RoutesKeys
import sbt.*
import sbt.Keys.libraryDependencySchemes
import uk.gov.hmrc.DefaultBuildSettings
import uk.gov.hmrc.DefaultBuildSettings.*
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion

val appName = "income-tax-obligations-frontend"
val currentScalaVersion = "3.3.6"

// Main compiler settings applied globally
ThisBuild / scalaVersion := currentScalaVersion
ThisBuild / scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-Wconf:src=target/.*:silent",
  "-Wconf:msg=value name in trait Retrievals is deprecated:silent",
  "-Wconf:msg=Flag.*repeatedly:s"
)

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(scalaSettings *)
  .settings(CodeCoverageSettings.settings *)
  .settings(defaultSettings() *)
  .settings(majorVersion := 1)
  .settings(
    Test / Keys.fork := true,
    Test / javaOptions += "-Dlogger.resource=logback-test.xml",
    libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always,
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test ++ AppDependencies.it,
    retrieveManaged := true
  )
  .settings(
    TwirlKeys.templateImports ++= Seq(
      "uk.gov.hmrc.govukfrontend.views.html.components.implicits._",
      "uk.gov.hmrc.hmrcfrontend.views.html.helpers._",
      "uk.gov.hmrc.hmrcfrontend.views.html.components.implicits._"
    ),
    RoutesKeys.routesImport := Seq(
      "enums.IncomeSourceJourney._", 
      "models.admin._", 
      "models.core._",
    )
  )
  .settings(
    scalacOptions --= Seq("-Wunused", "-Wunused:all"),
    Test / scalacOptions ++= Seq(
      "-Wunused:imports",
      "-Wunused:params",
      "-Wunused:implicits",
      "-Wunused:explicits",
      "-Wunused:privates"
    )
  )

lazy val it = project
  .dependsOn(microservice % "compile->compile;test->test") // Updated to fix missing dependencies
  .settings(DefaultBuildSettings.itSettings())
  .enablePlugins(play.sbt.PlayScala)
  .settings(
    publish / skip := true,
    majorVersion := 1,
    testForkedParallel := true,
    libraryDependencies ++= AppDependencies.it
  )

addCommandAlias("compileAll", "compile ; test:compile ; it/Test/compile")
