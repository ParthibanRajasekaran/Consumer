# PACT - Consumer Project

This is a spring boot based microservice for onboarding content that shall consume courses microservice. The purpose of the project is to demonstrate consumer driver contract testing using Pactflow as a consumer

### Prerequisites

* Java 17+
* MySql
* Maven
* Pactflow account (optional : to trigger contracts from remote url)
* IDE (IntelliJ)

## Getting Started

1. Clone the Project
2. Create DB and Tables specified using the query in the below section
3. Start the server by running : *src/main/java/com/parthibanrajasekaran/ConsumerApplication.java*

### DB & Table creation

```roomsql
CREATE DATABASE OnboardingContent;

use OnboardingContent;

CREATE TABLE Onboarding_Inventory (
    book_name VARCHAR(50),
    id VARCHAR(50),
    isbn VARCHAR(50),
    aisle VARCHAR(50),
    author VARCHAR(50),
    PRIMARY KEY (id)
);

INSERT INTO Onboarding_Inventory(book_name,id,isbn,aisle,author) values("Microservices","asdf","asdfg",11,"Rooney");
INSERT INTO Onboarding_Inventory(book_name,id,isbn,aisle,author) values("Selenium","qwer","qwert",22,"Rooney");
INSERT INTO Onboarding_Inventory(book_name,id,isbn,aisle,author) values("Appium","zxcv","zxcvb",12,"Wazza");
INSERT INTO Onboarding_Inventory(book_name,id,isbn,aisle,author) values("Springboot","spring","sprung",777,"Wazza");

SELECT 
    *
FROM
    Onboarding_Inventory;
```

## Test Location
* Pact Provider Tests:
  - src/test/java/com/parthibanrajasekaran/PactOnboardingContentTests.java
* Integration Tests: 
  - src/test/java/com/parthibanrajasekaran/OnboardingContentTestsIT.java
* Unit Tests: 
  - src/test/java/com/parthibanrajasekaran/OnboardingContentTests.java
  - src/test/java/com/parthibanrajasekaran/OnboardingContentServiceTests.java
  - src/test/java/com/parthibanrajasekaran/OnboardingContentRepositoryImplTests.java

## Usage

* To run PACT consumer tests, trigger tests from the aforementioned location

* To run all tests in the project (Unit + Integration + Pact Provider tests)
```
$ mvn clean test
```
* By default, the pact tests till upload the contract to the remote url (https://parthibanrajasekaran.pactflow.io/)

* Upon execution in local machine, the contract gets created in this location: target/pacts/OnboardingContent-CoursesCatalogue.json

## Additional Documentation and Acknowledgments

<p align="center">
  <a href="https://docs.pact.io/">What is PACT?</a> •
  <a href="https://junit.org/junit5/docs/current/user-guide/">Assertions</a>
</p>

## FAQ
How can I delete the table created?
```roomsql
use OnboardingContent;
DROP TABLE Onboarding_Inventory;
```

How can I delete the DB created
```roomsql
DROP DATABASE OnboardingContent;
```