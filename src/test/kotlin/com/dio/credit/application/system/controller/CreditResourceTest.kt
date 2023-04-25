package com.dio.credit.application.system.controller

import com.dio.credit.application.system.dto.CreditDto
import com.dio.credit.application.system.dto.CustomerDto
import com.dio.credit.application.system.entity.Credit
import com.dio.credit.application.system.entity.Customer
import com.dio.credit.application.system.repository.CreditRepository
import com.dio.credit.application.system.repository.CustomerRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CreditResourceTest {
    @Autowired
    private lateinit var creditRepository: CreditRepository
    @Autowired
    private lateinit var customerRepository: CustomerRepository
    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object{
        const val URL: String = "/api/credits"
    }

    @BeforeEach
    fun setup() {
        creditRepository.deleteAll()
        customerRepository.deleteAll()
    }
    @AfterEach
    fun tearDown() {
        creditRepository.deleteAll()
        customerRepository.deleteAll()
    }

    @Test
    fun `should create a credit and return 201 status`() {
        //given
        val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())
        val credit: CreditDto = builderCreditDto(customerId = customer.id!!.toLong())
        //Assertions.assertThat(creditDto.customerId).isEqualTo(2)

        val valueAsString: String = objectMapper.writeValueAsString(credit)
        //when

        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        ).andExpect(MockMvcResultMatchers.status().isCreated)
            .andDo(MockMvcResultHandlers.print())


    }

    @Test
    fun `should find all credit by customerId and return 200 status`() {
        //given
        val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())
        val credit: CreditDto = builderCreditDto(customerId = customer.id!!.toLong())
        val valueAsString: String = objectMapper.writeValueAsString(credit)

        //when

        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL?customerId=${customer.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())


    }

    @Test
    fun `should find credit by customerId, creditCode and return 200 status`() {
        //given
        val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())
        val credit: Credit = creditRepository.save(builderCreditDto(customerId = customer.id!!.toLong()).toEntity())
        val valueAsString: String = objectMapper.writeValueAsString(credit)

        //when

        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/${credit.creditCode}?customerId=${customer.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())


    }

    //BUILDS ABAIXO
    private fun builderCreditDto(
        creditValue: BigDecimal = BigDecimal.valueOf(2000.0),
        dayFirstOfInstallment: LocalDate = LocalDate.now().plusDays(45),
        numberOfInstallments: Int = 5,
        customerId: Long = 1L
    ) = CreditDto(
        creditValue = creditValue,
        dayFirstOfInstallment = dayFirstOfInstallment,
        numberOfInstallments = numberOfInstallments,
        customerId = customerId
    )

    private fun builderCustomerDto(
        firstName: String = "Mateus",
        lastName: String = "Lima",
        cpf: String = "28475934625",
        email: String = "mateus@email.com",
        income: BigDecimal = BigDecimal.valueOf(2000.0),
        password: String = "1234",
        zipCode: String = "000000",
        street: String = "Rua de tal, 123",
    ) = CustomerDto(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        income = income,
        password = password,
        zipCode = zipCode,
        street = street
    )
}