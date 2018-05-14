package com.sap.ibso.mindstorm.reservationclient

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Bean
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate


@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
@EnableFeignClients
@EnableCircuitBreaker
class ReservationClientApplication {

    @Bean
    @LoadBalanced
    fun restTemplate(): RestTemplate = RestTemplate()
}

fun main(args: Array<String>) {
    runApplication<ReservationClientApplication>(*args)
}

@FeignClient("reservation-service")
interface ReservationReader {

    @GetMapping("/reservations")
    fun getReservations(): List<Reservation>

}

@RestController
@RequestMapping("/reservations")
class ReservationApiGateway(/*val restTemplate: RestTemplate*/val reader: ReservationReader){

    fun getReservationNamesFallback(): List<Reservation?> = emptyList()

    @HystrixCommand(fallbackMethod = "getReservationNamesFallback")
    @GetMapping("/names")
    fun getNames(): List<Reservation?> = reader.getReservations()

//    @GetMapping("/names")
//    fun getReservationNames():ResponseEntity<List<Reservation>> {
//        val ptr = object : ParameterizedTypeReference<List<Reservation>>() {}
//
//        return this.restTemplate.exchange("http://reservation-service/reservations",
//                HttpMethod.GET, null, ptr)
//    }
}

data class Reservation(var id: Long?, var reservationName: String?) {
    constructor() : this(null, null)
}