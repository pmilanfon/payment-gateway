For local development:

 - start wiremock and postgres(not currently used) - cd docker-dev and then docker compose up
 - use postman collection from docker-dev folder (note that callback url used in initiate request "callbackUrl": "http://localhost:9090/merchant/notification" corresponds to path inside wiremock's merchant.json)

Deploy to Azure Cloud:
 - Execute az login
 - Execute az spring app deploy -n payment-gateway -g payce-resource-group -s payce-gateway-service --artifact-path target/payment-gateway-0.0.1-SNAPSHOT.jar
