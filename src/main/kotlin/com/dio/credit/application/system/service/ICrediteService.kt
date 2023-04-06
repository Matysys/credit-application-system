package com.dio.credit.application.system.service

import com.dio.credit.application.system.entity.Credit
import java.util.*

interface ICrediteService {
    fun save(credit: Credit): Credit

    fun findAllByCustomer(customerId: Long): List<Credit>

    fun findByCreditCode(customerId: Long, creditCode: UUID): Credit
}