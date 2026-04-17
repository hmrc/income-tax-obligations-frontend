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

package uk.gov.hmrc.incometaxobligationsfrontend.testUtils

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.scalatest.Assertion
import play.twirl.api.Html
import play.twirl.api.TwirlHelperImports.twirlJavaCollectionToScala

import scala.language.implicitConversions

trait ViewSpec extends TestSupport {

  class Setup(page: Html) {
    val document: Document = Jsoup.parse(page.body)
    lazy val content: Element = document.selectHead("#content")
    implicit lazy val layoutContent: Element = document.selectHead("#main-content")

    def pageContent(pageContentSelector: String) = document.selectHead(pageContentSelector)

    def findElementById(id: String): Option[Element] = {
      Option(document.getElementById(id))
    }

    def paragraphText: String = document.select("p").text()
    def panelText: String = document.getElementsByClass("govuk-panel").text()
    def firstH2Text: String = layoutContent.getElementsByTag("h2").first().text()
  }

  object Selectors {
    val h1: String = "h1"
    val h2: String = "h2"
    val h3: String = "h3"
    val backLink: String = id("back")
    val backToHome: String = "back"
    val form: String = "form"
    val summaryError: String = "#error-summary-display ul a"
    val inputError: String = ".error-notification"
    val link: String = "a"
    val table: String = "table"
    val tableRow: String = "tr"
    val div: String = "div"
    val p: String = "p"

    def id(id: String): String = s"#$id"
  }

  implicit class CustomSelectors(element: Element) {

    def selectHead(selector: String): Element = element.select(selector).toList.headOption match {
      case Some(element) => element
      case None => fail(s"$selector not found")
    }

    def selectNth(selector: String, nth: Int): Element = element.selectHead(s"$selector:nth-of-type($nth)")

    def getOptionalSelector(selector: String): Option[Element] = element.select(selector).toList.headOption

    //scalastyle:off
    def h1: Element = {
      Option(element.select(Selectors.h1).first())
        .getOrElse(fail("h1 not found"))
    }

    def h2: Element = {
      Option(element.select(Selectors.h2).first())
        .getOrElse(fail("h2 not found"))
    }

    def h3: Element = {
      Option(element.select(Selectors.h3).first())
        .getOrElse(fail("h3 not found"))
    }

    def backLink: Element = {
      Option(element.select(Selectors.backLink).first())
        .getOrElse(fail("back link not found"))
    }

    def backToHome: Element = {
      element.selectById(Selectors.backToHome)
    }

    def form: Element = {
      Option(element.select(Selectors.form).first())
        .getOrElse(fail("form not found"))
    }

    def summaryError: Element = {
      Option(element.select(Selectors.summaryError).first())
        .getOrElse(fail("error summary list item not found"))
    }

    def inputError: Element = {
      Option(element.select(Selectors.inputError).first())
        .getOrElse(fail(s"input error not found"))
    }

    def link: Element = {
      Option(element.select(Selectors.link).first())
        .getOrElse(fail("link element not found"))
    }

    def selectById(id: String): Element = {
      Option(element.select(Selectors.id(id)).first())
        .getOrElse(fail("element with id not found"))
    }

    def table(nthOfType: Int = 1): Element = {
      Option(element.select(s"${Selectors.table}:nth-of-type($nthOfType)").first())
        .getOrElse(fail("table element not found"))
    }

    def breadcrumbNav: Element = {
      element.selectHead("#breadcrumbs")
    }
  }

  implicit class ElementTests(element: Element) {
    def hasPageHeading(heading: String): Assertion = element.h1.text shouldBe heading
  }

}
