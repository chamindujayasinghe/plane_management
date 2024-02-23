import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PlaneManagement {
    static ArrayList<Ticket> tickets = new ArrayList<>();
    static int[][] seats = new int[4][];

    static {
        seats[0] = new int[14];
        seats[1] = new int[12];
        seats[2] = new int[12];
        seats[3] = new int[14];
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the Plane Management application");

        // Display the initial seat availability
        System.out.println("\nSeat availability:");
        displaySeatingPlan();

        // Main loop for user interaction
        boolean continueProgram = true;
        while (continueProgram) {
            // Display the menu
            System.out.println("\nMenu:");
            System.out.println("1. Buy a seat");
            System.out.println("2. Cancel a seat");
            System.out.println("3. Find first available seat");
            System.out.println("4. Show seating plan");
            System.out.println("5. Print tickets information and total sales");
            System.out.println("6. Search ticket");
            System.out.println("0. Quit");

            // Get user input
            int choice;
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your choice: ");
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        buy_Seat();
                        break;
                    case 2:
                        cancel_Seat();
                        break;
                    case 3:
                        findFirstAvailableSeat();
                        break;
                    case 4:
                        displaySeatingPlan();
                        break;
                    case 5:
                        printTicketsInfoAndSales();
                        break;
                    case 6:
                        search_Ticket();
                        break;
                    case 0:
                        continueProgram = false;
                        System.out.println("Exiting the program...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid input!");
                scanner.nextLine();
            }
        }
    }

    static void displaySeatingPlan() {
        for (int i = 0; i < seats.length; i++) {
            char rowLetter = (char) ('A' + i);
            System.out.print("Row " + rowLetter + ": ");
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j] == 0) {
                    System.out.print("O ");
                } else {
                    System.out.print("X ");
                }
            }
            System.out.println();
        }
    }

    static void buy_Seat() {
        Scanner scanner = new Scanner(System.in);

        // Get row letter and seat number from user
        System.out.print("Enter row letter (A, B, C, or D): ");
        String row = scanner.next().toUpperCase();
        System.out.print("Enter seat number (1-" + getNumberOfSeatsInRow(row) + "): ");
        int seatNumber = scanner.nextInt();

        // Validate user input
        if (!isValidRow(row) || !isValidSeatNumber(row, seatNumber) || !isSeatAvailable(row, seatNumber)) {
            System.out.println("Invalid seat selection. Please try again.");
            return;
        }

        // Convert row letter to index
        int rowIndex = row.charAt(0) - 'A';

        // Book the seat
        seats[rowIndex][seatNumber - 1] = 1; // Mark seat as booked (1)

        scanner.nextLine();
        System.out.print("Enter passenger's name: ");
        String name = scanner.nextLine();
        System.out.print("Enter passenger's surname: ");
        String surname = scanner.nextLine();
        System.out.print("Enter passenger's email: ");
        String email = scanner.nextLine();


        Person passenger = new Person(name, surname, email);


        double price = calculatePrice(row, seatNumber);
        Ticket ticket = new Ticket(rowIndex, seatNumber, price, passenger);


        ticket.save();

        tickets.add(ticket);


        System.out.println("Seat purchased successfully for " + passenger.getName() + " " + passenger.getSurname());
    }

    static void cancel_Seat() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter row letter (A, B, C, or D): ");
        String row = scanner.next().toUpperCase();
        System.out.print("Enter seat number (1-" + getNumberOfSeatsInRow(row) + "): ");
        int seatNumber = scanner.nextInt();

        if (!isValidRow(row) || !isValidSeatNumber(row, seatNumber) || isSeatAvailable(row, seatNumber)) {
            System.out.println("Invalid seat selection or seat already available. Please try again.");
            return;
        }

        int rowIndex = row.charAt(0) - 'A';

        seats[rowIndex][seatNumber - 1] = 0;

        // Cancel Ticket if exists
        for (Ticket ticket : tickets) {
            if (ticket.getRow() == rowIndex && ticket.getSeat() == seatNumber) {
                tickets.remove(ticket);
                System.out.println("Seat " + row + seatNumber + " canceled successfully!");
                return;
            }
        }
        System.out.println("Ticket not found for seat " + row + seatNumber);
    }

    static void findFirstAvailableSeat() {
        for (int i = 0; i < seats.length; i++) {
            char rowLetter = (char) ('A' + i);
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j] == 0) {
                    System.out.println("First available seat: Row " + rowLetter + ", Seat " + (j + 1));
                    return;
                }
            }
        }
        System.out.println("Sorry, all seats are booked.");
    }

    static boolean isValidRow(String row) {
        return row.matches("[A-D]");
    }

    static boolean isValidSeatNumber(String row, int seatNumber) {
        return seatNumber >= 1 && seatNumber <= getNumberOfSeatsInRow(row);
    }

    static boolean isSeatAvailable(String row, int seatNumber) {
        int rowIndex = row.charAt(0) - 'A';
        return seats[rowIndex][seatNumber - 1] == 0; // Seat is available if its value is 0
    }

    static int getNumberOfSeatsInRow(String row) {
        int rowIndex = row.charAt(0) - 'A';
        return seats[rowIndex].length;
    }

    static double calculatePrice(String row, int seatNumber) {
        if ((row.equals("A") || row.equals("B") || row.equals("C") || row.equals("D")) && seatNumber >= 1 && seatNumber <= 5) {
            return 200.0;
        } else if ((row.equals("A") || row.equals("B") || row.equals("C") || row.equals("D")) && seatNumber >= 6 && seatNumber <= 9) {
            return 150.0;
        } else {
            return 180.0;
        }
    }

    //task 5
    static void printTicketsInfoAndSales() {
        double totalSales = 0;
        for (Ticket ticket : tickets) {
            ticket.printTicketInfo();
            totalSales += ticket.getPrice();
        }
        System.out.println("Total Sales: $" + totalSales);
    }
    static void search_Ticket() {
        Scanner scanner = new Scanner(System.in);

        // Get row letter and seat number from user
        System.out.print("Enter row letter (A, B, C, or D): ");
        String row = scanner.next().toUpperCase();
        System.out.print("Enter seat number (1-" + getNumberOfSeatsInRow(row) + "): ");
        int seatNumber = scanner.nextInt();

        // Validate user input
        if (!isValidRow(row) || !isValidSeatNumber(row, seatNumber)) {
            System.out.println("Invalid seat selection. Please try again.");
            return;
        }

        int rowIndex = row.charAt(0) - 'A';

        // Check if the seat is booked
        if (seats[rowIndex][seatNumber - 1] == 1) {
            for (Ticket ticket : tickets) {
                if (ticket.getRow() == rowIndex && ticket.getSeat() == seatNumber) {
                    // Print ticket and person information
                    ticket.printTicketInfo();
                    return;
                }
            }
        }

        // If the seat is not booked
        System.out.println("This seat is available.");
    }


    static class Person {
        String name;
        String surname;
        String email;

        // Constructor
        Person(String name, String surname, String email) {
            this.name = name;
            this.surname = surname;
            this.email = email;
        }

        // Getters
        String getName() {
            return name;
        }

        String getSurname() {
            return surname;
        }

        String getEmail() {
            return email;
        }

        // Setters


        // Method to print information
        void printInfo() {
            System.out.println("Name: " + name);
            System.out.println("Surname: " + surname);
            System.out.println("Email: " + email);
        }
    }

    static class Ticket {
        int row;
        int seat;
        double price;

        Person person;

        // Constructor
        Ticket(int row, int seat, double price, Person person) {
            this.row = row;
            this.seat = seat;
            this.price = price;
            this.person = person;
        }

        // Getters
        int getRow() {
            return row;
        }

        int getSeat() {
            return seat;
        }

        double getPrice() {
            return price;
        }

        // Method to print ticket information
        void printTicketInfo() {
            System.out.println("\nTicket Information:");
            System.out.println("Row: " + (char)('A' + row));
            System.out.println("Seat: " + seat);
            System.out.println("Price: $" + price);
            System.out.println("Passenger Information:");
            person.printInfo();
            System.out.println("------------------------");
        }
        void save() {
            File directory = new File("tickets");
            if (!directory.exists()) {
                directory.mkdirs(); // Create the directory if it doesn't exist
            }
            String filename = "tickets/" + ((char) ('A' + row)) + seat + ".txt";

            try (FileWriter writer = new FileWriter(filename)) {
                writer.write("Ticket Information:\n");
                writer.write("Row: " + row + "\n");
                writer.write("Seat: " + seat + "\n");
                writer.write("Price: $" + price + "\n");
                writer.write("Passenger Information:\n");
                writer.write("Name: " + person.getName() + "\n");
                writer.write("Surname: " + person.getSurname() + "\n");
                writer.write("Email: " + person.getEmail() + "\n");
                System.out.println("Ticket information saved to file: " + filename);
            } catch (IOException e) {
                System.out.println("An error occurred while saving the ticket information.");
                e.printStackTrace();
            }
        }
    }
}
