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

package obligations.views.reportingObligations.optOut

import common.auth.MtdItUser
import common.config.FrontendAppConfig
import common.models.incomeSourceDetails.TaxYear
import common.testUtils.TestSupport
import obligations.models.reportingObligations.optOut.{ConfirmedOptOutViewModel, DefaultValidScenario}
import obligations.views.html.reportingObligations.optOut.ConfirmedOptOutView
import obligations.views.messages.ConfirmedOptOutMessages as viewMessages
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.test.Helpers.*

class ConfirmedOptOutUpdateViewSpec extends TestSupport {

  lazy val mockAppConfig: FrontendAppConfig = app.injector.instanceOf[FrontendAppConfig]
  val confirmedOptOutView: ConfirmedOptOutView = app.injector.instanceOf[ConfirmedOptOutView]

  class Setup(isAgent: Boolean = true) {
    implicit val testUser: MtdItUser[?] = if (isAgent) agentUserConfirmedClient() else individualUser
    val reportingObligationsURL: String = obligations.controllers.reportingObligations.routes.ReportingFrequencyPageController.show(isAgent).url

    val pageDocument: Document = {
      Jsoup.parse(
        contentAsString(
          confirmedOptOutView(
            viewModel = ConfirmedOptOutViewModel(TaxYear(2025,2026),None),
            confirmedOptOutViewScenarios = DefaultValidScenario,
              selfAssessmentTaxReturnLink = mockAppConfig.logInFileSelfAssessmentTaxReturnLink,
            compatibleSoftwareLink = mockAppConfig.compatibleSoftwareLink
          )
        )
      )
    }
  }

  "check opt-out confirmation  view" when {

    "render the page" should {

      "user is agent" in new Setup(isAgent = true) {
        pageDocument.title() shouldBe viewMessages.title

        pageDocument.getElementById("out-opt-complete-green-panel").text() shouldBe viewMessages.heading

        pageDocument.getElementById("submit-tax-return-bullet-2").text() shouldBe viewMessages.submitTaxBullet2

      }

      "user is not agent" in new Setup(isAgent = false) {
        pageDocument.title() shouldBe viewMessages.title

        pageDocument.getElementById("out-opt-complete-green-panel").text() shouldBe viewMessages.heading

        pageDocument.getElementById("submit-tax-return-bullet-2").text() shouldBe viewMessages.submitTaxBullet2

      }
    }
  }
}
