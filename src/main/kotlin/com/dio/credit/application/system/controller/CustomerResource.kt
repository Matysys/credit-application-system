package com.dio.credit.application.system.controller

import com.dio.credit.application.system.dto.CustomerDto
import com.dio.credit.application.system.dto.CustomerUpdateDto
import com.dio.credit.application.system.dto.CustomerView
import com.dio.credit.application.system.entity.Customer
import com.dio.credit.application.system.service.impl.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/customers")
class CustomerResource(private val customerService: CustomerService) {
    @PostMapping
    fun saveCustomer(@RequestBody customerDto: CustomerDto): ResponseEntity<String>{
        val savedCustomer = this.customerService.save(customerDto.toEntity())
        ResponseEntity.status(HttpStatus.CREATED).body("Customer ${savedCustomer.email} saved!")
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<CustomerView> {
        val customer: Customer = this.customerService.findById(id)
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerView(customer))
    }

    @DeleteMapping("/{id}")
    fun deleteCustomer(@PathVariable id: Long) = this.customerService.delete(id)

    @PatchMapping
    fun updateCustomer(@RequestParam(value = "customerId") id: Long, @RequestBody customerUpdateDto: CustomerUpdateDto): ResponseEntity<CustomerView>{
        val customer: Customer = this.customerService.findById(id)
        val customerToUpdate: Customer = customerUpdateDto.toEntity(customer)
        val customerUpdated: Customer = this.customerService.save(customerToUpdate)
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerView(customerUpdated))
    }
}