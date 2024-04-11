For local development:

 - start wiremock and postgres(not currently used) - cd docker-dev and then docker compose up
 - use postman collection from docker-dev folder (note that callback url used in initiate request "callbackUrl": "http://localhost:9090/merchant/notification" corresponds to path inside wiremock's merchant.json)
