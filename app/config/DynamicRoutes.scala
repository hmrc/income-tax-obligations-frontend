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

package config

import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

trait DynamicRoutes {
  val servicesConfig: ServicesConfig

  // needed for refactor (can be removed from config once refactoring is complete)
  private lazy val viewChangeFrontendBaseUrl = servicesConfig.baseUrl("income-tax-view-change-frontend") + "/report-quarterly/income-and-expenses/view"
  
  lazy val individualHomeUrl: String = viewChangeFrontendBaseUrl + "/"
  def individualHomeUrl(origin: Option[String] = None): String = individualHomeUrl + s"?origin=$origin"
  lazy val agentHomeUrl: String = viewChangeFrontendBaseUrl + "/agents/client-income-tax"
  def homePageUrl(isAgent: Boolean): String = if isAgent then agentHomeUrl else individualHomeUrl

  lazy val enterClientsUTRUrl: String = viewChangeFrontendBaseUrl + "/agents/client-utr"
  lazy val confirmClientUTRUrl: String = viewChangeFrontendBaseUrl + "/agents/confirm-client-details"

  def nextUpdatesIndividualUrl(origin: Option[String] = None): String = controllers.routes.NextUpdatesController.show(origin).url
  lazy val nextUpdatesAgentUrl: String = controllers.routes.NextUpdatesController.showAgent().url

  
  // TODO eventually will be income-tax-returns-frontend
  lazy val returnsBaseUrl = servicesConfig.baseUrl("income-tax-view-change-frontend") + "/report-quarterly/income-and-expenses/view"
  
  def taxYearsUrl(isAgent: Boolean): String = if isAgent 
    then returnsBaseUrl + "/agents/tax-years" 
    else returnsBaseUrl + "/tax-years"

  // TODO eventually will be income-tax-business-details-frontend
  lazy val businessBaseUrl = servicesConfig.baseUrl("income-tax-view-change-frontend") + "/report-quarterly/income-and-expenses/view"
  
  def manageYourBusinessUrl(isAgent: Boolean): String = if isAgent 
    then businessBaseUrl + "/agents/manage-your-businesses"
    else businessBaseUrl + "/manage-your-businesses"
}