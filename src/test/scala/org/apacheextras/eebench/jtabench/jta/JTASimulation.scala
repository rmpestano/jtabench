package org.apacheextras.eebench.jtabench.jta

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import org.apacheextras.eebench.jtabench.jta.Commons._

class JTASimulation extends Simulation {



  val jtaRequest = http("jta request")
    .get("jta")
    .check(status.in(200),okCheck)


  val jtaScenario = scenario("jta scenario")
    .exec(jtaRequest)
    .pause(50 milliseconds)// users don't click buttons at the same time

  val resetScenario = scenario("reset scenario")
    .exec(resetRequest)
    .pause(10 seconds)


  setUp(
    jtaScenario.inject(
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
      global.requestsPerSec.greaterThan(expectedRequestPerSecond)
     /* details("jta request").requestsPerSec.greaterThan(expectedRequestPerSecond/2),
      details("non jta request").requestsPerSec.greaterThan(expectedRequestPerSecond/2)*/

    )

}