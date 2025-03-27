# accounts-data-validator


## Features


## Building & Running

To build or run the project, use one of the following tasks:

| Task              | Description                                                          |
|-------------------|---------------------------------------------------------------------- |
| `./gradlew test`  | Run the tests                                                        |
| `./gradlew build` | Build everything                                                     |
| `./gradlew run`   | Run the server                                                       |


## Accesss the REST Api

```
curl -X POST http://0.0.0.0:8080/v1/accounts/benfords-validation 
-H "Content-Type: application/json"  
-d '{ "data": "<Accounting Data>", "significanceLevel": <Significance Level>}'
```


Example 1:

Request:
```
curl -X POST http://0.0.0.0:8080/v1/accounts/benfords-validation
  -H "Content-Type: application/json"  
-d '{ 
    "data": "inv_001:1230.00; inv_002:1050.00; inv_003:1875.50; inv_004:1123.20; exp_utilities:1560.00; refund_01:115.75; 
                deposit_01:1980.00; inv_005:2267.50; exp_software:2450.00; salary_01:2890.00; tax_vat:235.60; inv_006:3199.99; 
                payment_03:3214.00; consulting:3250.00; exp_marketing:450.00; fee_bank:415.20; exp_office:567.89; adjustment_01:523.45; 
                exp_travel:632.00; misc_01:678.10; payment_02:721.15; refund_02:875.50; insurance:912.00", 
    "significanceLevel": 0.05
    }'

```


Example 2:

Request:
```
curl -X POST http://0.0.0.0:8080/v1/accounts/benfords-validation
  -H "Content-Type: application/json"  
-d '{ 
    "data": "inv_001:9230.00; inv_002:9050.00; inv_003:9875.50; inv_004:1923.20; exp_utilities:9560.00; refund_01:915.75; 
            deposit_01:9980.00; inv_005:2267.50; exp_software:2450.00; salary_01:2890.00; tax_vat:235.60; inv_006:3199.99; 
            payment_03:3214.00; consulting:3250.00; exp_marketing:450.00; fee_bank:415.20; exp_office:567.89; adjustment_01:523.45; 
            exp_travel:632.00; misc_01:678.10; payment_02:721.15; refund_02:875.50; insurance:912.00", 
    "significanceLevel": 0.05
    }'

```