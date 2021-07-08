package com.demo.myretail.service


import com.demo.myretail.common.CurrencyCode
import com.demo.myretail.entity.ProductCurrency
import com.demo.myretail.entity.ProductKey
import com.demo.myretail.repository.ProductDetailsRepository
import com.demo.myretail.request.ProductDetailRequest
import com.demo.myretail.response.Item
import com.demo.myretail.response.Product
import com.demo.myretail.response.ProductDescription
import com.demo.myretail.response.ProductDetails
import com.demo.myretail.response.ProductPrice
import com.demo.myretail.response.RedSkyV2Response
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.test.StepVerifier
import spock.lang.Specification
import spock.lang.Unroll

class ProductDetailsServiceImplTestSpec extends Specification {

    def productDetailsRepository = Mock(com.demo.myretail.repository.ProductDetailsRepository)
    def redSkyV2Service = Mock(RedSkyV2Service)
    def scheduler = Schedulers.newSingle("1")
    def productDetailsService = new ProductDetailsServiceImpl(productDetailsRepository, redSkyV2Service, scheduler)

    def "test GetProductDetails in Service fetches product details"() {
        given:
        def productId = "51509273"
        def dpci = "030-09-4477"
        def price = 10.99f
        def productName = "Mifold Grab-N-Go Booster Car Seat - Slate Gray"

        def productPrice = new com.demo.myretail.response.ProductPrice("USD", price)
        def productKey = new com.demo.myretail.entity.ProductKey(productId, "USD")
        def productCurrency = new com.demo.myretail.entity.ProductCurrency(productKey, price)
        def redskyV2Response = new com.demo.myretail.response.RedSkyV2Response(new com.demo.myretail.response.Product(new com.demo.myretail.response.Item(productId, dpci, productId, new com.demo.myretail.response.ProductDescription(productName))) )

        def productDetails = new com.demo.myretail.response.ProductDetails(productId, productName, productId, dpci, productPrice )

        when:
        Mono<com.demo.myretail.response.ProductDetails> productDetailsObj = productDetailsService.getProductDetails(productId, "USD")

        then:
        productDetailsService.getProductDetailsRepository().findById(productKey) >> Mono.just(productCurrency)
        productDetailsService.getRedSkyV2Service().getProductDetails(productId) >> Mono.just(redskyV2Response)

        StepVerifier.create(productDetailsObj).expectNext(productDetails).verifyComplete()
    }

    @Unroll
    def "test UpdateProductDetails in Service updates price:#updatePrice and returns product details"() {
        given:
        def productId = "51509273"
        def dpci = "030-09-4477"
        def productName = "Mifold Grab-N-Go Booster Car Seat - Slate Gray"

        def productPrice = new com.demo.myretail.response.ProductPrice("USD", updatePrice)
        def productKey = new com.demo.myretail.entity.ProductKey(productId, "USD")
        def productCurrency = new com.demo.myretail.entity.ProductCurrency(productKey, updatePrice)
        def redskyV2Response = new com.demo.myretail.response.RedSkyV2Response(new com.demo.myretail.response.Product(new com.demo.myretail.response.Item(productId, dpci, productId, new com.demo.myretail.response.ProductDescription(productName))) )

        def productDetailsRequest = new com.demo.myretail.request.ProductDetailRequest(updatePrice, com.demo.myretail.common.CurrencyCode.USD)

        def productDetails = new com.demo.myretail.response.ProductDetails(productId, productName, productId, dpci, productPrice )

        when:
        Mono<com.demo.myretail.response.ProductDetails> productDetailsObj = productDetailsService.updateProductDetails(productId, productDetailsRequest)

        then:
        productDetailsService.getProductDetailsRepository().save(productCurrency) >> Mono.just(productCurrency)
        productDetailsService.getRedSkyV2Service().getProductDetails(productId) >> Mono.just(redskyV2Response)
        productDetailsService.getProductDetailsRepository().findById(productKey) >> Mono.just(productCurrency)
        StepVerifier.create(productDetailsObj).expectNext(productDetails).verifyComplete()

        where:
        updatePrice << [14.99, 23.99, 17.99]
    }

    @Unroll
    def "test update ProductDetails in Service uses getProductDetails with price:#updatePrice to Zip Product Details"() {
        given:
        def productId = "51509273"
        def dpci = "030-09-4477"
        def productName = "Mifold Grab-N-Go Booster Car Seat - Slate Gray"

        def productPrice = new com.demo.myretail.response.ProductPrice("USD", updatePrice)
        def productKey = new com.demo.myretail.entity.ProductKey(productId, "USD")
        def productCurrency = new com.demo.myretail.entity.ProductCurrency(productKey, updatePrice)
        def redskyV2Response = new com.demo.myretail.response.RedSkyV2Response(new com.demo.myretail.response.Product(new com.demo.myretail.response.Item(productId, dpci, productId, new com.demo.myretail.response.ProductDescription(productName))) )

        def productDetailsRequest = new com.demo.myretail.request.ProductDetailRequest(updatePrice, com.demo.myretail.common.CurrencyCode.USD)

        def productDetails = new com.demo.myretail.response.ProductDetails(productId, productName, productId, dpci, productPrice )

        when:
        Mono<com.demo.myretail.response.ProductDetails> productDetailsObj = productDetailsService.updateProductDetails(productId, productDetailsRequest)

        then:
        productDetailsService.getProductDetailsRepository().save(productCurrency) >> Mono.just(productCurrency)
        productDetailsService.getRedSkyV2Service().getProductDetails(productId) >> Mono.just(redskyV2Response)
        productDetailsService.getProductDetailsRepository().findById(productKey) >> Mono.just(productCurrency)
        productDetailsService.getProductDetails(Mono.just(productCurrency), Mono.just(redskyV2Response)) >> Mono.just(productDetails)

        StepVerifier.create(productDetailsObj).expectNext(productDetails).verifyComplete()

        where:
        updatePrice << [19.99, 22.99, 33.99]
    }
}
