package com.example.monitoredendpoints.utils

import org.springframework.security.core.*
import org.springframework.security.core.userdetails.*


fun Authentication.getToken(): String = (this.principal as User).username