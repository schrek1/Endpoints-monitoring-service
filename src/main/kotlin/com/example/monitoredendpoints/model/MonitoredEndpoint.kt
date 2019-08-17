package com.example.monitoredendpoints.model

import com.example.monitoredendpoints.model.enum.*
import com.fasterxml.jackson.annotation.*
import io.swagger.annotations.*
import org.hibernate.annotations.*
import java.time.*
import java.util.*
import javax.persistence.*
import javax.persistence.CascadeType.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "monitored_endpoint")
data class MonitoredEndpoint(
        @Id
        @GeneratedValue
        @Type(type = "uuid-char")
        var id: UUID,

        var name: String,

        var url: String,

        var operation: RestOperation,

        var creation: LocalDateTime,

        var lastCheck: LocalDateTime,

        var monitoringInterval: Int,

        @ManyToMany(cascade = [DETACH, MERGE, PERSIST, REFRESH])
        @JoinTable(name = "user_endpoint", joinColumns = [JoinColumn(name = "fk_monitoring_endpoint_id")], inverseJoinColumns = [JoinColumn(name = "fk_user_id")])
        var owners: MutableList<User>

) {
    @OneToMany(mappedBy = "monitoredEndpoint")
    val monitoringResults: MutableList<MonitoringResult> = mutableListOf()
}