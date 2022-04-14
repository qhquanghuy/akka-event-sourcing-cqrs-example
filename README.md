# Akka Event Sourcing CQRS Example

Build an event sourcing and CQRS service using Akka stack: persistence, cluster, http and projection. This service is called 'BTC Billionaire'

## BTC Billionaire

### APIs

1. /donations

    Create donations

    Example:

    ```bash
    curl --location --request POST 'localhost:8080/donations' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "datetime": "2022-04-13T15:30:05+07:00",
        "amount": 2.1
    }'
    ```
    
    Response donated balance:
    
    ```json
    {"data":{"address":"0xE10b158870c029A98836fC38d8AE5c1B8618844a","balance":2.1}}%
    ```

2. /donation-summary

    GET donation summary by each UTC hour. Data aggrate by datetime that start_time <= datetime < endtime

    Example:

    ```bash
    curl --location --request GET 'localhost:8080/donation-summary?start_time=2022-04-13T14%3A20%3A22.401643%2B07%3A00&end_time=2022-04-13T16%3A20%3A43.031847%2B07%3A00'
    ```
    
    Response:
    
    ```json
    {"data":[{"amount":3.10000000,"time":"2022-04-13T09:00Z[UTC]"},{"amount":13.30000000,"time":"2022-04-13T08:00Z[UTC]"},{"amount":0.00200000,"time":"2022-04-13T07:00Z[UTC]"}]}
    ```

### Run app in local

Prerequisite

- Docker version 20.10.11
- Docker Compose version 2.2.2
- Create .env file in project directory, see .env.example

```bash
$> chmod +x ./startup.sh
$> ./startup.sh
```
