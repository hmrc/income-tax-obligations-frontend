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

package testConstants

import testConstants.BusinessDetailsIntegrationTestConstants.*
import testConstants.PropertyDetailsIntegrationTestConstants.*
import enums.IncomeSourceJourney.SelfEmployment
import enums.JourneyType.IncomeSourceJourneyType
import models.incomeSourceDetails.{IncomeSourceDetailsError, IncomeSourceDetailsModel, IncomeSourceDetailsResponse, LatencyDetails}
import testConstants.BaseIntegrationTestConstants.*
import models.incomeSourceDetails.*
import play.api.libs.json.{JsObject, JsValue, Json}
import models.UIJourneySessionData

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object IncomeSourceIntegrationTestConstants {
  val singleBusinessResponse: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(business1),
    properties = Nil,
    yearOfMigration = Some("2018")
  )

  val multipleBusinessesResponse: IncomeSourceDetailsResponse = IncomeSourceDetailsModel(
    nino = testNino,
    mtdbsa = testMtdItId,
    businesses = List(
      business1,
      business2
    ),
    properties = Nil,
    yearOfMigration = Some("2019")
  )


  val businessAndPropertyResponse: IncomeSourceDetailsModel =
    IncomeSourceDetailsModel(
      testNino,
      testMtdItId,
      businesses = List(business1),
      properties = List(property),
      yearOfMigration = Some("2018")
    )

  val businessAndPropertyResponseWoMigration: IncomeSourceDetailsResponse = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(business1),
    properties = List(property),
    yearOfMigration = None
  )

  val multipleBusinessesAndPropertyResponse: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(
      business1,
      business2
    ),
    properties = List(property),
    yearOfMigration = Some("2018")
  )


  val foreignAndSoleTraderCeasedBusiness: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(ceasedBusiness1),
    properties = List(ceasedForeignProperty),
    yearOfMigration = Some("2018")
  )

  val businessWithLatency: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(
      businessWithLatencyForManageYourDetailsAudit
    ),
    properties = List(foreignProperty),
    yearOfMigration = Some("2018")
  )

  val propertyWithLatency: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(business1),
    properties = List(foreignPropertyAudit),
    yearOfMigration = Some("2018")
  )

  val allBusinessesWithLatency: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(businessWithLatencyForManageYourDetailsAudit),
    properties = List(foreignPropertyAudit),
    yearOfMigration = Some("2018")
  )

  val multipleBusinessesAndPropertyResponseWoMigration: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(
      business1,
      business2
    ),
    properties = List(property),
    yearOfMigration = None
  )

  val businessOnlyResponse: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(
      business1
    ),
    properties = List(),
    yearOfMigration = Some("2018")
  )

  val businessOnlyResponseWithLatency: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(
      business1.copy(latencyDetails = Some(testLatencyDetails3))
    ),
    properties = List(),
    yearOfMigration = Some("2018")
  )

  val businessOnlyResponseAllCeased: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(
      ceasedBusiness1
    ),
    properties = List(),
    yearOfMigration = Some("2018")
  )

  val propertyOnlyResponse: IncomeSourceDetailsModel =
    IncomeSourceDetailsModel(
      testNino,
      testMtdItId,
      businesses = List(),
      properties = List(property),
      yearOfMigration = Some("2018")
    )

  val ukPropertyOnlyResponse: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(),
    properties = List(ukProperty),
    yearOfMigration = Some("2018")
  )

  val ukPropertyOnlyResponseWithLatency: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(),
    properties = List(ukProperty.copy(latencyDetails = Some(testLatencyDetails3))),
    yearOfMigration = Some("2018")
  )

  val ukPropertyOnlyResponseAllCeased: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(),
    properties = List(ceasedUkProperty),
    yearOfMigration = Some("2018")
  )

  val foreignPropertyOnlyResponse: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(),
    properties = List(foreignProperty),
    yearOfMigration = Some("2018")
  )

  val foreignPropertyOnlyResponseWithLatency: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(),
    properties = List(foreignProperty.copy(latencyDetails = Some(testLatencyDetails3))),
    yearOfMigration = Some("2018")
  )

  val foreignPropertyOnlyResponseAllCeased: IncomeSourceDetailsModel = IncomeSourceDetailsModel(
    testNino,
    testMtdItId,
    businesses = List(),
    properties = List(ceasedForeignProperty),
    yearOfMigration = Some("2018")
  )

  val noPropertyOrBusinessResponse: IncomeSourceDetailsResponse = IncomeSourceDetailsModel(
    testNino,
    testMtdItId, None,
    List(), Nil
  )
  val errorResponse: IncomeSourceDetailsError = IncomeSourceDetailsError(500, "ISE")
  val testEmptyFinancialDetailsModelJson: JsValue = Json.obj("balanceDetails" -> Json.obj(
    "balanceDueWithin30Days" -> 0.00,
    "overDueAmount" -> 0.00,
    "balanceNotDuein30Days" -> 0.00,
    "totalBalance" -> 0.00
  ), "codingDetails" -> Json.arr(), "documentDetails" -> Json.arr(), "financialDetails" -> Json.arr())

  val noDunningLock: List[String] = List("dunningLock", "dunningLock")
  val oneDunningLock: List[String] = List("Stand over order", "dunningLock")
  val twoDunningLocks: List[String] = List("Stand over order", "Stand over order")
  val noInterestLock: List[String] = List("Interest lock", "Interest lock")
  val twoInterestLocks: List[String] = List("Breathing Space Moratorium Act", "Manual RPI Signal")

  val id1040000123 = "1040000123"

  private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

}


