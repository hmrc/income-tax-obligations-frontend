/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package config

import com.google.inject.Inject
import play.api.Configuration
import play.api.i18n.{Lang, Messages}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import javax.inject.Singleton

@Singleton
class FrontendAppConfig @Inject()(val servicesConfig: ServicesConfig, val config: Configuration) extends DynamicRoutes {

  lazy val hasEnabledTestOnlyRoutes: Boolean = config.get[String]("play.http.router") == "testOnlyDoNotUseInAppConf.Routes"

  // App
  lazy val baseUrl: String = "/manage-self-assessment/obligations"
  lazy val agentBaseUrl: String = s"$baseUrl/agents"
  lazy val itvcFrontendEnvironment: String = servicesConfig.getString("base.url")
  lazy val appName: String = servicesConfig.getString("appName")

  // Feedback Config
  lazy val contactFrontendBaseUrl: String = servicesConfig.baseUrl("contact-frontend") + "/contact"
  lazy val contactFormServiceIdentifier: String = "ITVC" // change to obligations? 
  lazy val reportAProblemNonJSUrl: String = s"$contactFrontendBaseUrl/problem_reports_nonjs?service=$contactFormServiceIdentifier"
  lazy val betaFeedbackUrl = s"$baseUrl/feedback"
  lazy val agentBetaFeedbackUrl = s"$agentBaseUrl/feedback"
  lazy val noIncomeSourcesContactUrl: String = s"$contactFrontendBaseUrl/report-technical-problem?service=$contactFormServiceIdentifier"

  //Income tax backend service urls
  lazy val incomeTaxObligationsBaseUrl: String = servicesConfig.baseUrl("income-tax-obligations") + "/income-tax-obligations"
  lazy val incomeTaxBusinessDetailsBaseUrl: String = servicesConfig.baseUrl("income-tax-business-details") + "/income-tax-business-details"
  lazy val incomeTaxCalculationBaseUrl: String = servicesConfig.baseUrl("income-tax-calculation") + "/income-tax-calculation"
  
  //GG Sign In via BAS Gateway
  private lazy val ggBaseUrl: String = servicesConfig.baseUrl("government-gateway") + "/bas-gateway"
  lazy val signInUrl: String = controllers.routes.SignInController.signIn().url
  lazy val ggSignInUrl: String = ggBaseUrl + "/sign-in"
  lazy val homePageUrl: String = individualHomeUrl

  //Sign out with redirect to feedback frontend
  private lazy val exitSurveyBaseUrl: String = servicesConfig.baseUrl("feedback-frontend") + "/feedback"
  private def exitSurveyUrl(identifier: String): String = s"$exitSurveyBaseUrl/$identifier"
  def ggSignOutUrl(identifier: String): String = ggBaseUrl + s"/sign-out-without-state?continue=${exitSurveyUrl(identifier)}"

  //Agent Services Account
  lazy val setUpAgentServicesAccountUrl: String = servicesConfig.getString("gov-links.set-up-agent-services-account.url")

  //Subscription Service
  lazy val signUpUrl: String = servicesConfig.getString("gov-links.mtd-subscription-service.url")

  lazy val citizenDetailsUrl: String = servicesConfig.baseUrl("citizen-details")

  lazy val enterSurveyUrl: String = servicesConfig.getString("enter-survey.url")

  // income-tax-session-data url
  lazy val incomeTaxSessionDataUrl: String = servicesConfig.baseUrl("income-tax-session-data")

  // API timeout
  lazy val agentServicesAccountFrontendBaseUrl: String = servicesConfig.baseUrl("agent-services-account-frontend") + "/agent-services-account"

  // Service Navigation Links
  private lazy val ptaFrontendBaseUrl: String = servicesConfig.baseUrl("personal-tax-account") + "/personal-account"
  private lazy val btaFrontendBaseUrl: String = servicesConfig.baseUrl("business-tax-account") + "/business-account"
  private lazy val helpAndContactBaseUrl: String = servicesConfig.baseUrl("help-and-contact-frontend") + "/business-account/help"
  private lazy val trackingBaseUrl: String = servicesConfig.baseUrl("tracking-frontend") + "/track"

