# API REST Kotlin de de aplicação de cartão de crédito

Esse projeto foi feito acompanhando as aulas da DIO (Digital Innovation One), e embora esse 
projeto não seja um **fork** do [ORIGINAL](https://github.com/cami-la/credit-application-system),
ele foi feito e melhorado com base no original. Clique no link para ver as especificações originais, 
pois esse aqui é o mesmo projeto. Porém, com algumas melhorias.

## Testes Unitários adicionados
Eu criei métodos que testem o ```CreditService``` e ```CreditResource```:
- CreditServiceTest 
- CreditResourceTest

No projeto original só há:
- CustomerResourceTest
- CreditRepositoryTest
- CustomerServiceTest

O caminho para os testes unitários começa em:
```src/test/kotlin/me/dio/credit/application/system/``` dentro das pastas ```controller```, ```repository``` e ```service```.
