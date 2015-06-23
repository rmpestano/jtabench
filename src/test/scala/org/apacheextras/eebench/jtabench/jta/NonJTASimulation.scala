package org.apacheextras.eebench.jtabench.jta

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.apacheextras.eebench.jtabench.jta.Commons._
import scala.concurrent.duration._

class NonJTASimulation extends Simulation {



   val nonJtaRequest = http("non jta request")
     .get("nonjta")
     .check(status.in(200),okCheck)


   val nonJtaScenario = scenario("non jta scenario")
     .exec(nonJtaRequest)
     .pause(50 milliseconds)

   val resetScenario = scenario("reset scenario")
     .exec(resetRequest)
     .pause(10 seconds)


   setUp(
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
       global.requestsPerSec.greaterThan(expectedRequestPerSecond)
      /* details("jta request").requestsPerSec.greaterThan(expectedRequestPerSecond/2),
       details("non jta request").requestsPerSec.greaterThan(expectedRequestPerSecond/2)*/

     )

 }