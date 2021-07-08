import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class MyRetailLoadTest extends Simulation {

  val httpConf = http.baseUrl("http://localhost:8080")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .doNotTrackHeader("1")
    .header("Content-Type", "application/json")

  val scnGetProduct = scenario("Get Product Details")
    .feed(csv("products.csv").random)
    .exec(http("Get Product Details")
      .get("/myretail/v1/products/${productId}"))

  setUp(scnGetProduct.inject(rampUsers(500) during(60 seconds)).protocols(httpConf))

}

