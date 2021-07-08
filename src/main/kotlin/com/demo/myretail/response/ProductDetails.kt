package com.demo.myretail.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class ProductDetails (var id: String,
                           var name: String,
                           var tcin: String,
                           var dpci: String,
                           @JsonProperty("product_price") var productPrice : ProductPrice)
