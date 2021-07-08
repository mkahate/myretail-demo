package com.demo.myretail.controller


import com.demo.myretail.common.CurrencyCode
import com.demo.myretail.common.ProductDetailsException
import com.demo.myretail.entity.ProductCurrency
import com.demo.myretail.entity.ProductKey
import com.demo.myretail.request.ProductDetailRequest
import com.demo.myretail.response.ProductDetails
import com.demo.myretail.response.ProductPrice
import com.demo.myretail.service.ProductDetailsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification
import spock.lang.Unroll


class MyRetailControllerTestSpec extends Specification {

    def productDetailsService = Mock(com.demo.myretail.service.ProductDetailsService)

    def myRetailController = new MyRetailController(productDetailsService)

    def "test GetProductDetails fetches product details with price"() {

        given:
        def productId = "51509273"
        def productPrice = new com.demo.myretail.response.ProductPrice("USD", 10.99f)
        def productDetails = new com.demo.myretail.response.ProductDetails(productId, "Mifold Grab-N-Go Booster Car Seat - Slate Gray", "51509273", "030-09-4477", productPrice )

        when:
        Mono<ResponseEntity<com.demo.myretail.response.ProductDetails>> response = myRetailController.getProductDetails(productId)

        then:
        myRetailController.productDetailsService.getProductDetails(productId, com.demo.myretail.common.CurrencyCode.USD.toString()) >> Mono.just(productDetails)

        StepVerifier.create(response).expectNext(new ResponseEntity<com.demo.myretail.response.ProductDetails>(
                productDetails,
                null,
                HttpStatus.OK
        )).verifyComplete()
    }

    @Unroll
    def "test UpdateProductDetails update the product price:#price and returns with updated price"() {

        given:
        def productId = "51509211"
        def productDetailRequest =  new com.demo.myretail.request.ProductDetailRequest(price, com.demo.myretail.common.CurrencyCode.USD)
        def productPrice = new com.demo.myretail.response.ProductPrice("USD", price)
        def productDetails = new com.demo.myretail.response.ProductDetails(productId, "Mifold Grab-N-Go Booster Car Seat - Slate Gray", "51509211", "030-09-4477", productPrice )

        when:
        Mono<ResponseEntity<com.demo.myretail.response.ProductDetails>> response = myRetailController.updateProductDetails(productId, productDetailRequest)

        then:
        myRetailController.productDetailsService.updateProductDetails(productId, productDetailRequest) >> Mono.just(productDetails)

        StepVerifier.create(response).expectNext(new ResponseEntity<com.demo.myretail.response.ProductDetails>(
                productDetails,
                null,
                HttpStatus.OK
        )).verifyComplete()

        where:
        price << [10.99, 20.99, 30.99]

    }


    def "test GetProductDetails returns error for product not found"() {

        given:
        def productId = "51509214"
        def exception = new com.demo.myretail.common.ProductDetailsException("Could not fetch product details from RedSky", HttpStatus.BAD_REQUEST)

        when:
        Mono<ResponseEntity<com.demo.myretail.response.ProductDetails>> response = myRetailController.getProductDetails(productId)

        then:
        myRetailController.productDetailsService.getProductDetails(productId, com.demo.myretail.common.CurrencyCode.USD.toString()) >> Mono.error(exception)

        StepVerifier.create(response).expectError(com.demo.myretail.common.ProductDetailsException).verify()
    }
}
