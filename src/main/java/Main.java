import model.User;
import model.enums.UserRole;
import repository.jdbc.JdbcUserRepository;
import service.AuthService;
import service.PasswordService;
import service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final JdbcUserRepository userRepository =
            new JdbcUserRepository();

    private static final UserService userService =
            new UserService(userRepository);

    private static final AuthService authService =
            new AuthService(
                    userRepository,
                    new PasswordService()
            );

    private static User loggedInUser = null;


    public static void main(String[] args) {

        boolean running = true;

        while (running) {

            // =========================================
            // AUTH MENU
            // =========================================

            if (loggedInUser == null) {

                printAuthMenu();

                int choice = readInt("Choose an option: ");

                try {

                    switch (choice) {

                        case 1:
                            register();
                            break;

                        case 2:
                            login();
                            break;

                        case 3:
                            running = false;
                            System.out.println("\nGoodbye!");
                            break;

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

            }

            // =========================================
            // USER MENU
            // =========================================

            else {

                printUserMenu();

                int choice = readInt("Choose an option: ");

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
                            logout();
                            break;

                        default:
                            System.out.println(
                                    "\nInvalid option. Please choose 1-6."
                            );
                    }

                } catch (RuntimeException e) {

                    System.out.println(
                            "\nERROR: " + e.getMessage()
                    );
                }
            }
        }

        scanner.close();
    }


    // =========================================================
    // AUTH MENU
    // =========================================================

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


    // =========================================================
    // REGISTER
    // =========================================================

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

        UserRole role = readRole();

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

        printUser(registeredUser);
    }


    // =========================================================
    // LOGIN
    // =========================================================

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
                "Welcome, " + user.getFirstName() + "!"
        );
    }


    // =========================================================
    // LOGOUT
    // =========================================================

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


    // =========================================================
    // USER MENU
    // =========================================================

    private static void printUserMenu() {

        System.out.println();
        System.out.println("====================================================");
        System.out.println("                    USER MENU");
        System.out.println("====================================================");

        System.out.println(
                "Logged in as: "
                        + loggedInUser.getFirstName()
                        + " "
                        + loggedInUser.getLastName()
                        + " (" + loggedInUser.getRole() + ")"
        );

        System.out.println("----------------------------------------------------");

        System.out.println("1. Find user by ID");
        System.out.println("2. Find user by Email");
        System.out.println("3. Find all users");
        System.out.println("4. Update user");
        System.out.println("5. Delete user");
        System.out.println("6. Logout");

        System.out.println("====================================================");
    }


    // =========================================================
    // FIND BY ID
    // =========================================================

    private static void findUserById() {

        System.out.println();
        System.out.println("========== FIND USER BY ID ==========");

        Long id =
                readLong("Enter user ID: ");

        Optional<User> result =
                userService.findById(id);

        if (result.isPresent()) {

            System.out.println(
                    "\nUser found successfully!"
            );

            printUser(result.get());

        } else {

            System.out.println(
                    "\nUser not found."
            );
        }
    }


    // =========================================================
    // FIND BY EMAIL
    // =========================================================

    private static void findUserByEmail() {

        System.out.println();
        System.out.println("========== FIND USER BY EMAIL ==========");

        String email =
                readString("Enter email: ");

        Optional<User> result =
                userService.findByEmail(email);

        if (result.isPresent()) {

            System.out.println(
                    "\nUser found successfully!"
            );

            printUser(result.get());

        } else {

            System.out.println(
                    "\nUser not found."
            );
        }
    }


    // =========================================================
    // FIND ALL
    // =========================================================

    private static void findAllUsers() {

        System.out.println();
        System.out.println("========== ALL USERS ==========");

        List<User> users =
                userService.findAll();

        if (users.isEmpty()) {

            System.out.println(
                    "\nNo users found."
            );

        } else {

            System.out.println(
                    "\nUsers found: " + users.size()
            );

            printUsers(users);
        }
    }


    // =========================================================
    // UPDATE
    // =========================================================

    private static void updateUser() {

        System.out.println();
        System.out.println("========== UPDATE USER ==========");

        Long id =
                readLong("Enter user ID to update: ");

        Optional<User> result =
                userService.findById(id);

        if (result.isEmpty()) {

            System.out.println(
                    "\nUser not found."
            );

            return;
        }

        User user = result.get();

        System.out.println();
        System.out.println("Current user:");

        printUser(user);

        System.out.println();
        System.out.println("Enter the new information:");

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

        printUser(user);
    }


    // =========================================================
    // DELETE
    // =========================================================

    private static void deleteUser() {

        System.out.println();
        System.out.println("========== DELETE USER ==========");

        Long id =
                readLong("Enter user ID to delete: ");

        Optional<User> result =
                userService.findById(id);

        if (result.isEmpty()) {

            System.out.println(
                    "\nUser not found."
            );

            return;
        }

        User user = result.get();

        System.out.println();
        System.out.println("User to delete:");

        printUser(user);

        String confirmation =
                readString(
                        "\nAre you sure you want to delete this user? (yes/no): "
                );

        if (confirmation.equalsIgnoreCase("yes")) {

            userService.deleteById(id);

            System.out.println(
                    "\nUser deleted successfully!"
            );

            // If the currently logged-in user deleted himself,
            // log him out.
            if (loggedInUser.getId().equals(id)) {
                loggedInUser = null;
            }

        } else {

            System.out.println(
                    "\nDelete operation cancelled."
            );
        }
    }


    // =========================================================
    // PRINT ONE USER
    // =========================================================

    private static void printUser(User user) {

        printTableHeader();

        printUserRow(user);

        printTableSeparator();
    }


    // =========================================================
    // PRINT MANY USERS
    // =========================================================

    private static void printUsers(List<User> users) {

        printTableHeader();

        for (User user : users) {

            printUserRow(user);
        }

        printTableSeparator();
    }


    // =========================================================
    // TABLE HEADER
    // =========================================================

    private static void printTableHeader() {

        printTableSeparator();

        System.out.printf(
                "| %-3s | %-12s | %-12s | %-25s | %-8s | %-20s |%n",
                "ID",
                "First Name",
                "Last Name",
                "Email",
                "Role",
                "Created At"
        );

        printTableSeparator();
    }


    // =========================================================
    // TABLE ROW
    // =========================================================

    private static void printUserRow(User user) {

        System.out.printf(
                "| %-3s | %-12s | %-12s | %-25s | %-8s | %-20s |%n",
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }


    // =========================================================
    // TABLE SEPARATOR
    // =========================================================

    private static void printTableSeparator() {

        System.out.println(
                "+-----+--------------+--------------+---------------------------+" +
                        "----------+----------------------+"
        );
    }


    // =========================================================
    // READ USER ROLE
    // =========================================================

    private static UserRole readRole() {

        while (true) {

            System.out.println();
            System.out.println("Available roles:");
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
                            "Invalid role. Please choose 1 or 2."
                    );
            }
        }
    }


    // =========================================================
    // READ STRING
    // =========================================================

    private static String readString(String message) {

        System.out.print(message);

        return scanner.nextLine().trim();
    }


    // =========================================================
    // READ INT
    // =========================================================

    private static int readInt(String message) {

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


    // =========================================================
    // READ LONG
    // =========================================================

    private static Long readLong(String message) {

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