package com.demo.myretail.service

import com.demo.myretail.common.ProductDetailsException
import com.demo.myretail.config.ApplicationConfiguration
import com.demo.myretail.response.RedSkyV2Response
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class RedSkyV2Service (private val redSkyWebClient: WebClient, private val appConfiguration: ApplicationConfiguration) {

    private val logger = KotlinLogging.logger {}


    fun getProductDetails(productId: String) : Mono<RedSkyV2Response> {
        val redSkyUrl =   appConfiguration.redSkyBaseUrl + productId + "?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics&key=candidate"

        logger.info { "Calling RedSky with URL $redSkyUrl" }

        return redSkyWebClient.get().uri(redSkyUrl).retrieve()
                .onStatus(HttpStatus::is5xxServerError){Mono.error(ProductDetailsException("Could not fetch product details from RedSky", HttpStatus.BAD_REQUEST))}
                .onStatus(HttpStatus::is4xxClientError) {Mono.error( ProductDetailsException("Product details not found", HttpStatus.NOT_FOUND))}
                .bodyToMono(RedSkyV2Response::class.java)
    }
}