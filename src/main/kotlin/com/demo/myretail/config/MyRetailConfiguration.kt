package com.demo.myretail.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers
import java.util.concurrent.Executors

@Configuration
@EnableScheduling
class MyRetailConfiguration (var appConfiguration: ApplicationConfiguration){

    @Bean("webClient")
    fun getWebClient() : WebClient {
        return WebClient.create()
    }

    @Bean("scheduler")
    fun getScheduler() : Scheduler {
        return Schedulers.fromExecutorService(Executors.newFixedThreadPool(appConfiguration.threadCount))
    }

}