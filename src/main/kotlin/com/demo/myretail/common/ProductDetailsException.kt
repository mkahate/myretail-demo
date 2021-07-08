package com.demo.myretail.common

import org.springframework.http.HttpStatus


data class ProductDetailsException (override val message : String, val exception : HttpStatus) : RuntimeException(message)