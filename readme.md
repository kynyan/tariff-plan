Tariff plan
----------------------------------------

This is a simple app which allows to create, store, and change internet traffic and minutes packages.

## Run tariff plan with Gradle

* Checkout source code with git or simply download .zip package and unpack it
* Open terminal (GitBash on Windows)
* Go to the project root directory (/tariff)
* Execute `sh gradlew bootRun`
* After application is up, it is available at http://localhost:8090.
You may change to any other port by editing application.properties 

## Accessing API

API description is available at http://localhost:8090/api/swagger-ui.html 

## Some tips on how to use an app

* **Create new user with valid first name, last name, passport and phone number** 
(see example at http://localhost:8090/api/swagger-ui.html#/user-controller/createUserUsingPOST)
User and SIM card with specified phone number will be created

* **Add package to user's tariff plan**
(see example of DTO at http://localhost:8090/api/swagger-ui.html#/package-controller/changePackageUsingPOST)
It's possible to add either of internet or minutes package or both of them.
Use same endpoint to change options of user's packages. You may reduce/increase traffic and/or minutes as well as 
expiration dates

* **Deactivate SIM card**
It's possible to activate/deactivate SIM card

## Stack of techologies

* Spring Boot
* H2 in memory database
* Spring Data JPA
* Hibernate
* Flyway
* Swagger
* AssertJ
* QALA datagen
* Gradle

## Entity Relation Diagram

 ![alt text](https://github.com/kynyan/tariff-plan/blob/master/src/main/resources/ER_diagram.png "ERD")
