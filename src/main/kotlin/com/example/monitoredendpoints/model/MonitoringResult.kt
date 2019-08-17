package com.example.monitoredendpoints.model

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
@Table(name = "monitoring_result")
data class MonitoringResult(
        @Id
        @GeneratedValue
        @Type(type="uuid-char")
        var id: UUID,

        @Column(name = "check_time")
        var checkTime: LocalDateTime,

        @Column(name = "status_code")
        var statusCode: Int,

        @Column(name = "response_body")
        var responseBody: String,

        @ManyToOne(cascade = [DETACH, MERGE, PERSIST, REFRESH])
        @JoinColumn(name = "fk_endpoint_id")
        var monitoredEndpoint: MonitoredEndpoint
)