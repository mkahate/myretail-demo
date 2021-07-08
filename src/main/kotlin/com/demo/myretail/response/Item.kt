package com.demo.myretail.response

import com.fasterxml.jackson.annotation.JsonProperty

data class Item(var tcin : String, var dpci: String, var upc : String, @JsonProperty("product_description") var productDescription: ProductDescription)