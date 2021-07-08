package com.demo.myretail.controller

import com.demo.myretail.common.API_PREFIX
import com.demo.myretail.common.PRODUCTS
import com.demo.myretail.common.PRODUCT_ID
import com.demo.myretail.entity.ProductCurrency
import com.demo.myretail.request.ProductDetailRequest
import com.demo.myretail.response.ProductDetails
import com.demo.myretail.service.ProductDetailsService
import mu.KotlinLogging
import javax.validation.constraints.Size
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.function.Supplier
import javax.validation.Valid


/*  Controller method for api calls entry point.
 *
 */

@RestController
@RequestMapping(API_PREFIX)
class MyRetailController (val productDetailsService : ProductDetailsService) {

    private val logger = KotlinLogging.logger {}

    /*
     *  Takes productId as input and provides Product Details as output
     */


    @GetMapping(PRODUCTS, produces = [(MediaType.APPLICATION_JSON_VALUE)])
    fun getProductDetails(@PathVariable(PRODUCT_ID)
                          @Size(min = 3, max = 8, message = "length between 3 to 8 chars for ProductId")
                          productId: String): Mono<ResponseEntity<ProductDetails>> {

        logger.info { "Get Product Details called for Product Id $productId" }

        var productDetails = productDetailsService.getProductDetails(productId)

        return productDetails.map { ResponseEntity.ok().body(it) }
                .doOnError { ResponseEntity.badRequest() }
                .defaultIfEmpty(ResponseEntity.notFound().build())
    }

    @PutMapping(PRODUCTS, produces = [(MediaType.APPLICATION_JSON_VALUE)])
    fun updateProductDetails(@Size(min = 3, max = 8, message = "length between 3 to 8 chars for ProductId")
                             @PathVariable(PRODUCT_ID) productId: String,
                             @RequestBody @Valid request : ProductDetailRequest): Mono<ResponseEntity<ProductDetails>> {

        logger.info { "Update Product Details called for updating Product $productId with Price ${request.price} and Currency Code ${request.currencyCode}" }

        var productDetails = productDetailsService.updateProductDetails(productId, request)

        return productDetails.map { ResponseEntity.ok().body(it) }
                .doOnError { ResponseEntity.badRequest() }
                .defaultIfEmpty(ResponseEntity.notFound().build())
    }

    @GetMapping("/allProductPrices", produces = [(MediaType.APPLICATION_JSON_VALUE)])
    fun getAllProductPrices(): Flux<ProductCurrency> {

        return productDetailsService.getAllProductPrices()
    }
}

