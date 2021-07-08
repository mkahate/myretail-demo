package com.demo.myretail.controller

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.demo.myretail.common.CurrencyCode
import com.demo.myretail.common.ProductDetailsException
import com.demo.myretail.repository.ProductDetailsRepository
import com.demo.myretail.request.ProductDetailRequest
import com.demo.myretail.response.ProductDetails
import com.demo.myretail.response.ProductPrice
import com.demo.myretail.service.ProductDetailsService
import com.demo.myretail.service.RedSkyV2Service
import junit.framework.TestCase.assertNotNull
import mu.KotlinLogging
import org.junit.ClassRule
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import reactor.core.publisher.Mono


@WebFluxTest(controllers = arrayOf(MyRetailController::class))
@AutoConfigureWebTestClient
class MyRetailControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockBean
    private lateinit var productDetailService : ProductDetailsService

    @MockBean
    private lateinit var redSkyV2Service: RedSkyV2Service

    @MockBean
    private lateinit var productDetailsRepository : ProductDetailsRepository

    private val logger = KotlinLogging.logger {}

    companion object {
        @ClassRule
        @JvmField
        val wireMockRule = WireMockRule(8090)
    }


    private val productDetailsResponseFile = "product_details_response.json"
    private val productDetailsResponse: String? = this::class.java.classLoader.getResource(productDetailsResponseFile)?.readText()

    private val redSkyResponseFile = "redsky_api_response.json"
    private val redSkyResponse: String? = this::class.java.classLoader.getResource(redSkyResponseFile)?.readText()

    @Test
    fun `test that product details response loaded`(){

        assertNotNull(productDetailsResponse)
        assertNotNull(redSkyResponse)
    }

    @Test
    fun `get Product Details response should return 200 with Product details`() {
        var apiUrl = "/myretail/v1/products/"
        var productId = "51509273"

        var productPrice = ProductPrice("USD", 10.99f)
        var productDetails = ProductDetails(id=productId, dpci="030-09-4477", tcin="51509273", name="Mifold Grab-N-Go Booster Car Seat - Slate Gray", productPrice = productPrice )

        given(productDetailService.getProductDetails(productId, "USD"))
                .willReturn(Mono.just(productDetails))

        webTestClient.get().uri(apiUrl+productId).exchange()
                .expectStatus().isOk
                .expectBody().json(productDetailsResponse!!)
    }

    @Test
    fun `get Product Details response should return 5XX api call fails`() {
        var apiUrl = "/myretail/v1/products/"
        var productId = "51509277"

        var exception = ProductDetailsException("Could not fetch product details from RedSky", HttpStatus.BAD_REQUEST)
        given(productDetailService.getProductDetails(productId, "USD"))
                .willReturn(Mono.error(exception))

       webTestClient.get().uri(apiUrl+productId).exchange()
                .expectStatus().is5xxServerError

    }

    @Test
    fun `Update Product Price response should return 200 with Product details`() {
        var apiUrl = "/myretail/v1/products/"
        var productId = "51509273"

        var productDetailsRequest = ProductDetailRequest(price = 10.99f, currencyCode = CurrencyCode.USD)
        var productPrice = ProductPrice("USD", 10.99f)
        var productDetails = ProductDetails(id=productId, dpci="030-09-4477", tcin="51509273", name="Mifold Grab-N-Go Booster Car Seat - Slate Gray", productPrice = productPrice )

        given(productDetailService.updateProductDetails(productId, productDetailsRequest))
                .willReturn(Mono.just(productDetails))

        webTestClient.put().uri(apiUrl+productId)
                .bodyValue(productDetailsRequest)
                .exchange()
                .expectStatus().isOk
                .expectBody().json(productDetailsResponse!!)
    }
}