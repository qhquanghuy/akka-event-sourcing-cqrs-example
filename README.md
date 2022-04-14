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

2. /donation-summary

    GET donation summary by each hour. Data aggrate by datetime that start_time <= datetime < endtime

    Example:

    ```bash
    curl --location --request GET 'localhost:8080/donation-summary?start_time=2022-04-13T14%3A20%3A22.401643%2B07%3A00&end_time=2022-04-13T16%3A20%3A43.031847%2B07%3A00'
    ```

### Run app in local

Prerequisite

- Docker version 20.10.11
- Docker Compose version 2.2.2

```bash
$> chmod +x ./startup.sh
$> ./startup.sh
```
