package com.demo.myretail.repository

import com.demo.myretail.entity.ProductKey
import com.demo.myretail.entity.ProductCurrency
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductDetailsRepository : ReactiveCrudRepository<ProductCurrency, ProductKey> {
}