package com.demo.myretail.service

import com.demo.myretail.common.CurrencyCode
import com.demo.myretail.entity.ProductCurrency
import com.demo.myretail.request.ProductDetailRequest
import com.demo.myretail.response.ProductDetails
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProductDetailsService {

    fun getProductDetails(productId : String, currencyCode : String = CurrencyCode.USD.toString()) : Mono<ProductDetails>

    fun updateProductDetails(productId : String, request : ProductDetailRequest) : Mono<ProductDetails>

    fun getAllProductPrices() : Flux<ProductCurrency>
}