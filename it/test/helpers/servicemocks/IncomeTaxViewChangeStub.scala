/*
 * Copyright 2017 HM Revenue & Customs
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

package helpers.servicemocks

import com.github.tomakehurst.wiremock.stubbing.StubMapping
import helpers.WiremockHelper
import models.incomeSourceDetails.IncomeSourceDetailsResponse
import play.api.http.Status
import play.api.libs.json.Json
import models.ObligationsModel

import java.time.LocalDate

object  IncomeTaxViewChangeStub { // scalastyle:off number.of.methods

  // Income Source Details Stubs
  // ===========================
  val incomeSourceDetailsUrl: String => String = mtditid => s"/income-tax-business-details/income-sources/$mtditid"

  def stubGetIncomeSourceDetailsResponse(mtditid: String)(status: Int, response: IncomeSourceDetailsResponse): StubMapping =
    WiremockHelper.stubGet(incomeSourceDetailsUrl(mtditid), status, response.toJson.toString)

  def verifyGetIncomeSourceDetails(mtditid: String, noOfCalls: Int = 1): Unit = {
    WiremockHelper.verifyGet(incomeSourceDetailsUrl(mtditid), noOfCalls)
  }

  //PreviousObligations Stubs
  def fulfilledObligationsUrl(nino: String): String = {
    s"/income-tax-obligations/$nino/fulfilled-report-deadlines"
  }

  def stubGetFulfilledObligations(nino: String, deadlines: ObligationsModel): Unit =
    WiremockHelper.stubGet(fulfilledObligationsUrl(nino), Status.OK, Json.toJson(deadlines).toString())

  private def allObligationsUrl(nino: String, fromDate: LocalDate, toDate: LocalDate): String =
    s"/income-tax-obligations/$nino/obligations/from/$fromDate/to/$toDate"

  private def obligationsUrl(nino: String): String =
    s"/income-tax-obligations/$nino/open-obligations"

  def stubGetAllObligations(nino: String, fromDate: LocalDate, toDate: LocalDate, deadlines: ObligationsModel): StubMapping =
    WiremockHelper.stubGet(allObligationsUrl(nino, fromDate, toDate), Status.OK, Json.toJson(deadlines).toString())

  def stubGetFulfilledObligationsNotFound(nino: String): Unit =
    WiremockHelper.stubGet(fulfilledObligationsUrl(nino), Status.NOT_FOUND, "")

  def verifyGetObligations(nino: String): Unit =
    WiremockHelper.verifyGet(obligationsUrl(nino))

  //NextUpdates Stubs
  //=====================
  private def nextUpdatesUrl(nino: String): String = s"/income-tax-obligations/$nino/open-obligations"

  def stubGetNextUpdates(nino: String, deadlines: ObligationsModel): Unit =
    WiremockHelper.stubGet(nextUpdatesUrl(nino), Status.OK, Json.toJson(deadlines).toString())

  def verifyGetNextUpdates(nino: String): Unit =
    WiremockHelper.verifyGet(nextUpdatesUrl(nino))
    
}
