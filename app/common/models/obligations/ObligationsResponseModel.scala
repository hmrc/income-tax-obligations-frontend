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

package common.models.obligations

import common.auth.MtdItUser
import common.models.incomeSourceDetails.QuarterTypeElection.orderingByTypeName
import common.models.incomeSourceDetails.{PropertyDetailsModel, QuarterReportingType, QuarterTypeCalendar, QuarterTypeStandard}
import play.api.libs.json.*

import java.time.LocalDate

sealed trait ObligationsResponseModel

case class ObligationsModel(obligations: Seq[GroupedObligationsModel]) extends ObligationsResponseModel {

  def allDeadlinesWithSource(previous: Boolean = false, hideUnknownBusinessName: Boolean)(implicit mtdItUser: MtdItUser[_]): Seq[ObligationWithIncomeType] = {
    val deadlines = obligations.flatMap { groupedObligationsModel =>
      mtdItUser.incomeSources.properties.find(_.incomeSourceId == groupedObligationsModel.identification) match {
        case Some(property) if property.incomeSourceType.contains("foreign-property") =>
          groupedObligationsModel.obligations.map(deadline => Some(ObligationWithIncomeType(s"nextUpdates.r17.tab.quarterly.table.income.source.foreign", deadline)))
        case Some(property) if property.incomeSourceType.contains("uk-property") =>
          groupedObligationsModel.obligations.map(deadline => Some(ObligationWithIncomeType(s"nextUpdates.r17.tab.quarterly.table.income.source.uk", deadline)))
        case Some(_: PropertyDetailsModel) =>
          groupedObligationsModel.obligations.map(deadline => Some(ObligationWithIncomeType(s"nextUpdates.r17.tab.quarterly.table.income.source.property", deadline)))
        case _ =>
          if (mtdItUser.incomeSources.businesses.exists(_.incomeSourceId == groupedObligationsModel.identification)) groupedObligationsModel.obligations.map {
            deadline =>
              Some(ObligationWithIncomeType(
                incomeType = mtdItUser.incomeSources.businesses
                  .find(_.incomeSourceId == groupedObligationsModel.identification)
                  .get
                  .tradingName
                  .getOrElse(getUnknownBusinessName(hideUnknownBusinessName)),
                obligation = deadline
              )
              )
          } else if (groupedObligationsModel.obligations.forall(ob => ob.obligationType == "Crystallisation"))
            groupedObligationsModel.obligations.map {
              deadline => Some(ObligationWithIncomeType("nextUpdates.crystallisedAll", deadline))
            } else None
      }
    }.flatten

    if (previous) deadlines.sortBy(_.obligation.dateReceived.map(_.toEpochDay)).reverse else deadlines.sortBy(_.obligation.due.toEpochDay)
  }

  def obligationsByDueDateAndStartDate(hideBusinessName: Boolean)(implicit mtdItUser: MtdItUser[_]): Seq[(LocalDate, Seq[ObligationWithIncomeType])] = {
    val groupedByDueDateAndStartDate = allDeadlinesWithSource(hideUnknownBusinessName = hideBusinessName)
      .groupBy(obligation => (obligation.obligation.due, obligation.obligation.start))

    groupedByDueDateAndStartDate.toList
      .sortBy { case ((due, start), _) => (due, start) }
      .map({ case ((dueDate, startDate), obligations) => (dueDate, obligations) })
  }

  def quarterlyUpdatesCounts(hideBusinessName: Boolean)(implicit mtdItUser: MtdItUser[_]): Int =
    allDeadlinesWithSource(hideUnknownBusinessName = hideBusinessName)(mtdItUser)
      .filter(_.obligation.obligationType == "Quarterly")
      .count(_.obligation.status == StatusFulfilled)

  def getPeriodForQuarterly(obligation: ObligationWithIncomeType): QuarterReportingType = {
    val dayOfMonth = obligation.obligation.start.getDayOfMonth
    if (dayOfMonth < 6) QuarterTypeCalendar else QuarterTypeStandard
  }

  def groupByQuarterPeriod(obligations: Seq[ObligationWithIncomeType]): Map[Option[QuarterReportingType], Seq[ObligationWithIncomeType]] = {
    obligations.groupBy { obligation =>
        obligation.obligation.obligationType match {
          case "Quarterly" => Some(getPeriodForQuarterly(obligation))
          case _ => None //"Default"
        }
      }.view
      .mapValues(_.sortBy(_.obligation.start))
      .toSeq
      .sortBy { case (reportingType, _) => reportingType } // Sort by QuarterlyReportingType STANDARD or CALENDAR
      .map { case (period, obligations) =>
        // Sort obligations within each period by start date
        val sortedObligations = obligations.sortBy(_.obligation.start)
        (period, sortedObligations)
      }
      .toMap
  }
  
  private def getUnknownBusinessName(hideBusinessName: Boolean): String = {
    if (hideBusinessName) "" else "nextUpdates.business"
  }

}

object ObligationsModel {
  implicit val format: OFormat[ObligationsModel] = Json.format[ObligationsModel]
}

case class ObligationWithIncomeType(incomeType: String, obligation: SingleObligationModel)

case class ObligationsErrorModel(code: Int, message: String) extends ObligationsResponseModel

object ObligationsErrorModel {
  implicit val format: Format[ObligationsErrorModel] = Json.format[ObligationsErrorModel]
}
