# springms – Spring Boot Microservices + JPA + MSSQL + REST (Maven)

| Module            | Port | What it does                                              |
|-------------------|------|-----------------------------------------------------------|
| discovery-server  | 8761 | Eureka service registry                                   |
| customer-service  | 8081 | CRUD for customers → saves to MSSQL `customerdb`          |
| order-service     | 8082 | CRUD for orders → saves to MSSQL `orderdb`; calls customer-service to check the customer |
| api-gateway       | 8080 | Single entry point (HTTP 8080, HTTPS 8443 with `ssl` profile) |

Tech: Java 17, Spring Boot 3.3.5, Spring Cloud 2023.0.3, Spring Data JPA/Hibernate, mssql-jdbc, Maven.

## 1. Start MSSQL + create databases
    docker compose up
(creates `customerdb` and `orderdb`; sa password `YourStrong@Passw0rd`).
If you use your own SQL Server, run `init-db.sql` and change URL/user/password in each service's `application.yml`.
Tables are auto-created by Hibernate (`ddl-auto: update`).

## 2. Build
    mvn clean install

## 3. Run (4 terminals, in this order)
    mvn -pl discovery-server spring-boot:run
    mvn -pl customer-service spring-boot:run
    mvn -pl order-service spring-boot:run
    mvn -pl api-gateway spring-boot:run

Open http://localhost:8761 – you should see CUSTOMER-SERVICE, ORDER-SERVICE, API-GATEWAY registered.

## 4. Test through the gateway
    # create customer
    curl -X POST http://localhost:8080/api/customers -H "Content-Type: application/json" \
      -d '{"name":"Ravi","email":"ravi@example.com","phone":"9999999999"}'

    curl http://localhost:8080/api/customers
    curl http://localhost:8080/api/customers/1
    curl -X PUT http://localhost:8080/api/customers/1 -H "Content-Type: application/json" \
      -d '{"name":"Ravi Kumar","email":"ravi@example.com","phone":"8888888888"}'

    # create order (order-service calls customer-service internally)
    curl -X POST http://localhost:8080/api/orders -H "Content-Type: application/json" \
      -d '{"customerId":1,"productName":"Laptop","quantity":2,"price":55000.50}'

    curl http://localhost:8080/api/orders
    curl http://localhost:8080/api/orders/customer/1
    curl -X DELETE http://localhost:8080/api/orders/1

    # error checks
    curl -X POST http://localhost:8080/api/orders -H "Content-Type: application/json" \
      -d '{"customerId":999,"productName":"X","quantity":1,"price":10}'     # -> 404 customer not found
    curl -X POST http://localhost:8080/api/customers -H "Content-Type: application/json" \
      -d '{"name":"","email":"bad"}'                                        # -> 400 validation errors

Check data in SQL Server:  `SELECT * FROM customerdb.dbo.customers;`  `SELECT * FROM orderdb.dbo.orders;`

## 5. HTTPS (optional)
Create a self-signed certificate:

    keytool -genkeypair -alias springms -keyalg RSA -keysize 2048 -storetype PKCS12 \
      -keystore api-gateway/src/main/resources/keystore.p12 -validity 365 \
      -storepass changeit -dname "CN=localhost"

Run the gateway with the ssl profile:

    mvn -pl api-gateway spring-boot:run -Dspring-boot.run.profiles=ssl

Test (`-k` accepts the self-signed certificate):

    curl -k https://localhost:8443/api/customers
