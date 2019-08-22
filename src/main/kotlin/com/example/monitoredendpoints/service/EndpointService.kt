package com.example.monitoredendpoints.service

import com.example.monitoredendpoints.model.*
import com.example.monitoredendpoints.repository.*
import com.example.monitoredendpoints.response.*
import com.example.monitoredendpoints.utils.*
import org.springframework.stereotype.*
import java.time.*
import java.util.*
import javax.transaction.*

@Service
@Transactional
class EndpointService(
        private val userService: UserService,
        private val endpointRepo: EndpointRepository
) {

    fun getEndpointById(id: String): EndpointResult {
        val optional = endpointRepo.findById(UUID.fromString(id))
        return if (optional.isPresent) {
            EndpointResult.Success(optional.get())
        } else {
            EndpointResult.Error.NotFound(endpointId = id)
        }
    }

    fun assignEndpoint(endpointId: String, userId: String): EndpointResult =
            when (val endpointResult: EndpointResult = getEndpointById(endpointId)) {
                is EndpointResult.Success -> {
                    when (val userResult = userService.getUserById(userId)) {
                        is UserResult.Success -> {
                            val monitoredEndpoint = endpointResult.monitoringEndpoint
                            monitoredEndpoint.owners.add(userResult.user)
                            EndpointResult.Success(endpointRepo.save(monitoredEndpoint))
                        }
                        is UserResult.Error -> EndpointResult.Error.NotAssigned(reason = userResult.message)
                    }

                }
                is EndpointResult.Error -> EndpointResult.Error.NotAssigned(reason = endpointResult.errorMessage)
            }


    fun removeEndpointAssign(endpointId: String, userId: String) =
            when (val endpointResult: EndpointResult = getEndpointById(endpointId)) {
                is EndpointResult.Success -> {
                    when (val userResponse = userService.getUserById(userId)) {
                        is UserResult.Success -> {
                            val endpoint = endpointResult.monitoringEndpoint
                            endpoint.owners.remove(userResponse.user)
                            EndpointResult.Success(endpointRepo.save(endpoint))
                        }
                        is UserResult.Error -> EndpointResult.Error.NotAssigned(reason = userResponse.message)
                    }
                }
                is EndpointResult.Error -> EndpointResult.Error.NotFound(endpointId = endpointId)
            }


    fun createEndpoint(name: String, url: String, monitoringInterval: Int, owners: List<String>): EndpointResult {
        val users = mutableListOf<User>()

        for (userId in owners) {
            when (val userResult = userService.getUserById(userId)) {
                is UserResult.Success -> users.add(userResult.user)
                is UserResult.Error -> return EndpointResult.Error.NotCreated(reason = userResult.message)
            }
        }

        val endpoint = endpointRepo.save(MonitoredEndpoint(
                id = Utils.getNullUUID(),
                name = name,
                url = url,
                creation = LocalDateTime.now(),
                lastCheck = LocalDateTime.now(),
                monitoringInterval = monitoringInterval,
                owners = users
        ))

        return EndpointResult.Success(endpoint)
    }

    fun getAllEndpoints(): MutableList<MonitoredEndpoint> = endpointRepo.findAll()


    fun getEndpointsForUser(userToken: String): List<MonitoredEndpoint> =
            when (val userResult = userService.getUserByToken(userToken)) {
                is UserResult.Success -> {
                    val user = userResult.user
                    endpointRepo.findAllByOwners(user)
                }
                is UserResult.Error -> emptyList()
            }

    fun updateEndpoint(endpoint: MonitoredEndpoint) = endpointRepo.save(endpoint)

}