  lazy val businessTaxAccountManageAccountUrl: String = s"$btaFrontendBaseUrl/manage-account"
  lazy val businessTaxAccountMessagesUrl: String = s"$btaFrontendBaseUrl/messages"
  lazy val businessTaxAccountHelpUrl: String = helpAndContactBaseUrl

  lazy val personalTaxAccountMessagesUrl: String = s"$ptaFrontendBaseUrl/messages"
  lazy val personalTaxAccountCheckProgressUrl: String = trackingBaseUrl
  lazy val personalTaxAccountProfileUrl: String = s"$ptaFrontendBaseUrl/profile-and-settings"
  lazy val personalTaxAccountBtaUrl: String = btaFrontendBaseUrl

  //Translation
  def languageMap: Map[String, Lang] = Map(
    "english" -> Lang("en"),
    "cymraeg" -> Lang("cy")
  )

  //Auth variables
  lazy val requiredConfidenceLevel: Int = servicesConfig.getInt("auth.confidenceLevel")
  lazy val identityVerificationFrontendBaseUrl = servicesConfig.baseUrl("identity-verification-frontend") + "/iv-stub"
  lazy val relativeIVUpliftParams = servicesConfig.getBoolean("microservice.services.identity-verification-frontend.use-relative-params")

  def incomeSourceOverrides(): Option[Seq[String]] = config.getOptional[Seq[String]]("afterIncomeSourceCreated")

  def triggeredMigrationOverrides(): Option[Seq[String]] = config.getOptional[Seq[String]]("afterMigration")

  val cacheTtl: Int = config.get[Int]("mongodb.timeToLiveInSeconds")

  lazy val readFeatureSwitchesFromMongo: Boolean = servicesConfig.getBoolean("feature-switches.read-from-mongo")
  
  lazy val isTimeMachineEnabled: Boolean = servicesConfig.getBoolean("feature-switch.enable-time-machine")
  lazy val timeMachineAddYears: Int = servicesConfig.getInt("time-machine.add-years")
  lazy val timeMachineAddDays: Int = servicesConfig.getInt("time-machine.add-days")

  lazy val isSessionDataStorageEnabled: Boolean = servicesConfig.getBoolean("feature-switch.enable-session-data-storage")

  //External-Urls
  def logInFileSelfAssessmentTaxReturnLink(implicit messages: Messages): String =
    messages.lang.code match {
      case "en" => "https://www.gov.uk/log-in-file-self-assessment-tax-return"
      case "cy" => "https://www.gov.uk/cyflwyno-ch-ffurflen-dreth-hunanasesiad-ar-lein"
      case _ => "https://www.gov.uk/log-in-file-self-assessment-tax-return"
    }

  def selfAssessmentTaxReturnLink(isAgent: Boolean)(implicit messages: Messages): String =
    messages.lang.code match {
      case _ if isAgent => "https://www.gov.uk/guidance/self-assessment-for-agents-online-service"
      case "en" => "https://www.gov.uk/self-assessment-tax-returns"
      case "cy" => "https://www.gov.uk/ffurflenni-treth-hunanasesiad/trosolwg"
      case _ => "https://www.gov.uk/self-assessment-tax-returns"
    }

  def compatibleSoftwareLink(implicit messages: Messages): String =
    val link = servicesConfig.getString("gov-links.compatible-software.url")
    if messages.lang.code == "cy" then link + ".cy" else link

  lazy val preThreshold2027 = servicesConfig.getString("thresholds.prethreshold2027")
  lazy val threshold2027 = servicesConfig.getString("thresholds.threshold2027")
  lazy val threshold2028 = servicesConfig.getString("thresholds.threshold2028")

  lazy val dynamicStubUrl: String = servicesConfig.baseUrl("itvc-dynamic-stub")

}
