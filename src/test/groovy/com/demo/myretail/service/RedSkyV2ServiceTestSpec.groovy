package com.demo.myretail.service


import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomjankes.wiremock.WireMockGroovy
import com.demo.myretail.config.ApplicationConfiguration
import com.demo.myretail.response.Item
import com.demo.myretail.response.Product
import com.demo.myretail.response.ProductDescription
import com.demo.myretail.response.RedSkyV2Response
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier
import spock.lang.Specification
import org.junit.Rule

class RedSkyV2ServiceTestSpec extends Specification {

    @Rule
    WireMockRule wireMockRule = new WireMockRule(8080)

    WireMockGroovy wireMockStub = new WireMockGroovy()

    def webClient = WebClient.builder()
            .baseUrl("http://localhost:8080")
            .build()

    def applicationConfig = new com.demo.myretail.config.ApplicationConfiguration()

    def redSkyV2Service = new RedSkyV2Service(webClient, applicationConfig)

    def "test GetProductDetails fetches details from RedSky Api and returns Product details on Success"() {

        given:
        def productId = "51509273"
        def dpci = "030-09-4477"
        def upc = "816445020035"
        def productName = "Mifold Grab-N-Go Booster Car Seat - Slate Gray"

        def redskyV2Response = new com.demo.myretail.response.RedSkyV2Response(new com.demo.myretail.response.Product(new com.demo.myretail.response.Item(productId, dpci, upc, new com.demo.myretail.response.ProductDescription(productName))) )

        wireMockStub.stub {
            request {
                method "GET"
                url "/pdp/tcin/$productId"
            }
            response {
                status 200
                body """{
                        "product": {
                            "item": { 
                                "tcin": $productId,
                                "dpci": $dpci,
                                "upc": $upc,
                                "product_description": {
                                    "title": $productName
                                }
                            }
                        }
                    }
                 """
                headers { "Content-Type" "application/json" }
            }
        }

        when:

        def response = redSkyV2Service.getProductDetails(productId)


        then:
        StepVerifier.create(response).expectNext(redskyV2Response).verifyComplete()
    }

    def "test GetProductDetails get error and returns error response"() {

        given:
        def productId = "515091111"

        wireMockStub.stub {
            request {
                method "GET"
                url "/pdp/tcin/$productId"
            }
            response {
                status 404
                headers { "Content-Type" "application/json" }
            }
        }

        when:

        def response = redSkyV2Service.getProductDetails(productId)

        then:
        StepVerifier.create(response).expectError()
    }
}
