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

package common.config

import play.api.Configuration
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

trait ExternalRedirectHelper {

  val servicesConfig: ServicesConfig
  val config: Configuration
  
  lazy val vcFrontendBaseUrl: String = servicesConfig.getString("income-tax-view-change-frontend.baseUrl")
  lazy val vcFrontendAgentBaseUrl: String = s"${vcFrontendBaseUrl}/agents"

  def hubBaseUrl(newHubContextRootEnabled: Boolean): String =
    if(newHubContextRootEnabled) servicesConfig.getString("income-tax-view-change-frontend.hubBaseUrl") else vcFrontendBaseUrl
    
  def hubAgentBaseUrl(newHubContextRootEnabled: Boolean): String =
    s"${hubBaseUrl(newHubContextRootEnabled)}/agents"

  def individualHomeUrl(newHubContextRootEnabled: Boolean): String =
    s"${hubBaseUrl(newHubContextRootEnabled)}/income-tax"

  def individualHomeUrlWithOrigin(newHubContextRootEnabled: Boolean, origin: Option[String]): String =
    origin.fold(individualHomeUrl(newHubContextRootEnabled))(o => s"${individualHomeUrl(newHubContextRootEnabled)}?origin=$o")

  def agentHomeUrl(newHubContextRootEnabled: Boolean): String =
    s"${hubAgentBaseUrl(newHubContextRootEnabled)}/client-income-tax"

  def homePageUrl(isAgent: Boolean, newHubContextRootEnabled: Boolean, origin: Option[String] = None): String =
    if (isAgent) agentHomeUrl(newHubContextRootEnabled) else individualHomeUrlWithOrigin(newHubContextRootEnabled, origin)


  def enterClientsUTRUrl(newHubContextRootEnabled: Boolean): String =
    s"${hubAgentBaseUrl(newHubContextRootEnabled)}/client-utr"
  
  def confirmClientUTRUrl(newHubContextRootEnabled: Boolean): String =
    s"${hubAgentBaseUrl(newHubContextRootEnabled)}/confirm-client-details"
  
  //Business Details routes
  lazy val businessDetailsBaseUrl: String = servicesConfig.getString("income-tax-business-details-frontend.baseUrl")
  lazy val businessDetailsAgentBaseUrl: String = s"$businessDetailsBaseUrl/agents"

  def triggeredMigrationCheckHMRCRecordsUrl(isAgent: Boolean, businessDetailsFrontendEnabled: Boolean): String = {
    if (businessDetailsFrontendEnabled) {
      val baseUri = if (isAgent) businessDetailsAgentBaseUrl else businessDetailsBaseUrl
      s"$baseUri/check-your-active-businesses/hmrc-record"
    } else {
      val baseUri = if (isAgent) vcFrontendAgentBaseUrl else vcFrontendBaseUrl
      s"$baseUri/check-your-active-businesses/hmrc-record"
    }
  }

  lazy val businessDetailsManageBusinessesIndividualUrl: Boolean => String = businessDetailsFrontendEnabled =>
    if (businessDetailsFrontendEnabled)
      s"$businessDetailsBaseUrl/manage-your-businesses"
    else
      s"$vcFrontendBaseUrl/manage-your-businesses"

  lazy val businessDetailsManageBusinessesAgentUrl: Boolean => String = businessDetailsFrontendEnabled =>
    if (businessDetailsFrontendEnabled)
      s"$businessDetailsAgentBaseUrl/manage-your-businesses"
    else
      s"$vcFrontendAgentBaseUrl/manage-your-businesses"

  def manageBusinessesUrl(isAgent: Boolean, businessDetailsFrontendEnabled: Boolean): String =
    if (isAgent)
      businessDetailsManageBusinessesAgentUrl(businessDetailsFrontendEnabled)
    else
      businessDetailsManageBusinessesIndividualUrl(businessDetailsFrontendEnabled)
  
  //Returns routes

  lazy val returnsBaseUrl: String = servicesConfig.getString("income-tax-returns-frontend.baseUrl")
  lazy val returnsAgentBaseUrl: String = s"$returnsBaseUrl/agents"


  lazy val returnsTaxYearsIndividualUrl: Boolean => String = returnsFrontendEnabled =>
    if (returnsFrontendEnabled)
      s"$returnsBaseUrl/tax-years"
    else
      s"$vcFrontendBaseUrl/tax-years"

  lazy val returnsTaxYearsAgentUrl: Boolean => String = returnsFrontendEnabled =>
    if (returnsFrontendEnabled)
      s"$returnsAgentBaseUrl/tax-years"
    else
      s"$vcFrontendAgentBaseUrl/tax-years"

  def returnsTaxYearsUrl(isAgent: Boolean, returnsFrontendEnabled: Boolean): String =
    if (isAgent)
      returnsTaxYearsAgentUrl(returnsFrontendEnabled)
    else
      returnsTaxYearsIndividualUrl(returnsFrontendEnabled)

}
