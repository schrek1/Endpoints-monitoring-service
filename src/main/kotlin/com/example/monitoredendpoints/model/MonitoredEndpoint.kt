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
@Table(name = "monitored_endpoint")
data class MonitoredEndpoint(
        @Id
        @GeneratedValue
        @Type(type = "uuid-char")
        var id: UUID,

        @NotBlank
        var name: String,

        @NotBlank
        var url: String,

        @NotNull
        var creation: LocalDateTime,

        @Column("last_check")
        @NotNull
        var lastCheck: LocalDateTime,

        @Column("monitoring_interval")
        @Positive
        var monitoringInterval: Int,

        @ManyToMany(cascade = [DETACH, MERGE, PERSIST, REFRESH])
        @JoinTable(name = "user_endpoint", joinColumns = [JoinColumn(name = "fk_monitoring_endpoint_id")], inverseJoinColumns = [JoinColumn(name = "fk_user_id")])
        var owners: MutableList<User>

) {
    @OneToMany(mappedBy = "monitoredEndpoint")
    val monitoringResults: MutableList<MonitoringResult> = mutableListOf()
}