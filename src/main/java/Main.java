import dto.AvailableRoomDTO;
import dto.ReservationSummaryDTO;
import dto.RoomSearchCriteria;
import model.Reservation;
import model.Room;
import model.User;
import model.enums.PaymentMethod;
import model.enums.ReservationStatus;
import model.enums.RoomStatus;
import model.enums.RoomType;
import model.enums.UserRole;

import repository.jdbc.JdbcInvoiceRepository;
import repository.jdbc.JdbcPaymentRepository;
import repository.jdbc.JdbcReservationRepository;
import repository.jdbc.JdbcRoomRepository;
import repository.jdbc.JdbcUserRepository;

import service.AuthService;
import service.BookingTransactionService;
import service.InvoiceService;
import service.PasswordService;
import service.PaymentService;
import service.ReservationService;
import service.RoomService;
import service.UserService;
import policy.DynamicPricingStrategy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    // =========================================================
    // REPOSITORIES
    // =========================================================
    private static final JdbcUserRepository userRepository = new JdbcUserRepository();
    private static final JdbcRoomRepository roomRepository = new JdbcRoomRepository();
    private static final JdbcReservationRepository reservationRepository = new JdbcReservationRepository();
    private static final JdbcPaymentRepository paymentRepository = new JdbcPaymentRepository();
    private static final JdbcInvoiceRepository invoiceRepository = new JdbcInvoiceRepository();

    // =========================================================
    // SERVICES & POLICIES
    // =========================================================
    private static final PasswordService passwordService = new PasswordService();
    private static final AuthService authService = new AuthService(userRepository, passwordService);
    private static final UserService userService = new UserService(userRepository);
    private static final RoomService roomService = new RoomService(roomRepository);
    private static final ReservationService reservationService = new ReservationService(reservationRepository, roomRepository, userRepository);
    private static final PaymentService paymentService = new PaymentService(paymentRepository, reservationRepository);
    private static final InvoiceService invoiceService = new InvoiceService(invoiceRepository, paymentRepository);

    private static final DynamicPricingStrategy pricingStrategy = new DynamicPricingStrategy();
    private static final BookingTransactionService bookingTransactionService = new BookingTransactionService();

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

    // =========================================================
    // AUTH MENU
    // =========================================================

    private static boolean showAuthMenu() {
        System.out.println("\n====================================================");
        System.out.println("                    AUTH MENU");
        System.out.println("====================================================");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.println("====================================================");

        int choice = readInt("Choose an option: ");
        try {
            switch (choice) {
                case 1 -> register();
                case 2 -> login();
                case 3 -> { return false; }
                default -> System.out.println("\nInvalid option. Please choose 1-3.");
            }
        } catch (RuntimeException e) {
            System.out.println("\nERROR: " + e.getMessage());
        }
        return true;
    }

    private static void register() {
        System.out.println("\n========== REGISTER ==========");
        String firstName = readString("First name: ");
        String lastName = readString("Last name: ");
        String email = readString("Email: ");
        String password = readString("Password: ");
        UserRole role = readRole();

        User registeredUser = authService.register(firstName, lastName, email, password, role);
        System.out.println("\nUser registered successfully!");
        System.out.println("Account: " + registeredUser.getEmail());
    }

    private static void login() {
        System.out.println("\n========== LOGIN ==========");
        String email = readString("Email: ");
        String password = readString("Password: ");

        loggedInUser = authService.login(email, password);

        System.out.println("\nLogin successful!");
        System.out.println("Welcome, " + loggedInUser.getFirstName() + "!");
        System.out.println("Role: " + loggedInUser.getRole());
    }

    private static void logout() {
        if (loggedInUser != null) {
            System.out.println("\nGoodbye, " + loggedInUser.getFirstName() + "!");
            loggedInUser = null;
            System.out.println("You have been logged out.");
        }
    }

    // =========================================================
    // ROLE MENU
    // =========================================================

    private static void showRoleMenu() {
        try {
            if (loggedInUser.getRole() == UserRole.ADMIN) {
                showAdminMenu();
            } else if (loggedInUser.getRole() == UserRole.CLIENT) {
                showClientMenu();
            } else {
                throw new IllegalStateException("Unknown user role.");
            }
        } catch (RuntimeException e) {
            System.out.println("\nERROR: " + e.getMessage());
        }
    }

    // =========================================================
    // ADMIN MENU
    // =========================================================

    private static void showAdminMenu() {
        System.out.println("\n====================================================");
        System.out.println("                   ADMIN MENU");
        System.out.println("====================================================");
        System.out.println("Logged in as: " + loggedInUser.getFirstName() + " " + loggedInUser.getLastName());
        System.out.println("----------------------------------------------------");
        System.out.println("1. Manage users");
        System.out.println("2. Manage rooms");
        System.out.println("3. Manage reservations");
        System.out.println("4. Manage payments (WIP)");
        System.out.println("5. Manage invoices (WIP)");
        System.out.println("6. Logout");
        System.out.println("====================================================");

        int choice = readInt("Choose an option: ");
        switch (choice) {
            case 1 -> adminUserMenu();
            case 2 -> adminRoomMenu();
            case 3 -> adminReservationMenu();
            case 4 -> System.out.println("\nPayment management will use PaymentService.");
            case 5 -> System.out.println("\nInvoice management will use InvoiceService.");
            case 6 -> logout();
            default -> System.out.println("\nInvalid option.");
        }
    }

    // --- ADMIN: USERS ---
    private static void adminUserMenu() {
        boolean back = false;
        while (!back && loggedInUser != null) {
            System.out.println("\n========== USER MANAGEMENT ==========");
            System.out.println("1. Find user by ID");
            System.out.println("2. Find user by Email");
            System.out.println("3. Find all users");
            System.out.println("4. Update user");
            System.out.println("5. Delete user");
            System.out.println("6. Back");

            int choice = readInt("Choose an option: ");
            try {
                switch (choice) {
                    case 1 -> findUserById();
                    case 2 -> findUserByEmail();
                    case 3 -> findAllUsers();
                    case 4 -> updateUser();
                    case 5 -> deleteUser();
                    case 6 -> back = true;
                    default -> System.out.println("\nInvalid option.");
                }
            } catch (RuntimeException e) {
                System.out.println("\nERROR: " + e.getMessage());
            }
        }
    }

    // --- ADMIN: ROOMS ---
    private static void adminRoomMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n========== ROOM MANAGEMENT ==========");
            System.out.println("1. List all rooms");
            System.out.println("2. Add new room");
            System.out.println("3. Change room status");
            System.out.println("4. Delete room");
            System.out.println("5. Back");

            int choice = readInt("Choose an option: ");
            try {
                switch (choice) {
                    case 1 -> {
                        List<Room> rooms = roomService.findAll();
                        System.out.println("\n--- ALL ROOMS ---");
                        rooms.forEach(r -> System.out.printf("ID: %d | N° %s | Type: %s | %s MAD | Status: %s%n",
                                r.getId(), r.getRoomNumber(), r.getType(), r.getBasePrice(), r.getStatus()));
                    }
                    case 2 -> {
                        String number = readString("Room Number: ");
                        RoomType type = RoomType.valueOf(readString("Type (SINGLE, DOUBLE, SUITE): ").toUpperCase());
                        int capacity = readInt("Capacity (persons): ");
                        BigDecimal price = new BigDecimal(readString("Base Price: "));
                        String desc = readString("Description: ");

                        Room newRoom = new Room(null, number, type, capacity, price, RoomStatus.AVAILABLE, desc);
                        roomService.create(newRoom);
                        System.out.println("\nRoom created successfully.");
                    }
                    case 3 -> {
                        long roomId = readLong("Room ID to update: ");
                        RoomStatus status = RoomStatus.valueOf(readString("New Status (AVAILABLE, OCCUPIED, MAINTENANCE): ").toUpperCase());
                        roomService.changeStatus(roomId, status);
                        System.out.println("\nRoom status updated.");
                    }
                    case 4 -> {
                        long roomId = readLong("Room ID to delete: ");
                        roomService.deleteById(roomId);
                        System.out.println("\nRoom deleted successfully.");
                    }
                    case 5 -> back = true;
                    default -> System.out.println("\nInvalid option.");
                }
            } catch (Exception e) {
                System.out.println("\nERROR: " + e.getMessage());
            }
        }
    }

    // --- ADMIN: RESERVATIONS ---
    private static void adminReservationMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n========== RESERVATION MANAGEMENT ==========");
            System.out.println("1. View pending reservations");
            System.out.println("2. Confirm a reservation");
            System.out.println("3. Cancel a reservation");
            System.out.println("4. Back");

            int choice = readInt("Choose an option: ");
            try {
                switch (choice) {
                    case 1 -> {
                        List<Reservation> pending = reservationService.findByStatus(ReservationStatus.PENDING);
                        if (pending.isEmpty()) System.out.println("\nNo pending reservations.");
                        pending.forEach(r -> System.out.printf("Res ID: %d | User ID: %d | Room ID: %d | %s to %s | Amount: %s%n",
                                r.getId(), r.getUserId(), r.getRoomId(), r.getCheckIn(), r.getCheckOut(), r.getTotalAmount()));
                    }
                    case 2 -> {
                        long resId = readLong("Reservation ID to confirm: ");
                        reservationService.confirm(resId);
                        System.out.println("\nReservation confirmed.");
                    }
                    case 3 -> {
                        long resId = readLong("Reservation ID to cancel: ");
                        reservationService.cancel(resId);
                        System.out.println("\nReservation cancelled.");
                    }
                    case 4 -> back = true;
                    default -> System.out.println("\nInvalid option.");
                }
            } catch (Exception e) {
                System.out.println("\nERROR: " + e.getMessage());
            }
        }
    }

    // =========================================================
    // CLIENT MENU
    // =========================================================

    private static void showClientMenu() {
        System.out.println("\n====================================================");
        System.out.println("                  CLIENT MENU");
        System.out.println("====================================================");
        System.out.println("Welcome " + loggedInUser.getFirstName() + "!");
        System.out.println("----------------------------------------------------");
        System.out.println("1. View Available Rooms");
        System.out.println("2. Make a Reservation");
        System.out.println("3. My Reservations");
        System.out.println("4. Logout");
        System.out.println("====================================================");

        int choice = readInt("Choose an option: ");
        switch (choice) {
            case 1 -> searchAvailableRooms();
            case 2 -> makeReservation();
            case 3 -> viewMyReservations();
            case 4 -> logout();
            default -> System.out.println("\nInvalid option.");
        }
    }

    private static void searchAvailableRooms() {
        System.out.println("\n--- SEARCH AVAILABLE ROOMS ---");
        try {
            String checkInStr = readString("Check-in Date (YYYY-MM-DD): ");
            String checkOutStr = readString("Check-out Date (YYYY-MM-DD): ");

            LocalDate checkIn = LocalDate.parse(checkInStr);
            LocalDate checkOut = LocalDate.parse(checkOutStr);

            RoomSearchCriteria criteria = new RoomSearchCriteria();
            criteria.setCheckIn(checkIn);
            criteria.setCheckOut(checkOut);

            List<AvailableRoomDTO> rooms = roomService.findAvailableRooms(criteria);

            if (rooms.isEmpty()) {
                System.out.println("\nNo rooms available for these dates.");
                return;
            }

            System.out.println("\n--- AVAILABLE ROOMS ---");
            for (AvailableRoomDTO r : rooms) {
                System.out.printf("ID: %d | Room: %s | Type: %s | Base Price: %s MAD/night%n",
                        r.getRoomId(), r.getRoomNumber(), r.getRoomType(), r.getBasePrice());
            }
        } catch (Exception e) {
            System.out.println("\nError formatting dates. Please use YYYY-MM-DD.");
        }
    }

    private static void makeReservation() {
        System.out.println("\n--- MAKE A RESERVATION ---");
        try {
            long roomId = readLong("Enter the Room ID you want to book: ");
            String checkInStr = readString("Check-in Date (YYYY-MM-DD): ");
            String checkOutStr = readString("Check-out Date (YYYY-MM-DD): ");

            LocalDate checkIn = LocalDate.parse(checkInStr);
            LocalDate checkOut = LocalDate.parse(checkOutStr);

            Optional<Room> selectedRoomOpt = roomService.findById(roomId);
            if (selectedRoomOpt.isEmpty()) {
                System.out.println("\nInvalid Room ID.");
                return;
            }

            Room selectedRoom = selectedRoomOpt.get();

            // Calcul Dynamique du prix via le Strategy Pattern
            BigDecimal totalAmount = pricingStrategy.calculateTotalPrice(selectedRoom, checkIn, checkOut);

            System.out.println("\n--- BOOKING SUMMARY ---");
            System.out.println("Total calculated price (with dynamic rules): " + totalAmount + " MAD");
            String confirm = readString("Confirm booking and pay with CARD? (yes/no): ");

            if (confirm.equalsIgnoreCase("yes")) {
                bookingTransactionService.processFullBooking(
                        loggedInUser, selectedRoom, checkIn, checkOut, totalAmount, PaymentMethod.CARD
                );
            } else {
                System.out.println("\nBooking cancelled.");
            }
        } catch (Exception e) {
            System.out.println("\nError during reservation. Ensure dates are YYYY-MM-DD.");
        }
    }

    private static void viewMyReservations() {
        System.out.println("\n--- MY RESERVATIONS ---");
        List<ReservationSummaryDTO> myReservations = reservationRepository.findByUserId(loggedInUser.getId());

        if (myReservations.isEmpty()) {
            System.out.println("You have no reservations.");
            return;
        }

        for (ReservationSummaryDTO res : myReservations) {
            System.out.printf("Res ID: %d | Room: %s | %s to %s | Status: %s | Total: %s MAD%n",
                    res.getReservationId(), res.getRoomNumber(), res.getCheckIn(), res.getCheckOut(), res.getStatus(), res.getTotalAmount());
        }
    }

    // =========================================================
    // USER OPERATIONS (ADMIN)
    // =========================================================

    private static void findUserById() {
        System.out.println("\n========== FIND USER BY ID ==========");
        Long id = readLong("Enter user ID: ");
        Optional<User> result = userService.findById(id);
        if (result.isPresent()) printUser(result.get());
        else System.out.println("\nUser not found.");
    }

    private static void findUserByEmail() {
        System.out.println("\n========== FIND USER BY EMAIL ==========");
        String email = readString("Enter email: ");
        Optional<User> result = userService.findByEmail(email);
        if (result.isPresent()) printUser(result.get());
        else System.out.println("\nUser not found.");
    }

    private static void findAllUsers() {
        System.out.println("\n========== ALL USERS ==========");
        List<User> users = userService.findAll();
        if (users.isEmpty()) System.out.println("\nNo users found.");
        else printUsers(users);
    }

    private static void updateUser() {
        System.out.println("\n========== UPDATE USER ==========");
        Long id = readLong("Enter user ID: ");
        Optional<User> result = userService.findById(id);
        if (result.isEmpty()) {
            System.out.println("\nUser not found.");
            return;
        }
        User user = result.get();
        printUser(user);

        String firstName = readString("First name: ");
        String lastName = readString("Last name: ");
        String email = readString("Email: ");
        UserRole role = readRole();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setRole(role);

        userService.update(user);
        System.out.println("\nUser updated successfully!");
    }

    private static void deleteUser() {
        System.out.println("\n========== DELETE USER ==========");
        Long id = readLong("Enter user ID: ");
        Optional<User> result = userService.findById(id);
        if (result.isEmpty()) {
            System.out.println("\nUser not found.");
            return;
        }
        User user = result.get();
        printUser(user);

        String confirmation = readString("Are you sure? (yes/no): ");
        if (!confirmation.equalsIgnoreCase("yes")) {
            System.out.println("\nDelete cancelled.");
            return;
        }

        userService.deleteById(id);
        System.out.println("\nUser deleted successfully!");

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

    // =========================================================
    // INPUT UTILS
    // =========================================================

    private static UserRole readRole() {
        while (true) {
            System.out.println("\n1. ADMIN");
            System.out.println("2. CLIENT");
            int choice = readInt("Choose role: ");
            switch (choice) {
                case 1 -> { return UserRole.ADMIN; }
                case 2 -> { return UserRole.CLIENT; }
                default -> System.out.println("Invalid role.");
            }
        }
    }

    private static String readString(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    private static int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static Long readLong(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid ID.");
            }
        }
    }
}