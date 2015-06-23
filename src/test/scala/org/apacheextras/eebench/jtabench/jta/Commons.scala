/**
 * Created by pestano on 15/05/15.
 */
package org.apacheextras.eebench.jtabench.jta

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object Commons {

  var totalUsersPerScenario = 60
  var initialUsersPerScenario = 1
  var scenarioDurationInSeconds = 30 //2 user per second
  var expectedMaxResponseTime = 1000
  var expectedMeanResponseTime = 30
  var expectedRequestPerSecond = 20


  if (System.getProperty("torture") != null) {
    println("torture mode on!")
    totalUsersPerScenario = 200
    initialUsersPerScenario = 1
    scenarioDurationInSeconds = 400
    expectedMaxResponseTime = 800 //because of too high concurrency some requests take longer
    expectedMeanResponseTime = 25 //mean is lower because of caches(JPA, rest, etc...)
    expectedRequestPerSecond = 120 // 6000 req per minute
  }


  val httpProtocol = http
    .baseURL("http://localhost:8080/jtabench/customer/")
    .acceptHeader("text/html,application/xhtml+xml,text/xml, application/xml;q=0.9,*/*;q=0.01")
    .acceptEncodingHeader("gzip, deflate")
    .inferHtmlResources()
    .connection( """keep-alive""")
    .contentTypeHeader("*/*")
    .acceptLanguageHeader("pt-BR,pt;q=0.8,en-US;q=0.5,en;q=0.3")
    .userAgentHeader("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:36.0) Gecko/20100101 Firefox/36.0")

  val okCheck = regex("OK")

  val resetRequest = http("reset request")
    .get("reset")
    .check(status.in(200),okCheck)




}
