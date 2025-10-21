import java.util.Scanner;

class Account {
    private String owner;
    private double balance;
    private String pin;

    public Account(String owner, double balance, String pin) {
        this.owner = owner;
        this.balance = balance;
        this.pin = pin;
    }

    public boolean checkPin(String enteredPin) {
        return this.pin.equals(enteredPin);
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public double getBalance() {
        return balance;
    }

    public String getOwner() {
        return owner;
    }
}

class ATM {
    private Account account;
    private Scanner sc = new Scanner(System.in);
    private int maxPinAttempts = 3;

    public ATM(Account account) {
        this.account = account;
    }

    public void start() {
        int attempts = 0;
        boolean authenticated = false;

        while (attempts < maxPinAttempts && !authenticated) {
            System.out.print("Zadaj PIN: ");
            String pin = sc.nextLine();

            if (account.checkPin(pin)) {
                authenticated = true;
                System.out.println("Vitajte, " + account.getOwner() + "!");
                menu();
            } else {
                attempts++;
                if (attempts < maxPinAttempts) {
                    System.out.println("Nesprávny PIN! Zostávajú " + (maxPinAttempts - attempts) + " pokusy.");
                } else {
                    System.out.println("Príliš veľa nesprávnych pokusov. Karta zablokovaná.");
                }
            }
        }
    }

    private void menu() {
        int volba;
        do {
            System.out.println("\n===== BANKOMAT =====");
            System.out.println("1. Vklad");
            System.out.println("2. Výber");
            System.out.println("3. Zostatok");
            System.out.println("0. Koniec");
            System.out.print("Vyber možnosť: ");

            // Handle invalid input
            if (sc.hasNextInt()) {
                volba = sc.nextInt();
                sc.nextLine(); // Clear the buffer
            } else {
                sc.nextLine(); // Clear invalid input
                volba = -1; // Set to invalid choice
            }

            switch (volba) {
                case 1 -> vklad();
                case 2 -> vyber();
                case 3 -> zobrazZostatok();
                case 0 -> System.out.println("Ďakujeme za použitie bankomatu. Dovidenia!");
                default -> System.out.println("Neplatná voľba! Zadajte číslo 0-3.");
            }
        } while (volba != 0);
    }

    private void vklad() {
        System.out.print("Zadaj sumu na vklad: €");
        if (sc.hasNextDouble()) {
            double suma = sc.nextDouble();
            sc.nextLine(); // Clear buffer

            if (suma <= 0) {
                System.out.println("Suma musí byť kladná!");
            } else if (suma > 10000) {
                System.out.println("Maximálny vklad je €10,000!");
            } else {
                account.deposit(suma);
                System.out.printf("Úspešne vložené: €%.2f%n", suma);
                System.out.printf("Nový zostatok: €%.2f%n", account.getBalance());
            }
        } else {
            sc.nextLine(); // Clear invalid input
            System.out.println("Neplatná suma!");
        }
    }

    private void vyber() {
        System.out.print("Zadaj sumu na výber: €");
        if (sc.hasNextDouble()) {
            double suma = sc.nextDouble();
            sc.nextLine(); // Clear buffer

            if (suma <= 0) {
                System.out.println("Suma musí byť kladná!");
            } else if (suma > 1000) {
                System.out.println("Maximálny výber je €1,000!");
            } else if (account.withdraw(suma)) {
                System.out.printf("Vybraté: €%.2f%n", suma);
                System.out.printf("Nový zostatok: €%.2f%n", account.getBalance());
            } else {
                System.out.println("Nedostatok prostriedkov na účte!");
                System.out.printf("Dostupný zostatok: €%.2f%n", account.getBalance());
            }
        } else {
            sc.nextLine(); // Clear invalid input
            System.out.println("Neplatná suma!");
        }
    }

    private void zobrazZostatok() {
        System.out.println("\n----- Informácie o účte -----");
        System.out.println("Majiteľ: " + account.getOwner());
        System.out.printf("Zostatok: €%.2f%n", account.getBalance());
        System.out.println("-----------------------------");
    }
}

public class Main {
    public static void main(String[] args) {
        // Create test accounts
        Account[] accounts = {
                new Account("Vladimír", 1000.00, "1234"),
                new Account("Anna", 500.50, "5678"),
                new Account("Peter", 2500.75, "9012")
        };

        // For this example, we'll use the first account
        ATM atm = new ATM(accounts[0]);

        System.out.println("===== Vitajte v BANKOMATE =====");
        System.out.println("(Test PIN: 1234)");
        atm.start();
    }
}