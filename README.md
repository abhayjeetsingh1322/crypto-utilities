# CryptoUtilities

## Description
The **CryptoUtilities** project explores the foundational number-theoretic computations used in modern cryptography. It involves implementing utility methods to work with **prime numbers**, **modular arithmetic**, and **random number generation**. These methods simulate computations used in public-key cryptography, such as RSA, to ensure secure communication.

In addition, the project leverages **JUnit** to create and run test cases for verifying the correctness of these utility methods.

---

## Objectives
1. Develop an understanding of cryptographic computations, including:
   - Prime number generation and testing.
   - Modular arithmetic.
   - Witness-based prime verification.
2. Gain experience using `NaturalNumber` objects and methods.
3. Learn to write and run unit tests with JUnit.

---

## Features
### 1. Utility Methods
The program includes the following cryptographic utility methods:
- **isEven**: Determines if a number is even.
- **reduceToGCD**: Computes the greatest common divisor (GCD) of two numbers.
- **powerMod**: Computes \( w^n \mod m \) efficiently using modular exponentiation.
- **isWitnessToCompositeness**: Checks whether a given number is a witness to the compositeness of another number.
- **isPrime2**: Determines if a number is likely a prime using witness-based testing.
- **generateNextLikelyPrime**: Generates a large number that is highly likely to be prime.

### 2. Error Handling
- Ensures invalid inputs (e.g., divide by zero) are handled gracefully with descriptive error messages.

### 3. Unit Testing with JUnit
- **CryptoUtilitiesTest**: Provides unit tests to validate the correctness of each method in `CryptoUtilities.java`.
- Includes sample test cases and encourages adding more comprehensive tests to ensure reliability.

---

## Technologies Used
- **Java** for implementing cryptographic computations.
- **JUnit** for writing and running unit tests.
- **NaturalNumber** objects for handling large numbers and arithmetic operations.

---

## How to Run
### Prerequisites
- Java Development Kit (JDK)
- Eclipse IDE or any Java-compatible IDE

### Steps
1. Clone the repository:
   ```bash
   git clone [repository URL]
