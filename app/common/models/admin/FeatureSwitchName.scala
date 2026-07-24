/*
 * Copyright 2024 HM Revenue & Customs
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

package common.models.admin

import play.api.Logger
import play.api.libs.json._
import play.api.mvc.PathBindable

import scala.collection.immutable

case class FeatureSwitch(name: FeatureSwitchName, isEnabled: Boolean)

object FeatureSwitch {
  implicit val format: OFormat[FeatureSwitch] = Json.format[FeatureSwitch]
}

sealed trait FeatureSwitchName {
  val name: String
}

object FeatureSwitchName {

  implicit val writes: Writes[FeatureSwitchName] = (o: FeatureSwitchName) => JsString(o.name)

  implicit val reads: Reads[FeatureSwitchName] = {
    case JsString(OptOutFs.name) =>
      JsSuccess(OptOutFs)
    case JsString(SignUpFs.name) =>
      JsSuccess(SignUpFs)
    case JsString(`CY+1YouMustWaitToSignUpPageEnabled`.name) =>
      JsSuccess(`CY+1YouMustWaitToSignUpPageEnabled`)
    case JsString(NoIncomeSourcesRedirect.name) =>
      JsSuccess(NoIncomeSourcesRedirect)
    case JsString(ObligationsFrontend.name) =>
      JsSuccess(ObligationsFrontend)
    case JsString(BusinessDetailsFrontend.name) =>
      JsSuccess(BusinessDetailsFrontend)
    case JsString(TriggeredMigration.name) =>
      JsSuccess(TriggeredMigration)
    case JsString(FinancialsFrontend.name) =>
      JsSuccess(FinancialsFrontend)
    case notRequiredFS =>
      Logger("application").debug("Feature switch not required in this service")
      JsSuccess(NotRequiredFS)
  }

  implicit val formats: Format[FeatureSwitchName] =
    Format(reads, writes)

  implicit def pathBindable: PathBindable[FeatureSwitchName] = new PathBindable[FeatureSwitchName] {

    override def bind(key: String, value: String): Either[String, FeatureSwitchName] =
      JsString(value).validate[FeatureSwitchName] match {
        case JsSuccess(name, _) =>
          Right(name)
        case _ =>
          Left(s"The feature switch `$value` does not exist")
      }

    override def unbind(key: String, value: FeatureSwitchName): String =
      value.name
  }

  val allFeatureSwitches: immutable.Set[FeatureSwitchName] =
    Set(
      OptOutFs,
      SignUpFs,
      `CY+1YouMustWaitToSignUpPageEnabled`,
      NoIncomeSourcesRedirect,
      ObligationsFrontend,
      BusinessDetailsFrontend,
      TriggeredMigration,
      FinancialsFrontend
    )

  def get(str: String): Option[FeatureSwitchName] = allFeatureSwitches find (_.name == str)
}

case object OptOutFs extends FeatureSwitchName {
  override val name = "opt-out"
  override val toString = "Opt Out"
}

case object SignUpFs extends FeatureSwitchName {
  override val name = "sign-up"
  override val toString = "Sign Up"
}

case object `CY+1YouMustWaitToSignUpPageEnabled` extends FeatureSwitchName {
  override val name: String = "cy-plus-one-you-must-wait-to-sign-up-page-enabled"
  override val toString: String = "CY+1 You Must Wait To Sign Up Page Enabled"
}

case object NoIncomeSourcesRedirect extends FeatureSwitchName {
  override val name: String = "no-income-sources-redirect"
  override val toString: String = "No Income Sources Redirect"
}

case object BusinessDetailsFrontend extends FeatureSwitchName {
  override val name: String = "business-details-frontend"
  override val toString: String = "Business Details Frontend"
}

case object TriggeredMigration extends FeatureSwitchName {
  override val name: String = "triggered-migration"
  override def toString: String = "Triggered Migration"
}

case object NotRequiredFS extends FeatureSwitchName {
  override val name: String = "not-required-FS"
  override val toString: String = "Not required feature Switch"
}

case object FinancialsFrontend extends FeatureSwitchName {
  override val name: String = "financials-frontend"
  override val toString: String = "Financials Frontend"
}

case object ObligationsFrontend extends FeatureSwitchName {
  override val name: String = "obligations-frontend"

  override def toString: String = "Obligations Frontend"
}