package com.example.monitoredendpoints.model

import org.hibernate.annotations.*
import java.time.*
import java.util.*
import javax.persistence.*
import javax.persistence.CascadeType.*
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.*


@Entity
@Table(name = "monitoring_result")
data class MonitoringResult(
        @Id
        @GeneratedValue
        @Type(type = "uuid-char")
        var id: UUID,

        @Column(name = "check_time")
        @NotNull
        var checkTime: LocalDateTime,

        @Column(name = "status_code")
        @Positive
        var statusCode: Int,

        @Column(name = "response_body")
        @Lob
        var responseBody: String,

        @Column(name = "success")
        var isSuccessful: Boolean,

        @ManyToOne(cascade = [DETACH, MERGE, PERSIST, REFRESH])
        @JoinColumn(name = "fk_endpoint_id")
        var monitoredEndpoint: MonitoredEndpoint
)