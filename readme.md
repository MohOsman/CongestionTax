# Congestion Tax Calculator Spring Boot

### Description
The Congestion Tax Calculator is a REST API application designed to calculate tax fees for vehicles in a specified city. The input is a JSON object containing the city name and a list of toll entries, each including entry time and vehicle registration number. To calculate tax fees, you must first upload the tax rules for that city in the form of a JSON file located under `input/Taxrule.json`. After successfully posting the tax rules, you can then submit a JSON object for toll entries located under 
`input/tollentries.json`. The output will be a list of tax fees per registration number.

### Prerequisites

- Java 17 installed
- Maven (mvn) 3.5.9

### Running the Application
 
To run the application, use the following command:

```bash
mvn spring-boot:run
```

The application will run on the default port 8080.


### API-specifcation 

#### 1. Upload a tax rule for a specific city.

- **Endpoint:** `POST /api/v1/taxRule`
- **Request Body:**  tax rule JSON object
```json
{
  "city" : "Gothenburg",
  "maxAmountPerDay": 60,
  "isFreeOnWeekend": true,
  "taxFreeDates": ["1-1", "3-28","3-29","4-1","4-30","5-01","5-8","5-9","6-5","6-6","6-21",
                   "7","11-1","12-24","12-25","12-26", "12-31"],
  "tolltimeFees" :[
    {
      "startTime": "6:00",
      "endTime": "6:30",
      "fee": 8
    },
    {
      "startTime": "6:29",
      "endTime": "6:59",
      "fee": 8
    },
    {
      "startTime": "7:00",
      "endTime": "7:59",
      "fee": 18
    },
    {
      "startTime": "8:00",
      "endTime": "8:30",
      "fee": 13
    },
    {
      "startTime": "8:30",
      "endTime": "14:59",
      "fee": 8
    }
  ]
}
```
- **Response:** 200 Ok, with empty response. 
- **Resoinse:** 409 Conflict, if a tax rule for the given city already exist.



#### 2. Post toll entries for a given city.

- **Endpoint:** `POST /api/v1/tollEntries`
- **RequestBody**  JSON object with city and list of toll entries

```json
{
  "city" : "Gothenburg",
  "tollEntryList":[
    {
      "date" : "2013-01-14 21:00:00",
      "vehicle": {
        "type": "car",
        "registrationNumber": "XWZ113"
      }
    },
    {
      "date" : "2013-01-15 21:00:00",
      "vehicle": {
        "type": "car",
        "registrationNumber": "XWZ113"
      }
    },
    {
      "date" :"2013-02-07 06:23:27",
      "vehicle": {
        "type": "car",
        "registrationNumber": "XWZ113"
      }
    },
    {
      "date" : "2013-02-07 15:27:00",
      "vehicle": {
        "type": "car",
        "registrationNumber": "XWZ113"
      }
    },
    {
      "date" : "2013-02-08 06:27:00",
      "vehicle": {
        "type": "car",
        "registrationNumber": "ZZA123"
      }
    },
    {
      "date" : "2013-02-08 06:20:00",
      "vehicle": {
        "type": "car",
        "registrationNumber": "ZZA123"
      }
    }]
}
```
- **Response:**  200 OK  JSON Array with TaxFee object with vehicle, dates and fee
- **Response:** 400 Bad Request, if tax rule is not uploaded for the given city

```json
[
  {
  "vehicle": {
    "type": "car",
    "registrationNumber": "XWZ113"
  },
  "fee": 0,
  "dates": [
    "2013-01-14 21:00:00"
  ]
},
{
    "vehicle": {
        "type": "car",
        "registrationNumber": "XWZ113"
    },
    "fee": 0,
    "dates": [
        "2013-01-15 21:00:00"
    ]
},
{
    "vehicle": {
        "type": "car",
        "registrationNumber": "TTV123"
    },
    "fee": 0,
    "dates": [
        "2013-03-28 14:07:27"
    ]
},
{
    "vehicle": {
        "type": "car",
        "registrationNumber": "AUT123"
    },
    "fee": 0,
    "dates": [
        "2013-03-26 14:25:00"
    ]
}
]

```
#### 3. Get tax rule for a given city.

- **Endpoint:** `GET /api/v1/taxRule/{city}`}
- **Response** 200 Ok, Tax rule JSON object should be the same  for `POST /api/v1/taxRule/`
- **Response** 404 Not found , If tar rule for given city does not exist



#### 4. Delete tax rule for a given city.

- **Endpoint:** `DELETE /api/v1/taxRule/{city}`}
- **Response** 200 Ok






### Improvements if I had more time 
#### API improvments
- Full API with PUT,DELETE,GET and POST 
- Get tax fees for a specific registrationNumber 
- Get tax fees for a specific date

#### Application improvements 
- Dockerize 
- Add outside datasource 
- Add more validation/error handling for different scenarios 

#### Test improvements    
- Add end-to-end test / api-tests






 
