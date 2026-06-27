# Java Metro Card Simulation System

A robust, console-based Java application that simulates an automated metro transit network. The system leverages object-oriented programming (OOP) principles, custom exception handling, and collection maps to manage passengers, calculate dynamic station-to-station fares, and handle real-time swipe actions.

---

## 🚀 Features

* **Card Issuance**: Generates unique IDs (`MC1001`, `MC1002`, etc.) with a minimum initialization deposit configuration.
* **Smart Fare Calculation**: Computes transactional costs automatically based on absolute distance between geographic network nodes.
* **Custom Exceptions**: Uses explicit error handling classes (`InsufficientBalanceException`, `InvalidStationException`) to block illegal transits.
* **State Machine Mechanics**: Prevents illegal operational ordering (e.g., double swipe-ins or exiting without entering).
* **Buffer Safety**: Employs scanner line-clearing routines to prevent console prompt-skipping errors.

---

## 🛠️ System Specifications

* **Per-Station Cost**: $5.00
* **Minimum Boarding Balance**: $20.00
* **Minimum Initial Deposit**: $100.00
* **Supported Stations**: `STATION_A` through `STATION_E`

---

## 💻 Technical Stack

* **Language**: Java (JDK 8 or higher)
* **Core APIs Used**: `java.util.Scanner`, `java.util.HashMap`, `java.util.Map`

---

## ⚙️ How to Run

1. Ensure you have the **Java Development Kit (JDK)** installed on your machine.
2. Download or copy the `Main.java` file content.
3. Open your terminal/command prompt and compile the application:
   ```bash
   javac Main.java
   ```
4. Execute the compiled bytecode:
   ```bash
   java Main
   ```

---

## 📝 Usage Example

1. **Select Option 1** to register a new user and generate a card.
2. **Select Option 3** to swipe into `STATION_A`.
3. **Select Option 4** to swipe out at `STATION_D`. The system automatically calculates 3 stations traveled, deducts $15.00, and logs the remaining balance.
