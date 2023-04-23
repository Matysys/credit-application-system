package com.dio.credit.application.system.service

import com.dio.credit.application.system.entity.Address
import com.dio.credit.application.system.entity.Credit
import com.dio.credit.application.system.entity.Customer
import com.dio.credit.application.system.exception.BusinessException
import com.dio.credit.application.system.repository.CreditRepository
import com.dio.credit.application.system.repository.CustomerRepository
import com.dio.credit.application.system.service.impl.CreditService
import com.dio.credit.application.system.service.impl.CustomerService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CreditServiceTest {

    @MockK
    lateinit var creditRepository: CreditRepository
    @MockK
    lateinit var customerService: CustomerService
    @InjectMockKs
    lateinit var creditService: CreditService

    @Test
    fun `should save and return credit`(){
        //given
        val fakeId: Long = Random().nextLong()
        val fakeCustomer: Customer = buildCustomer(id = fakeId)
        val fakeCredit: Credit = buildCredit(customer = fakeCustomer);
        every { customerService.findById(fakeCredit.customer?.id!!) } returns fakeCustomer
        every { creditRepository.save(fakeCredit) } returns fakeCredit

        //when
        val actual: Credit = creditRepository.save(fakeCredit)

        //then
        verify { creditRepository.save(fakeCredit) }
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual.customer).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredit)
    }

    @Test
    fun `should return a list of credits`(){
        //given
        val fakeId: Long = Random().nextLong()
        val fakeCustomer: Customer = buildCustomer(id = fakeId)
        val fakeCredit: Credit = buildCredit(customer = fakeCustomer);
        val fakeCreditList = listOf(fakeCredit, buildCredit(customer = fakeCustomer))

        every { creditRepository.findAllByCustomerId(fakeId) } returns fakeCreditList

        //when
        val actual: List<Credit> = creditService.findAllByCustomer(fakeId)

        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual.size).isEqualTo(fakeCreditList.size)
        Assertions.assertThat(actual).containsExactlyElementsOf(fakeCreditList)
    }

    @Test
    fun `should return expected credit`(){
        //given
        val fakeId: Long = Random().nextLong()
        val fakeCustomer: Customer = buildCustomer(id = fakeId)
        val fakeCredit: Credit = buildCredit(customer = fakeCustomer)
        val fakeCreditCode: UUID = fakeCredit.creditCode
        every { creditRepository.findByCreditCode(fakeCreditCode) } returns fakeCredit

        //when
        val actual: Credit = creditService.findByCreditCode(fakeCustomer.id!!, fakeCredit.creditCode!!)

        //every
        Assertions.assertThat(actual).isEqualTo(fakeCredit)

    }

    @Test
    fun `should return BusinessException when creditcode is not found`(){
        //given
        val fakeId: Long = Random().nextLong()
        val fakeCustomer: Customer = buildCustomer(id = fakeId)
        val fakeCredit: Credit = buildCredit(customer = fakeCustomer)
        val fakeCreditCode: UUID? = UUID.randomUUID()
        every { creditRepository.findByCreditCode(fakeCreditCode!!) } returns null

        //when
        val exception = Assertions.catchThrowable{creditService.findByCreditCode(fakeCustomer.id!!, fakeCreditCode!!)}

        //then
        Assertions.assertThat(exception).isInstanceOf(BusinessException::class.java)
            .hasMessage("Creditcode $fakeCreditCode not found")
    }

    @Test
    fun `should return credit when customerId is equal to creditcostumerid`(){
        //given
        val fakeId: Long = Random().nextLong()
        val fakeCustomer: Customer = buildCustomer(id = fakeId)
        val fakeCredit: Credit = buildCredit(customer = fakeCustomer)
        val fakeCreditCode: UUID = UUID.randomUUID()
        every { creditRepository.findByCreditCode(fakeCreditCode) } returns fakeCredit

        //when
        val actual: Boolean = fakeCredit.customer?.id == fakeId

        //then
        Assertions.assertThat(actual).isTrue()

    }

    @Test
    fun `should return IllegalArgumentException when customerId is not equal to creditcostumerid`(){
        //given
        val fakeId: Long = Random().nextLong()
        val fakeCustomer: Customer = buildCustomer(id = 500)
        val fakeCredit: Credit = buildCredit(customer = fakeCustomer)
        val fakeCreditCode: UUID = fakeCredit.creditCode
        every { creditRepository.findByCreditCode(fakeCreditCode) } returns fakeCredit

        //when
        val exception = Assertions.catchThrowable{creditService.findByCreditCode(fakeId!!, fakeCreditCode!!)}

        //then
        Assertions.assertThat(exception).isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Contact Admin")

    }

    private fun buildCredit(
        creditCode: UUID = UUID.randomUUID(),
        creditValue: BigDecimal = BigDecimal.valueOf(500.0),
        dayFirstInstallment: LocalDate = LocalDate.of(2023, Month.APRIL, 22),
        numberOfInstallments: Int = 5,
        customer: Customer
    ): Credit = Credit(
        creditValue = creditValue,
        dayFirstInstallment = dayFirstInstallment,
        numberOfInstallments = numberOfInstallments,
        customer = customer
    )

    private fun buildCustomer(
        firstName: String = "Mateus",
        lastName: String = "Lima",
        cpf: String = "83838374412",
        email: String = "mateus@gmail.com",
        password: String = "12345",
        zipCode: String = "12345",
        street: String = "Rua de tal",
        income: BigDecimal = BigDecimal.valueOf(1000.0),
        id: Long = 1L
    ) = Customer(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        address = Address(
            zipCode = zipCode,
            street = street,
        ),
        income = income,
        id = id
    )


}