package com.sap.ibso.mindstorm.reservationservice

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.annotation.RestResource
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@SpringBootApplication
@EnableDiscoveryClient
class ReservationServiceApplication {

    @Bean
    fun initializer(reservationRepository: ReservationRepository) =
            ApplicationRunner {
                arrayOf("Heinz", "Sven","Alexandra", "Micha", "Bianca", "Volker", "Michael")
                        .forEach { name -> reservationRepository.save(Reservation(reservationName = name)) }

                reservationRepository.findAll().forEach { println(it) }
            }
}

fun main(args: Array<String>) {
    runApplication<ReservationServiceApplication>(*args)
}


@RefreshScope
@RestController
class MessageRestController(@Value("\${message}") m: String) {

    var message: String? = m

    @GetMapping("/message")
    fun message() = this.message

}


@RestController
class ReservationRestController(val reservationRepository: ReservationRepository){

    @GetMapping("/reservations")
    fun getReservations() = reservationRepository.findAll()
}


@RepositoryRestResource
interface ReservationRepository : JpaRepository<Reservation, Long> {

    @RestResource(path = "by-name")
    fun findByReservationName(@Param("rn") rn: String ): Collection<Reservation>
}

@Entity
data class Reservation(@Id
                       @GeneratedValue
                       var id: Long?,
                       var reservationName: String?) {

    // this guly constructor is needed by JPA
    constructor(): this(null, null)

    constructor(reservationName: String?) : this(null, reservationName)
}
