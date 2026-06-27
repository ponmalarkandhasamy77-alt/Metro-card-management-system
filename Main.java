import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
class InvalidStationException extends Exception {
    public InvalidStationException(String message) {
        super(message);
    }
}
class MetroCard {
    private final String cardId;
    private final String passengerName;
    private double balance;
    private String sourceStation;
    private boolean isSwipedIn;

    public MetroCard(String cardId, String passengerName, double initialBalance) {
        this.cardId = cardId;
        this.passengerName = passengerName;
        this.balance = initialBalance;
        this.isSwipedIn = false;
        this.sourceStation = null;
    }
    public String getCardId() { return cardId; }
    public String getPassengerName() { return passengerName; }
    public double getBalance() { return balance; }
    public boolean isSwipedIn() { return isSwipedIn; }
    public String getSourceStation() { return sourceStation; }

    public void addBalance(double amount) {
        this.balance += amount;
    }

    public void deductBalance(double amount) {
        this.balance -= amount;
    }

    public void swipeIn(String station) {
        this.isSwipedIn = true;
        this.sourceStation = station;
    }

    public void swipeOut() {
        this.isSwipedIn = false;
        this.sourceStation = null;
    }
}

public class Main {
    private static final Map<String, Integer> STATIONS = new HashMap<>();
    private static final Map<String, MetroCard> CARD_DATABASE = new HashMap<>();
    private static final double FARE_PER_STATION = 5.00;
    private static final double MINIMUM_BALANCE = 20.00;

    static {
        STATIONS.put("STATION_A", 1);
        STATIONS.put("STATION_B", 2);
        STATIONS.put("STATION_C", 3);
        STATIONS.put("STATION_D", 4);
        STATIONS.put("STATION_E", 5);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Welcome to the Java Metro Card System ===");

        while (true) {
            System.out.println("\n1. get New Metro Card.");
            System.out.println("2. Recharge Metro Card.");
            System.out.println("3. Swipe In(Board).");
            System.out.println("4. Swipe Out(Exit).");
            System.out.println("5. Check Balance.");
            System.out.println("6. Exit Application.");
            System.out.print("Select an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    issueCard(scanner);
                    break;
                case 2:
                    rechargeCard(scanner);
                    break;
                case 3:
                    processSwipeIn(scanner);
                    break;
                case 4:
                    processSwipeOut(scanner);
                    break;
                case 5:
                    checkCardBalance(scanner);
                    break;
                case 6:
                    System.out.println("Thank you for using the Metro System.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void issueCard(Scanner scanner) {
        System.out.print("Enter Passenger Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Initial Deposit (Min 100): ");
        double balance = scanner.nextDouble();

        if (balance < 100) {
            System.out.println("Error: Minimum initial deposit of 100 is required.");
            return;
        }

        String cardId = "MC" + (CARD_DATABASE.size() + 1001);
        MetroCard newCard = new MetroCard(cardId, name, balance);
        CARD_DATABASE.put(cardId, newCard);

        System.out.println("\n✔ Metro Card successfully created!");
        System.out.println("Card ID: " + cardId);
        System.out.println("Passenger: " + name);
        System.out.println("Balance: $" + balance);
    }

    private static void rechargeCard(Scanner scanner) {
        System.out.print("Enter Metro Card ID: ");
        String cardId = scanner.nextLine().toUpperCase();
        MetroCard card = CARD_DATABASE.get(cardId);

        if (card == null) {
            System.out.println("Error: Card ID not found.");
            return;
        }

        System.out.print("Enter Recharge Amount: ");
        double amount = scanner.nextDouble();
        if (amount <= 0) {
            System.out.println("Error: Invalid amount.");
            return;
        }

        card.addBalance(amount);
        System.out.println("✔ Recharge successful. New Balance: $" + card.getBalance());
    }

    private static void processSwipeIn(Scanner scanner) {
        try {
            System.out.print("Enter Metro Card ID: ");
            String cardId = scanner.nextLine().toUpperCase();
            MetroCard card = CARD_DATABASE.get(cardId);

            if (card == null) {
                System.out.println("Error: Card ID not found.");
                return;
            }

            if (card.isSwipedIn()) {
                System.out.println("Error: Card is already swiped in at " + card.getSourceStation());
                return;
            }

            if (card.getBalance() < MINIMUM_BALANCE) {
                throw new InsufficientBalanceException("Insufficient balance! Minimum $" + MINIMUM_BALANCE + " required to board.");
            }

            System.out.print("Enter Source Station (STATION_A to STATION_E): ");
            String station = scanner.nextLine().toUpperCase();

            if (!STATIONS.containsKey(station)) {
                throw new InvalidStationException("The entered station does not exist on this line.");
            }

            card.swipeIn(station);
            System.out.println("✔ Swipe In Successful! Boarded at: " + station);

        } catch (InsufficientBalanceException | InvalidStationException e) {
            System.out.println("*" + e.getMessage());
        }
    }

    private static void processSwipeOut(Scanner scanner) {
        try {
            System.out.print("Enter Metro Card ID: ");
            String cardId = scanner.nextLine().toUpperCase();
            MetroCard card = CARD_DATABASE.get(cardId);

            if (card == null) {
                System.out.println("Error: Card ID not found.");
                return;
            }

            if (!card.isSwipedIn()) {
                System.out.println("Error: This card has not swiped in at any station.");
                return;
            }

            System.out.print("Enter Destination Station (STATION_A to STATION_E): ");
            String destStation = scanner.nextLine().toUpperCase();

            if (!STATIONS.containsKey(destStation)) {
                throw new InvalidStationException("The entered station does not exist on this line.");
            }
            int sourceIdx = STATIONS.get(card.getSourceStation());
            int destIdx = STATIONS.get(destStation);
            int stationsTraveled = Math.abs(destIdx - sourceIdx);

            if (stationsTraveled == 0) {
                System.out.println("Same-station exits incur no charges.");
            }

            double totalFare = stationsTraveled * FARE_PER_STATION;
            if (card.getBalance() < totalFare) {
                throw new InsufficientBalanceException("Insufficient balance to exit. Required Fare: $" + totalFare + ". Please recharge your card.");
            }
            card.deductBalance(totalFare);
            System.out.println("\n✔ Swipe Out Successful!");
            System.out.println("Journey: " + card.getSourceStation() + " ➔ " + destStation);
            System.out.println("Fare Deducted: $" + totalFare);
            System.out.println("Remaining Balance: $" + card.getBalance());
            card.swipeOut(); 
        } catch (InsufficientBalanceException | InvalidStationException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
    private static void checkCardBalance(Scanner scanner) {
        System.out.print("Enter Metro Card ID: ");
        String cardId = scanner.nextLine().toUpperCase();
        MetroCard card = CARD_DATABASE.get(cardId);
        if (card == null) {
            System.out.println("Error: Card ID not found.");
            return;
        }
        System.out.println("\n--- Card Details ---");
        System.out.println("Card Holder: " + card.getPassengerName());
        System.out.println("Current Balance: $" + card.getBalance());
        System.out.println("Status: " + (card.isSwipedIn() ? "Swiped In at " + card.getSourceStation() : "Not In Transit"));
    }
}
