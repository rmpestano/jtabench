package org.apacheextras.eebench.jtabench.jta

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class JTASimulation extends Simulation {


  var totalUsersPerScenario = 50
  var initialUsersPerScenario = 1
  var scenarioDurationInSeconds = 25 //2 user per second
  var expectedMaxResponseTime = 100
  var expectedMeanResponseTime = 20
  var expectedRequestPerSecond = 35


  if (System.getProperty("torture") != null) {
    println("torture mode on!")
    totalUsersPerScenario = 150 //x2 scenario = 300 simultaneous users
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


  val jtaRequest = http("jta request")
    .get("jta")
    .check(status.in(200),okCheck)

  val nonJtaRequest = http("non jta request")
    .get("nonjta")
    .check(status.in(200),okCheck)

  val resetRequest = http("reset request")
    .get("reset")
    .check(status.in(200),okCheck)




  val jtaScenario = scenario("jta scenario")
    .exec(jtaRequest)
    .pause(50 milliseconds)// users don't click buttons at the same time


  val nonJtaScenario = scenario("non jta scenario")
    .exec(nonJtaRequest)
    .pause(50 milliseconds)

  val resetScenario = scenario("reset scenario")
    .exec(resetRequest)
    .pause(10 seconds)


  setUp(
    jtaScenario.inject(
      rampUsersPerSec(initialUsersPerScenario) to (totalUsersPerScenario) during(scenarioDurationInSeconds seconds)
     ),
    nonJtaScenario.inject(
      rampUsersPerSec(initialUsersPerScenario) to (totalUsersPerScenario) during(scenarioDurationInSeconds seconds)
    ),
     resetScenario.inject(
       constantUsersPerSec(1) during (scenarioDurationInSeconds seconds)
    )

  )
    .protocols(httpProtocol)
    .assertions(
      global.successfulRequests.percent.greaterThan(95),
      global.responseTime.max.lessThan(expectedMaxResponseTime),
      global.responseTime.mean.lessThan(expectedMeanResponseTime),
      global.requestsPerSec.greaterThan(expectedRequestPerSecond),
      details("jta request").requestsPerSec.greaterThan(expectedRequestPerSecond/2),
      details("non jta request").requestsPerSec.greaterThan(expectedRequestPerSecond/2)

    )

}