package com.demo.myretail.service

import com.demo.myretail.common.ProductDetailsException
import com.demo.myretail.entity.ProductCurrency
import com.demo.myretail.entity.ProductKey
import com.demo.myretail.repository.ProductDetailsRepository
import com.demo.myretail.request.ProductDetailRequest
import com.demo.myretail.response.ProductDetails
import com.demo.myretail.response.ProductPrice
import com.demo.myretail.response.RedSkyV2Response
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler

@Service
class ProductDetailsServiceImpl (var productDetailsRepository : ProductDetailsRepository,
                                 var redSkyV2Service: RedSkyV2Service,
                                 val scheduler: Scheduler) : ProductDetailsService {

    private val logger = KotlinLogging.logger {}

     override fun getProductDetails(productId: String, currencyCode : String): Mono<ProductDetails> {

        logger.info { "service called to fetch Product Details for product $productId with currency code $currencyCode" }

         val productDetails = redSkyV2Service.getProductDetails(productId).subscribeOn(scheduler)
         val productPrice = productDetailsRepository.findById(ProductKey(productId, currencyCode)).subscribeOn(scheduler)

        return getProductDetails(productPrice, productDetails)
    }

    override fun updateProductDetails(productId : String, request : ProductDetailRequest) : Mono<ProductDetails> {

        logger.info { "service called to update Product Details for product $productId with price ${request.price}" }
        val productKey = ProductKey(productId = productId, currencyCode = request.currencyCode.toString())
        val insertData = ProductCurrency(productKey, request.price)

        val productDetails = redSkyV2Service.getProductDetails(productId).subscribeOn(scheduler)
        val productPrice = productDetails
                .switchIfEmpty(Mono.error( ProductDetailsException("Product details not found", HttpStatus.NOT_FOUND)))
                .then(productDetailsRepository.save(insertData).subscribeOn(scheduler).switchIfEmpty(
                        Mono.error( ProductDetailsException("Failed to update Price", HttpStatus.BAD_REQUEST))
                ).then(productDetailsRepository.findById(productKey).subscribeOn(scheduler)))

        return getProductDetails(productPrice, productDetails)
    }

    private fun getProductDetails(productPrice: Mono<ProductCurrency>, productDetails: Mono<RedSkyV2Response>): Mono<ProductDetails> {
        return Mono.zip(productPrice, productDetails).map {
            ProductDetails(
                    id = it.t1.key.productId,
                    name = it.t2.product.item.productDescription.title,
                    tcin = it.t2.product.item.tcin,
                    dpci = it.t2.product.item.dpci,
                    productPrice = ProductPrice(
                            value = it.t1.value,
                            currencyCode = it.t1.key.currencyCode
                    )
            )
        }
    }

    override fun getAllProductPrices() : Flux<ProductCurrency> {

        return productDetailsRepository.findAll();
    }
}