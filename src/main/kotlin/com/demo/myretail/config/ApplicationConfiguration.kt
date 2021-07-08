package com.demo.myretail.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component



@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app")
@Component
class ApplicationConfiguration {

    var redSkyBaseUrl = "https://redsky.target.com/v3/pdp/tcin/"
    set(value) {
        field = value
    }

    var threadCount : Int = 5

}