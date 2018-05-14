package com.sap.ibso.mindstorm.hystrixdashboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard

@SpringBootApplication
@EnableHystrixDashboard
@EnableDiscoveryClient
class HystrixDashboardApplication

fun main(args: Array<String>) {
    runApplication<HystrixDashboardApplication>(*args)
}
