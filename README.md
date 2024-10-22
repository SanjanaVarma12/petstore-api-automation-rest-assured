
# PetStore API Automation with Rest Assured

This project automates testing of the [Swagger PetStore API](https://petstore3.swagger.io/) using **Rest Assured**. It includes automated test cases for **Pet**, **User**, and **Store** APIs, providing comprehensive coverage for common operations. The project uses **Maven** for build management and integrates **Allure** for detailed test reporting.

## Table of Contents

- [Project Overview](#project-overview)
- [Pre-requisites](#pre-requisites)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Test Scenarios](#test-scenarios)
- [Setup and Execution](#setup-and-execution)
- [Generating Allure Reports](#generating-allure-reports)


## Project Overview

This project automates RESTful API testing for the **PetStore** application, focusing on:
- **Pets**
- **Users**
- **Store Orders**

It performs functional testing, API response validation, and status code verification using the **Rest Assured** library. The test suite is integrated with **Allure** to generate detailed test reports for better insights into test execution.

## Pre-requisites

Ensure the following are installed on your system:
- **Java JDK** (8 or higher)
- **Maven** (3.x)
- **Allure Commandline** (for generating reports)
- An IDE (e.g., **IntelliJ IDEA** or **Eclipse**)

## Technologies Used

- **Rest Assured**: API testing framework
- **Maven**: Build and dependency management
- **JUnit/TestNG**: Test management frameworks
- **Allure**: Test reporting tool
- **Jackson**: JSON parsing library
- **Swagger PetStore API**: The target API

## Project Structure

```bash
├── src
│   ├── main
│   │   └── java
│   └── test
│       └── java
│           ├── api.tests  # Contains the API test classes
├── allure-results          # Allure results for reporting
├── pom.xml                 # Maven dependencies and configurations
└── README.md               # Project documentation
```
![image](https://github.com/user-attachments/assets/500348b3-2ab3-45c4-8743-1bb11ea6cfca)

## Test Scenarios

The following scenarios are automated in this project:

### Pet Store API:
- **Add a new pet** (`POST /pet`)
- **Update an existing pet** (`PUT /pet`)
- **Find a pet by ID** (`GET /pet/{petId}`)
- **Delete a pet by ID** (`DELETE /pet/{petId}`)

### User API:
- **Create a user** (`POST /user`)
- **Get user by username** (`GET /user/{username}`)
- **Update user details** (`PUT /user/{username}`)
- **Delete a user** (`DELETE /user/{username}`)

### Store API:
- **Place an order for a pet** (`POST /store/order`)
- **Get order by ID** (`GET /store/order/{orderId}`)
- **Delete order by ID** (`DELETE /store/order/{orderId}`)
- **Get inventory** (`GET /store/inventory`)

### Additional Test Coverage:
- Response validation (status codes, headers, body)
- Edge cases for invalid data

## Setup and Execution

### Step 1: Clone the repository

```bash
git clone https://github.com/SanjanaVarma12/petstore-api-automation-rest-assured.git
```

### Step 2: Open the project in your IDE

Import the project into your IDE (e.g., **IntelliJ IDEA** or **Eclipse**) as a Maven project.

### Step 3: Run the test suite

Navigate to the project directory and execute the following command:

```bash
mvn clean test
```

This command will compile the tests and execute the automated test suite.

## Generating Allure Reports

### Allure Report Setup

To generate and view Allure reports, you need to have **Allure** installed. 

For **Mac** users, you can install Allure via Homebrew:

```bash
brew install allure
```

For **Windows/Linux** users, follow the installation instructions provided in the [Allure documentation](https://docs.qameta.io/allure/#_get_started).

### View Allure Report

After running the test suite, generate and serve the Allure report with the following command:

```bash
allure serve target/allure-results
```

![image](https://github.com/user-attachments/assets/d8f2a4ec-de6b-481b-9f28-8367f260114a)

This will automatically open the Allure report in your default browser. The report includes:
- **Overview**: Summary of test results (passed, failed, skipped tests)
- **Suites**: A breakdown of individual test cases
- **Graphs**: Visual representation of test performance
- **Timeline**: Time distribution for test execution
  
![image](https://github.com/user-attachments/assets/4107c6e2-1507-48aa-92d0-c826ce2f9687)


### Generate a Static Report

To generate a static HTML report without starting a web server, run:

```bash
allure generate target/allure-results --clean
```

The static report will be saved in the `allure-report` directory, and you can open the `index.html` file to view it locally.

