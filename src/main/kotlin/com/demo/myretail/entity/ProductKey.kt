package com.demo.myretail.entity

import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn

import org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass

@PrimaryKeyClass
data class ProductKey(@PrimaryKeyColumn(name = "product_id", type = PARTITIONED)
                 val productId: String,
                 @PrimaryKeyColumn(name = "currency_code")
                 val currencyCode: String) {

}
