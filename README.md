# accounts-data-validator


## Features

<br>
<br>
<br>

## Building & Running

To build or run the project, use one of the following tasks:

| Task              | Description                                                          |
|-------------------|---------------------------------------------------------------------- |
| `./gradlew test`  | Run the tests                                                        |
| `./gradlew build` | Build everything                                                     |
| `./gradlew run`   | Run the server                                                       |

<br>
<br>
<br>


## Accesss the REST Api

```
curl -X POST http://0.0.0.0:8080/v1/accounts/benfords-validation 
-H "Content-Type: application/json"  
-d '{ "data": "<Accounting Data>", "significanceLevel": <Significance Level>}'
```
<br>
<br>

### Format of the data string

The string consists of key value pairs. Key and value separated by a colon. Each key value pair
is separated by a semicolon and a space.
```
<key>:<value>; <key>:<value>; 

Example: "inv_004:1123.20; exp_utilities:1560.00; refund_01:115.75; deposit_01:1980.00; 
```
<br>

#### Example 1:
This dataset is compliance with the Benford's law. 

<br>

Request:
```
curl -H "Content-Type: application/json"  -X POST http://0.0.0.0:8080/v1/accounts/benfords-validation -d '{ "data": "inv_001:1230.00; inv_002:1050.00; inv_003:1875.50; inv_004:1123.20; exp_utilities:1560.00; refund_01:115.75; deposit_01:1980.00; inv_005:2267.50; exp_software:2450.00; salary_01:2890.00; tax_vat:235.60; inv_006:3199.99; payment_03:3214.00; consulting:3250.00; exp_marketing:450.00; fee_bank:415.20; exp_office:567.89; adjustment_01:523.45; exp_travel:632.00; misc_01:678.10; payment_02:721.15; refund_02:875.50; insurance:912.00", "significanceLevel": 0.05}'
```
<br>


Response:
```
{
   "observedDistribution":{
      "1":30,
      "2":17,
      "3":13,
      "4":8,
      "5":8,
      "6":8,
      "7":4,
      "8":4,
      "9":4
   },
   "expectedDistribution":{
      "1":30,
      "2":17,
      "3":12,
      "4":9,
      "5":7,
      "6":6,
      "7":5,
      "8":5,
      "9":4
   },
   "chiSquareStatistic":0.3,
   "criticalValue":15.51,
   "isBenfordCompliant":true
}
```

<br>
<br>

#### Example 2:
This dataset is not compliance with the Benford's law.

<br>

Request:
```
curl -H "Content-Type: application/json" -X POST http://0.0.0.0:8080/v1/accounts/benfords-validation -d '{ "data": "inv_001:9230.00; inv_002:9050.00; inv_003:9875.50; inv_004:1923.20; exp_utilities:9560.00; refund_01:915.75; deposit_01:9980.00; inv_005:2267.50; exp_software:2450.00; salary_01:2890.00; tax_vat:235.60; inv_006:3199.99; payment_03:3214.00; consulting:3250.00; exp_marketing:450.00; fee_bank:415.20; exp_office:567.89; adjustment_01:523.45; exp_travel:632.00; misc_01:678.10; payment_02:721.15; refund_02:875.50; insurance:912.00", "significanceLevel": 0.05}'

```

<br>


Response:
```
{
   "observedDistribution":{
      "1":4,
      "2":17,
      "3":13,
      "4":8,
      "5":8,
      "6":8,
      "7":4,
      "8":4,
      "9":30
   },
   "expectedDistribution":{
      "1":30,
      "2":17,
      "3":12,
      "4":9,
      "5":7,
      "6":6,
      "7":5,
      "8":5,
      "9":4
   },
   "chiSquareStatistic":38.97,
   "criticalValue":15.51,
   "isBenfordCompliant":false
}
```