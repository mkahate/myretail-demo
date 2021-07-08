package com.demo.myretail.entity


import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table

@Table (value = "product")
data class ProductCurrency(@PrimaryKey var key: ProductKey,
              var value: Float)
