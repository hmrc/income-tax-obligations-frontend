/*
 * Copyright 2026 HM Revenue & Customs
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

package uk.gov.hmrc.incometaxobligationsfrontend.config

import javax.inject.{Inject, Singleton}
import play.api.Configuration
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

@Singleton
class AppConfig @Inject()(val servicesConfig: ServicesConfig, config: Configuration){

  // TODO: implement controllers.routes.SignOutController.signOut().url from income-tax-view-change-frontend
  lazy val signOutControllerSignOutUrl: String = "#"

  // TODO: implement controllers.routes.HomeController.show().url from income-tax-view-change-frontend
  lazy val homeControllerShowUrl: String = "#"

  // TODO: implement controllers.routes.HomeController.showAgent().url from income-tax-view-change-frontend
  lazy val homeControllerShowAgentUrl: String = "#"

  // TODO: implement controllers.routes.LocalLanguageController.switchToLanguage("cymraeg").url
  //        and .switchToLanguage("english") from income-tax-view-change-frontend
  lazy val localLanguageControllerSwitchLanguageUrl: String = "#"

  // TODO: implement controllers.timeout.routes.SessionTimeoutController.keepAlive().url from income-tax-view-change-frontend
  lazy val sessiontTimeoutControllerKeepAliveUrl: String = "#"

  val welshLanguageSupportEnabled: Boolean =
    config.getOptional[Boolean]("features.welsh-language-support").getOrElse(false)

  lazy val enterSurveyUrl: String = servicesConfig.getString("enter-survey.url")

  lazy val baseUrl: String = "report-quarterly/income-and-expenses/view"
  lazy val agentBaseUrl: String = s"$baseUrl/agents"
  lazy val betaFeedbackUrl = s"/$baseUrl/feedback"
  lazy val agentBetaFeedbackUrl = s"/$agentBaseUrl/feedback"

  private lazy val contactHost: String = servicesConfig.getString("contact-frontend.host")
  lazy val contactFormServiceIdentifier: String = "ITVC"
  lazy val reportAProblemNonJSUrl: String = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"

  lazy val itvcRebrand: Boolean = servicesConfig.getBoolean("itvc.useRebrand")

  lazy val itvcFrontendEnvironment: String = servicesConfig.getString("base.url")

}
