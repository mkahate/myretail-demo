package com.demo.myretail.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ProductPrice (@JsonProperty("currency_code") val currencyCode: String,
                         val value: Float)