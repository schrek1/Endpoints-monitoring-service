package com.example.monitoredendpoints.model

import org.hibernate.annotations.*
import java.util.*
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "user")
data class User(
        @Id
        @GeneratedValue
        @Type(type="uuid-char")
        var id: UUID,

        @Column(unique = true)
        var name: String,

        @Column(unique = true)
        var email: String,

        @Column(unique = true)
        var token: String
) {
    @ManyToMany(mappedBy = "owners")
    var monitoredEndpoints: MutableList<MonitoredEndpoint> = mutableListOf()

}