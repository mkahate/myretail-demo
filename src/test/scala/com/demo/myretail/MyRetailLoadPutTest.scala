import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class MyRetailLoadPutTest extends Simulation {

  val httpConf = http.baseUrl("http://localhost:8080")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .doNotTrackHeader("1")
    .header("Content-Type", "application/json")

   val scnPutProduct = scenario("Put Product Details")
    .feed(csv("products.csv").random)
    .feed(csv("price.csv").random)
    .exec(http("Put Product Details")
      .put("/myretail/v1/products/${productId}") body(StringBody("{\"price\" : ${price},\"currency_code\" : \"USD\"}")))

  setUp(scnPutProduct.inject(rampUsers(500) during(60 seconds)).protocols(httpConf))
}

