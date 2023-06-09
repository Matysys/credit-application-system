package com.dio.credit.application.system.service.impl

import com.dio.credit.application.system.entity.Credit
import com.dio.credit.application.system.exception.BusinessException
import com.dio.credit.application.system.repository.CreditRepository
import com.dio.credit.application.system.service.ICrediteService
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import java.util.*

@Service
class CreditService(private val creditRepository: CreditRepository, private val customerService: CustomerService): ICrediteService {
    override fun save(credit: Credit): Credit {
        credit.apply {
            customer = customerService.findById(credit.customer?.id!!)
        }
        return this.creditRepository.save(credit)
    }

    override fun findAllByCustomer(customerId: Long): List<Credit> = this.creditRepository.findAllByCustomerId((customerId))

    override fun findByCreditCode(customerId: Long, creditCode: UUID): Credit {
       val credit: Credit = this.creditRepository.findByCreditCode(creditCode)
            ?: throw BusinessException("Creditcode $creditCode not found")
        return if(credit.customer?.id == customerId) credit else throw IllegalArgumentException("Contact Admin")
        /*if(credit.customer?.id == customerId){
            return credit
        }else{
            throw RuntimeException("Contact Admin")
        }*/
    }

}