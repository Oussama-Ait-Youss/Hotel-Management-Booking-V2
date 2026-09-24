import model.User;
import model.enums.UserRole;
import repository.jdbc.JdbcRoomRepository;
import repository.jdbc.JdbcUserRepository;
import service.AuthService;
import service.BookingTransactionService;
import service.PasswordService;
import service.UserService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final JdbcRoomRepository roomRepository = new JdbcRoomRepository();
    private static final policy.DynamicPricingStrategy pricingStrategy = new policy.DynamicPricingStrategy();
    private static final BookingTransactionService bookingTransactionService = new BookingTransactionService();
    private static final Scanner scanner =
            new Scanner(System.in);

    private static final JdbcUserRepository userRepository =
            new JdbcUserRepository();

    private static final UserService userService =
            new UserService(userRepository);

    private static final PasswordService passwordService =
            new PasswordService();

    private static final AuthService authService =
            new AuthService(
                    userRepository,
                    passwordService
            );

    private static User loggedInUser;

    public static void main(String[] args) {

        boolean running = true;

        while (running) {

            if (loggedInUser == null) {

                running = showAuthMenu();

            } else {

                showRoleMenu();
            }
        }

        scanner.close();

        System.out.println("\nGoodbye!");
    }



    private static boolean showAuthMenu() {

        printAuthMenu();

        int choice =
                readInt("Choose an option: ");

        try {

            switch (choice) {

                case 1:
                    register();
                    break;

                case 2:
                    login();
                    break;

                case 3:
                    return false;

                default:
                    System.out.println(
                            "\nInvalid option. Please choose 1-3."
                    );
            }

        } catch (RuntimeException e) {

            System.out.println(
                    "\nERROR: " + e.getMessage()
            );
        }

        return true;
    }

    private static void printAuthMenu() {

        System.out.println();
        System.out.println("====================================================");
        System.out.println("                    AUTH MENU");
        System.out.println("====================================================");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.println("====================================================");
    }



    private static void register() {

        System.out.println();
        System.out.println("========== REGISTER ==========");

        String firstName =
                readString("First name: ");

        String lastName =
                readString("Last name: ");

        String email =
                readString("Email: ");

        String password =
                readString("Password: ");

        UserRole role =
                readRole();

        User registeredUser =
                authService.register(
                        firstName,
                        lastName,
                        email,
                        password,
                        role
                );

        System.out.println(
                "\nUser registered successfully!"
        );

        System.out.println(
                "Account: "
                        + registeredUser.getEmail()
        );
    }



    private static void login() {

        System.out.println();
        System.out.println("========== LOGIN ==========");

        String email =
                readString("Email: ");

        String password =
                readString("Password: ");

        User user =
                authService.login(
                        email,
                        password
                );

        loggedInUser = user;

        System.out.println();
        System.out.println("Login successful!");
        System.out.println(
                "Welcome, "
                        + user.getFirstName()
                        + "!"
        );

        System.out.println(
                "Role: "
                        + user.getRole()
        );
    }


    private static void showRoleMenu() {

        try {

            if (loggedInUser.getRole()
                    == UserRole.ADMIN) {

                showAdminMenu();

            } else if (loggedInUser.getRole()
                    == UserRole.CLIENT) {

                showClientMenu();

            } else {

                throw new IllegalStateException(
                        "Unknown user role."
                );
            }

        } catch (RuntimeException e) {

            System.out.println(
                    "\nERROR: " + e.getMessage()
            );
        }
    }



    private static void showAdminMenu() {

        System.out.println();
        System.out.println("====================================================");
        System.out.println("                   ADMIN MENU");
        System.out.println("====================================================");

        System.out.println(
                "Logged in as: "
                        + loggedInUser.getFirstName()
                        + " "
                        + loggedInUser.getLastName()
        );

        System.out.println("----------------------------------------------------");

        System.out.println("1. Manage users");
        System.out.println("2. Manage rooms");
        System.out.println("3. Manage reservations");
        System.out.println("4. Manage payments");
        System.out.println("5. Manage invoices");
        System.out.println("6. Manage refunds");
        System.out.println("7. Statistics / KPIs");
        System.out.println("8. Logout");

        System.out.println("====================================================");

        int choice =
                readInt("Choose an option: ");

        switch (choice) {

            case 1:
                adminUserMenu();
                break;

            case 2:
                System.out.println(
                        "\nRoom management will use RoomService."
                );
                break;

            case 3:
                System.out.println(
                        "\nReservation management will use ReservationService."
                );
                break;

            case 4:
                System.out.println(
                        "\nPayment management will use PaymentService."
                );
                break;

            case 5:
                System.out.println(
                        "\nInvoice management will use InvoiceService."
                );
                break;

            case 6:
                System.out.println(
                        "\nRefund management will use RefundService."
                );
                break;

            case 7:
                System.out.println(
                        "\nStatistics / KPI service will be added."
                );
                break;

            case 8:
                logout();
                break;

            default:
                System.out.println(
                        "\nInvalid option."
                );
        }
    }



    private static void showClientMenu() {
        System.out.println("\n====================================================");
        System.out.println("                  CLIENT MENU");
        System.out.println("====================================================");
        System.out.println("Welcome " + loggedInUser.getFirstName() + "!");
        System.out.println("----------------------------------------------------");
        System.out.println("1. Search & Book a Room");
        System.out.println("2. Logout");
        System.out.println("====================================================");

        int choice = readInt("Choose an option: ");
        switch (choice) {
            case 1 -> searchAndBookRoom();
            case 2 -> logout();
            default -> System.out.println("\nInvalid option.");
        }
    }

    private static void searchAndBookRoom() {
        System.out.println("\n--- SEARCH AVAILABLE ROOMS ---");
        try {
            System.out.print("Check-in Date (YYYY-MM-DD): ");
            java.time.LocalDate checkIn = java.time.LocalDate.parse(scanner.nextLine().trim());

            System.out.print("Check-out Date (YYYY-MM-DD): ");
            java.time.LocalDate checkOut = java.time.LocalDate.parse(scanner.nextLine().trim());

            dto.RoomSearchCriteria criteria = new dto.RoomSearchCriteria();
            criteria.setCheckIn(checkIn);
            criteria.setCheckOut(checkOut);

            List<dto.AvailableRoomDTO> rooms = roomRepository.findAvailableRooms(criteria);

            if (rooms.isEmpty()) {
                System.out.println("No rooms available for these dates.");
                return;
            }

            System.out.println("\nAVAILABLE ROOMS:");
            for (dto.AvailableRoomDTO r : rooms) {
                System.out.printf("ID: %d | Room: %s | Type: %s | Base Price: %s MAD/night%n",
                        r.getRoomId(), r.getRoomNumber(), r.getRoomType(), r.getBasePrice());
            }

            System.out.print("\nEnter the Room ID to book (or 0 to cancel): ");
            long roomId = readLong("");
            if (roomId == 0) return;

            Optional<model.Room> selectedRoomOpt = roomRepository.findById(roomId);
            if (selectedRoomOpt.isEmpty()) {
                System.out.println("Invalid Room ID.");
                return;
            }

            model.Room selectedRoom = selectedRoomOpt.get();

            // Calcul Dynamique du prix via le Strategy Pattern
            BigDecimal totalAmount = pricingStrategy.calculateTotalPrice(selectedRoom, checkIn, checkOut);

            System.out.println("\n--- BOOKING SUMMARY ---");
            System.out.println("Total calculated price (with dynamic rules applied): " + totalAmount + " MAD");
            System.out.print("Confirm booking and pay with CARD? (yes/no): ");

            if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                // Déclenchement de la transaction ACID
                bookingTransactionService.processFullBooking(
                        loggedInUser,
                        selectedRoom,
                        checkIn,
                        checkOut,
                        totalAmount,
                        model.enums.PaymentMethod.CARD
                );
            } else {
                System.out.println("Booking cancelled.");
            }

        } catch (Exception e) {
            System.out.println("Error formatting dates. Please use YYYY-MM-DD.");
        }
    }



    private static void adminUserMenu() {

        boolean back = false;

        while (!back && loggedInUser != null) {

            System.out.println();
            System.out.println("========== USER MANAGEMENT ==========");

            System.out.println("1. Find user by ID");
            System.out.println("2. Find user by Email");
            System.out.println("3. Find all users");
            System.out.println("4. Update user");
            System.out.println("5. Delete user");
            System.out.println("6. Back");

            int choice =
                    readInt("Choose an option: ");

            try {

                switch (choice) {

                    case 1:
                        findUserById();
                        break;

                    case 2:
                        findUserByEmail();
                        break;

                    case 3:
                        findAllUsers();
                        break;

                    case 4:
                        updateUser();
                        break;

                    case 5:
                        deleteUser();
                        break;

                    case 6:
                        back = true;
                        break;

                    default:
                        System.out.println(
                                "\nInvalid option."
                        );
                }

            } catch (RuntimeException e) {

                System.out.println(
                        "\nERROR: " + e.getMessage()
                );
            }
        }
    }



    private static void logout() {

        if (loggedInUser != null) {

            System.out.println(
                    "\nGoodbye, "
                            + loggedInUser.getFirstName()
                            + "!"
            );

            loggedInUser = null;

            System.out.println(
                    "You have been logged out."
            );
        }
    }



    private static void findUserById() {

        System.out.println();
        System.out.println("========== FIND USER BY ID ==========");

        Long id =
                readLong("Enter user ID: ");

        Optional<User> result =
                userService.findById(id);

        if (result.isPresent()) {

            printUser(result.get());

        } else {

            System.out.println(
                    "\nUser not found."
            );
        }
    }

    private static void findUserByEmail() {

        System.out.println();
        System.out.println("========== FIND USER BY EMAIL ==========");

        String email =
                readString("Enter email: ");

        Optional<User> result =
                userService.findByEmail(email);

        if (result.isPresent()) {

            printUser(result.get());

        } else {

            System.out.println(
                    "\nUser not found."
            );
        }
    }

    private static void findAllUsers() {

        System.out.println();
        System.out.println("========== ALL USERS ==========");

        List<User> users =
                userService.findAll();

        if (users.isEmpty()) {

            System.out.println(
                    "\nNo users found."
            );

            return;
        }

        printUsers(users);
    }

    private static void updateUser() {

        System.out.println();
        System.out.println("========== UPDATE USER ==========");

        Long id =
                readLong("Enter user ID: ");

        Optional<User> result =
                userService.findById(id);

        if (result.isEmpty()) {

            System.out.println(
                    "\nUser not found."
            );

            return;
        }

        User user = result.get();

        printUser(user);

        String firstName =
                readString("First name: ");

        String lastName =
                readString("Last name: ");

        String email =
                readString("Email: ");

        UserRole role =
                readRole();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setRole(role);

        userService.update(user);

        System.out.println(
                "\nUser updated successfully!"
        );
    }

    private static void deleteUser() {

        System.out.println();
        System.out.println("========== DELETE USER ==========");

        Long id =
                readLong("Enter user ID: ");

        Optional<User> result =
                userService.findById(id);

        if (result.isEmpty()) {

            System.out.println(
                    "\nUser not found."
            );

            return;
        }

        User user = result.get();

        printUser(user);

        String confirmation =
                readString(
                        "Are you sure? (yes/no): "
                );

        if (!confirmation.equalsIgnoreCase("yes")) {

            System.out.println(
                    "\nDelete cancelled."
            );

            return;
        }

        userService.deleteById(id);

        System.out.println(
                "\nUser deleted successfully!"
        );

        if (loggedInUser.getId().equals(id)) {
            loggedInUser = null;
        }
    }



    private static void printUser(User user) {

        System.out.println("---------------------------------------------");
        System.out.println("ID         : " + user.getId());
        System.out.println("First Name : " + user.getFirstName());
        System.out.println("Last Name  : " + user.getLastName());
        System.out.println("Email      : " + user.getEmail());
        System.out.println("Role       : " + user.getRole());
        System.out.println("Created At : " + user.getCreatedAt());
        System.out.println("---------------------------------------------");
    }

    private static void printUsers(List<User> users) {

        for (User user : users) {

            printUser(user);
        }
    }



    private static UserRole readRole() {

        while (true) {

            System.out.println();
            System.out.println("1. ADMIN");
            System.out.println("2. CLIENT");

            int choice =
                    readInt("Choose role: ");

            switch (choice) {

                case 1:
                    return UserRole.ADMIN;

                case 2:
                    return UserRole.CLIENT;

                default:
                    System.out.println(
                            "Invalid role."
                    );
            }
        }
    }


    private static String readString(
            String message
    ) {

        System.out.print(message);

        return scanner.nextLine().trim();
    }

    private static int readInt(
            String message
    ) {

        while (true) {

            try {

                System.out.print(message);

                return Integer.parseInt(
                        scanner.nextLine().trim()
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }

    private static Long readLong(
            String message
    ) {

        while (true) {

            try {

                System.out.print(message);

                return Long.parseLong(
                        scanner.nextLine().trim()
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid ID."
                );
            }
        }
    }
}