package com.demo.myretail.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.demo.myretail.common.CurrencyCode
import javax.validation.Valid
import javax.validation.constraints.Digits

data class ProductDetailRequest (@Digits(fraction = 2, integer = 10, message = "Proper product price") val price : Float, @JsonProperty("currency_code") val currencyCode : CurrencyCode)