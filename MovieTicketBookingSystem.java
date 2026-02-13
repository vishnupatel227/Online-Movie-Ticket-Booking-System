
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.util.regex.Pattern;

public class MovieTicketBookingSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static AuthenticationService authService = new AuthenticationService();
    private static ProfileManager profileManager = new ProfileManager();
    private static BookingService bookingService = new BookingService();
    private static TheaterManager theaterManager = new TheaterManager();
    private static String currentUserId = null;
    
    public static void main(String[] args) {
  
        AnimationUtil.showWelcomeBanner();
        
        System.out.print(AnimationUtil.center("Press Enter to continue..."));
        scanner.nextLine();
        
        
        initializeSystem();
        
        boolean running = true;
        
        while (running) {
            if (currentUserId == null) {
                running = showAuthMenu();
            } else {
                running = showMainMenu();
            }
        }
        
       
        AnimationUtil.clearScreen();
        AnimationUtil.printCenteredWithDelay("", 200);
        AnimationUtil.printCenteredWithDelay("========================================================================================================", 200);
        AnimationUtil.printCenteredWithDelay("", 200);
        AnimationUtil.printCenteredWithDelay(ColorCode.CYAN + ColorCode.BOLD + "Thank you for using Movie Ticket Booking System!" + ColorCode.RESET, 300);
        AnimationUtil.printCenteredWithDelay("", 200);
        AnimationUtil.printCenteredWithDelay(ColorCode.GREEN + "See you at the movies!" + ColorCode.RESET, 300);
        AnimationUtil.printCenteredWithDelay("", 200);
        AnimationUtil.printCenteredWithDelay("========================================================================================================", 200);
        AnimationUtil.printCenteredWithDelay("", 500);
        
        scanner.close();
    }
    
    private static void initializeSystem() {
        theaterManager.initializeTheaters();
    }
    
    private static boolean showAuthMenu() {
        AnimationUtil.clearScreen();
        System.out.println();
        AnimationUtil.printCenteredWithDelay("╔════════════════════════════════════════╗", 100);
        AnimationUtil.printCenteredWithDelay("║        AUTHENTICATION MENU             ║", 100);
        AnimationUtil.printCenteredWithDelay("╠════════════════════════════════════════╣", 100);
        AnimationUtil.printCenteredWithDelay("║ 1. Signup                              ║", 100);
        AnimationUtil.printCenteredWithDelay("║ 2. Login                               ║", 100);
        AnimationUtil.printCenteredWithDelay("║ 3. Exit                                ║", 100);
        AnimationUtil.printCenteredWithDelay("╚════════════════════════════════════════╝", 100);
        System.out.print(AnimationUtil.center("Enter choice: "));
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                handleSignup();
                break;
            case 2:
                handleLogin();
                break;
            case 3:
                return false;
            default:
                System.out.println(AnimationUtil.center(ColorCode.error("Invalid choice!")));
                AnimationUtil.delay(2000);
        }
        return true;
    }
    
    private static boolean showMainMenu() {
        UserProfile profile = profileManager.getProfile(currentUserId);
        BankAccount account = profileManager.getBankAccount(currentUserId);
        
        AnimationUtil.clearScreen();
        System.out.println();
        AnimationUtil.printCenteredWithDelay("╔════════════════════════════════════════╗", 100);
        AnimationUtil.printCenteredWithDelay("║    MOVIE TICKET BOOKING SYSTEM         ║", 100);
        AnimationUtil.printCenteredWithDelay("╠════════════════════════════════════════╣", 100);
        AnimationUtil.printCenteredWithDelay("║ Welcome, " + String.format("%-30s", profile.getName()) + "║", 100);
        AnimationUtil.printCenteredWithDelay("║ Bank Balance: Rs." + String.format("%-21.2f", account.getBalance()) + "║", 100);
        AnimationUtil.printCenteredWithDelay("║ Loyalty Points: " + String.format("%-24d", profile.getLoyaltyPoints()) + "║", 100);
        AnimationUtil.printCenteredWithDelay("╠════════════════════════════════════════╣", 100);
        AnimationUtil.printCenteredWithDelay("║ 1. View Profile                        ║", 100);
        AnimationUtil.printCenteredWithDelay("║ 2. Edit Profile                        ║", 100);
        AnimationUtil.printCenteredWithDelay("║ 3. Booking History                     ║", 100);
        AnimationUtil.printCenteredWithDelay("║ 4. My Preferences                      ║", 100);
        AnimationUtil.printCenteredWithDelay("║ 5. Book New Ticket                     ║", 100);
        AnimationUtil.printCenteredWithDelay("║ 6. Cancel Booking                      ║", 100);
        AnimationUtil.printCenteredWithDelay("║ 7. Bank Account & Transactions         ║", 100);
        AnimationUtil.printCenteredWithDelay("║ 8. User Statistics                     ║", 100);
        AnimationUtil.printCenteredWithDelay("║ 9. Logout                              ║", 100);
        AnimationUtil.printCenteredWithDelay("╚════════════════════════════════════════╝", 100);
        System.out.print(AnimationUtil.center("Enter choice: "));
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                viewProfile();
                break;
            case 2:
                editProfile();
                break;
            case 3:
                showBookingHistoryMenu();
                break;
            case 4:
                showPreferencesMenu();
                break;
            case 5:
                bookNewTicket();
                break;
            case 6:
                cancelBooking();
                break;
            case 7:
                showBankAccountMenu();
                break;
            case 8:
                profileManager.displayUserStatistics(currentUserId);
                waitForEnter();
                break;
            case 9:
                logout();
                break;
            default:
                System.out.println(AnimationUtil.center(ColorCode.error("Invalid choice!")));
                AnimationUtil.delay(2000);
        }
        return true;
    }
    
    private static void handleSignup() {
        AnimationUtil.clearScreen();
        System.out.println(AnimationUtil.center("\n=== SIGNUP ===\n"));
        System.out.print(AnimationUtil.center("Enter your name: "));
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            System.out.println(AnimationUtil.center(ColorCode.error("Name cannot be empty!")));
            waitForEnter();
            return;
        }
        
        System.out.println(AnimationUtil.center("\nChoose signup method:"));
        System.out.println(AnimationUtil.center("1. Email"));
        System.out.println(AnimationUtil.center("2. Mobile Number"));
        System.out.print(AnimationUtil.center("Enter choice: "));
        
        int choice = getIntInput();
        String identifier = "";
        boolean isEmail = false;
        
        if (choice == 1) {
            System.out.print(AnimationUtil.center("Enter email (must end with @gmail.com): "));
            identifier = scanner.nextLine().trim();
            if (!Validator.validateEmail(identifier)) {
                System.out.println(AnimationUtil.center(ColorCode.error("Invalid email! Must end with @gmail.com")));
                waitForEnter();
                return;
            }
            isEmail = true;
        } else if (choice == 2) {
            System.out.print(AnimationUtil.center("Enter mobile number (10 digits, starts with 6/7/8/9): "));
            identifier = scanner.nextLine().trim();
            if (!Validator.validateMobile(identifier)) {
                System.out.println(AnimationUtil.center(ColorCode.error("Invalid mobile number! Must be 10 digits starting with 6, 7, 8, or 9")));
                waitForEnter();
                return;
            }
        } else {
            System.out.println(AnimationUtil.center(ColorCode.error("Invalid choice!")));
            waitForEnter();
            return;
        }
        
        if (authService.userExists(identifier)) {
            System.out.println(AnimationUtil.center(ColorCode.error("User already exists! Please login.")));
            waitForEnter();
            return;
        }
        
        String otp = OTPGenerator.generateOTP();
        System.out.println(AnimationUtil.center("\n" + ColorCode.info("OTP sent to " + (isEmail ? "email" : "mobile") + ": " + otp)));
        System.out.print(AnimationUtil.center("Enter OTP: "));
        String enteredOtp = scanner.nextLine().trim();
        
        if (otp.equals(enteredOtp)) {
            String userId = authService.signup(identifier, name, isEmail);
            profileManager.createProfile(userId, name, isEmail ? identifier : "", isEmail ? "" : identifier);
            System.out.println(AnimationUtil.center("\n" + ColorCode.success("Signup successful! Rs.10,000 added to your account!")));
            AnimationUtil.delay(2000);
        } else {
            System.out.println(AnimationUtil.center(ColorCode.error("Invalid OTP!")));
            waitForEnter();
        }
    }
    
    private static void handleLogin() {
        AnimationUtil.clearScreen();
        System.out.println(AnimationUtil.center("\n=== LOGIN ===\n"));
        System.out.print(AnimationUtil.center("Enter registered mobile number: "));
        String mobile = scanner.nextLine().trim();
        
        if (!Validator.validateMobile(mobile)) {
            System.out.println(AnimationUtil.center(ColorCode.error("Invalid mobile number format!")));
            waitForEnter();
            return;
        }
        
        if (!authService.userExists(mobile)) {
            System.out.println(AnimationUtil.center(ColorCode.error("User not found! Please signup first.")));
            waitForEnter();
            return;
        }
        
        String otp = OTPGenerator.generateOTP();
        System.out.println(AnimationUtil.center("\n" + ColorCode.info("OTP sent to mobile: " + otp)));
        System.out.print(AnimationUtil.center("Enter OTP: "));
        String enteredOtp = scanner.nextLine().trim();
        
        if (otp.equals(enteredOtp)) {
            currentUserId = authService.login(mobile);
            System.out.println(AnimationUtil.center("\n" + ColorCode.success("Login successful!")));
            AnimationUtil.delay(2000);
        } else {
            System.out.println(AnimationUtil.center(ColorCode.error("Invalid OTP!")));
            waitForEnter();
        }
    }
    
    private static void viewProfile() {
        AnimationUtil.clearScreen();
        UserProfile profile = profileManager.getProfile(currentUserId);
        if (profile != null) {
            profile.displayProfile();
        }
        waitForEnter();
    }
    
    private static void editProfile() {
        AnimationUtil.clearScreen();
        UserProfile profile = profileManager.getProfile(currentUserId);
        if (profile == null) return;
        
        System.out.println(AnimationUtil.center("\n=== EDIT PROFILE ==="));
        System.out.println(AnimationUtil.center("1. Update Name"));
        System.out.println(AnimationUtil.center("2. Update Email (OTP required)"));
        System.out.println(AnimationUtil.center("3. Update Mobile (OTP required)"));
        System.out.println(AnimationUtil.center("4. Back"));
        System.out.print(AnimationUtil.center("Enter choice: "));
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                System.out.print(AnimationUtil.center("Enter new name: "));
                String name = scanner.nextLine().trim();
                profile.setName(name);
                System.out.println(AnimationUtil.center(ColorCode.success("Name updated successfully!")));
                waitForEnter();
                break;
            case 2:
                updateEmail(profile);
                break;
            case 3:
                updateMobile(profile);
                break;
        }
    }
    
    private static void updateEmail(UserProfile profile) {
        System.out.print(AnimationUtil.center("Enter new email (must end with @gmail.com): "));
        String email = scanner.nextLine().trim();
        
        if (!Validator.validateEmail(email)) {
            System.out.println(AnimationUtil.center(ColorCode.error("Invalid email format!")));
            waitForEnter();
            return;
        }
        
        String otp = OTPGenerator.generateOTP();
        System.out.println(AnimationUtil.center(ColorCode.info("OTP sent to new email: " + otp)));
        System.out.print(AnimationUtil.center("Enter OTP: "));
        String enteredOtp = scanner.nextLine().trim();
        
        if (otp.equals(enteredOtp)) {
            profile.setEmail(email);
            System.out.println(AnimationUtil.center(ColorCode.success("Email updated successfully!")));
            waitForEnter();
        } else {
            System.out.println(AnimationUtil.center(ColorCode.error("Invalid OTP!")));
            waitForEnter();
        }
    }
    
    private static void updateMobile(UserProfile profile) {
        System.out.print(AnimationUtil.center("Enter new mobile number: "));
        String mobile = scanner.nextLine().trim();
        
        if (!Validator.validateMobile(mobile)) {
            System.out.println(AnimationUtil.center(ColorCode.error("Invalid mobile number format!")));
            waitForEnter();
            return;
        }
        
        String otp = OTPGenerator.generateOTP();
        System.out.println(AnimationUtil.center(ColorCode.info("OTP sent to new mobile: " + otp)));
        System.out.print(AnimationUtil.center("Enter OTP: "));
        String enteredOtp = scanner.nextLine().trim();
        
        if (otp.equals(enteredOtp)) {
            profile.setMobileNumber(mobile);
            System.out.println(AnimationUtil.center(ColorCode.success("Mobile number updated successfully!")));
            waitForEnter();
        } else {
            System.out.println(AnimationUtil.center(ColorCode.error("Invalid OTP!")));
            waitForEnter();
        }
    }
    
    private static void showBookingHistoryMenu() {
        AnimationUtil.clearScreen();
        BookingHistory history = profileManager.getBookingHistory(currentUserId);
        
        System.out.println(AnimationUtil.center("\n=== BOOKING HISTORY ==="));
        System.out.println(AnimationUtil.center("1. View All Bookings"));
        System.out.println(AnimationUtil.center("2. View Active Bookings"));
        System.out.println(AnimationUtil.center("3. View Completed Bookings"));
        System.out.println(AnimationUtil.center("4. View Cancelled Bookings"));
        System.out.println(AnimationUtil.center("5. Back"));
        System.out.print(AnimationUtil.center("Enter choice: "));
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                history.displayHistory();
                waitForEnter();
                break;
            case 2:
                displayFilteredBookings(history.getActiveBookings(), "ACTIVE");
                waitForEnter();
                break;
            case 3:
                displayFilteredBookings(history.getCompletedBookings(), "COMPLETED");
                waitForEnter();
                break;
            case 4:
                displayFilteredBookings(history.getCancelledBookings(), "CANCELLED");
                waitForEnter();
                break;
        }
    }
    
    private static void displayFilteredBookings(List<Booking> bookings, String filter) {
        AnimationUtil.clearScreen();
        System.out.println(AnimationUtil.center("\n=== " + filter + " BOOKINGS ==="));
        if (bookings.isEmpty()) {
            System.out.println(AnimationUtil.center("No " + filter.toLowerCase() + " bookings found."));
            return;
        }
        
        for (Booking booking : bookings) {
            System.out.println(AnimationUtil.center("\n┌─────────────────────────────────────────────────────────┐"));
            System.out.println(AnimationUtil.center("│ Booking ID: " + booking.getBookingId()));
            System.out.println(AnimationUtil.center("│ Movie: " + booking.getMovieName()));
            System.out.println(AnimationUtil.center("│ Theater: " + booking.getTheaterName()));
            System.out.println(AnimationUtil.center("│ Seats: " + booking.getSeatNumbersString()));
            System.out.println(AnimationUtil.center("│ Amount: Rs." + booking.getTotalPrice()));
            System.out.println(AnimationUtil.center("└─────────────────────────────────────────────────────────┘"));
        }
    }
    
    private static void showPreferencesMenu() {
        AnimationUtil.clearScreen();
        UserPreferences prefs = profileManager.getPreferences(currentUserId);
        
        System.out.println(AnimationUtil.center("\n=== USER PREFERENCES ==="));
        System.out.println(AnimationUtil.center("1. View Preferences"));
        System.out.println(AnimationUtil.center("2. Set Favorite Theater"));
        System.out.println(AnimationUtil.center("3. Set Preferred Seat Class"));
        System.out.println(AnimationUtil.center("4. Toggle Notifications"));
        System.out.println(AnimationUtil.center("5. Back"));
        System.out.print(AnimationUtil.center("Enter choice: "));
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                prefs.displayPreferences();
                waitForEnter();
                break;
            case 2:
                setFavoriteTheater(prefs);
                break;
            case 3:
                setPreferredSeatClass(prefs);
                break;
            case 4:
                toggleNotifications(prefs);
                break;
        }
    }
    
    private static void setFavoriteTheater(UserPreferences prefs) {
        System.out.println(AnimationUtil.center("\n=== SELECT FAVORITE THEATER ==="));
        List<Theater> theaters = theaterManager.getAllTheaters();
        for (int i = 0; i < theaters.size(); i++) {
            System.out.println(AnimationUtil.center((i + 1) + ". " + theaters.get(i).getName()));
        }
        System.out.print(AnimationUtil.center("Enter choice: "));
        int choice = getIntInput();
        
        if (choice > 0 && choice <= theaters.size()) {
            prefs.setFavoriteTheater(theaters.get(choice - 1).getName());
            System.out.println(AnimationUtil.center(ColorCode.success("Favorite theater set!")));
            waitForEnter();
        } else {
            System.out.println(AnimationUtil.center(ColorCode.error("Invalid choice!")));
            waitForEnter();
        }
    }
    
    private static void setPreferredSeatClass(UserPreferences prefs) {
        System.out.println(AnimationUtil.center("\n=== SELECT PREFERRED SEAT CLASS ==="));
        System.out.println(AnimationUtil.center("1. UPPER Class"));
        System.out.println(AnimationUtil.center("2. LOWER Class"));
        System.out.print(AnimationUtil.center("Enter choice: "));
        int choice = getIntInput();
        
        if (choice == 1) {
            prefs.setPreferredSeatClass("UPPER");
            System.out.println(AnimationUtil.center(ColorCode.success("Preferred seat class set to UPPER!")));
            waitForEnter();
        } else if (choice == 2) {
            prefs.setPreferredSeatClass("LOWER");
            System.out.println(AnimationUtil.center(ColorCode.success("Preferred seat class set to LOWER!")));
            waitForEnter();
        } else {
            System.out.println(AnimationUtil.center(ColorCode.error("Invalid choice!")));
            waitForEnter();
        }
    }
    
    private static void toggleNotifications(UserPreferences prefs) {
        System.out.println(AnimationUtil.center("\n=== NOTIFICATION SETTINGS ==="));
        System.out.println(AnimationUtil.center("1. Toggle Email Notifications (Currently: " + 
            (prefs.isEmailNotificationsEnabled() ? "ON" : "OFF") + ")"));
        System.out.println(AnimationUtil.center("2. Toggle SMS Notifications (Currently: " + 
            (prefs.isSmsNotificationsEnabled() ? "ON" : "OFF") + ")"));
        System.out.print(AnimationUtil.center("Enter choice: "));
        int choice = getIntInput();
        
        if (choice == 1) {
            prefs.setEmailNotifications(!prefs.isEmailNotificationsEnabled());
            System.out.println(AnimationUtil.center(ColorCode.success("Email notifications " + 
                (prefs.isEmailNotificationsEnabled() ? "enabled" : "disabled") + "!")));
            waitForEnter();
        } else if (choice == 2) {
            prefs.setSmsNotifications(!prefs.isSmsNotificationsEnabled());
            System.out.println(AnimationUtil.center(ColorCode.success("SMS notifications " + 
                (prefs.isSmsNotificationsEnabled() ? "enabled" : "disabled") + "!")));
            waitForEnter();
        }
    }
    
    private static void showBankAccountMenu() {
        AnimationUtil.clearScreen();
        BankAccount account = profileManager.getBankAccount(currentUserId);
        
        System.out.println(AnimationUtil.center("\n=== BANK ACCOUNT MENU ==="));
        System.out.println(AnimationUtil.center("1. View Balance"));
        System.out.println(AnimationUtil.center("2. Transaction History"));
        System.out.println(AnimationUtil.center("3. Add Money"));
        System.out.println(AnimationUtil.center("4. Back"));
        System.out.print(AnimationUtil.center("Enter choice: "));
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                account.displayBalance();
                waitForEnter();
                break;
            case 2:
                account.displayTransactionHistory();
                waitForEnter();
                break;
            case 3:
                addMoney(account);
                break;
        }
    }
    
    private static void addMoney(BankAccount account) {
        System.out.print(AnimationUtil.center("Enter amount to add: Rs."));
        double amount = getDoubleInput();
        
        if (amount > 0 && amount <= 50000) {
            account.credit(amount, "Money added to wallet");
            System.out.println(AnimationUtil.center(ColorCode.success("Rs." + amount + " added successfully!")));
            account.displayBalance();
            waitForEnter();
        } else {
            System.out.println(AnimationUtil.center(ColorCode.error("Invalid amount! (Max: Rs.50,000)")));
            waitForEnter();
        }
    }
    
    private static void bookNewTicket() {
        AnimationUtil.clearScreen();
        System.out.println(AnimationUtil.center("\n=== BOOK NEW TICKET ===\n"));
        
        BankAccount account = profileManager.getBankAccount(currentUserId);
        
       
        List<Theater> theaters = theaterManager.getAllTheaters();
        System.out.println(AnimationUtil.center("Available Theaters:"));
        for (int i = 0; i < theaters.size(); i++) {
            System.out.println(AnimationUtil.center((i + 1) + ". " + theaters.get(i).getName()));
        }
        System.out.print(AnimationUtil.center("Select theater: "));
        int theaterChoice = getIntInput();
        
        if (theaterChoice < 1 || theaterChoice > theaters.size()) {
            System.out.println(AnimationUtil.center(ColorCode.error("Invalid theater selection!")));
            waitForEnter();
            return;
        }
        
        Theater selectedTheater = theaters.get(theaterChoice - 1);
        
        
        List<Show> shows = selectedTheater.getShows();
        System.out.println(AnimationUtil.center("\nAvailable Shows:"));
        for (int i = 0; i < shows.size(); i++) {
            Show show = shows.get(i);
            System.out.println(AnimationUtil.center((i + 1) + ". " + show.getMovieName() + " - " + show.getShowTime()));
        }
        System.out.print(AnimationUtil.center("Select show: "));
        int showChoice = getIntInput();
        
        if (showChoice < 1 || showChoice > shows.size()) {
            System.out.println(AnimationUtil.center(ColorCode.error("Invalid show selection!")));
            waitForEnter();
            return;
        }
        
        Show selectedShow = shows.get(showChoice - 1);
        
        
        System.out.println(AnimationUtil.center("\nSelect Seat Class:"));
        System.out.println(AnimationUtil.center("1. UPPER Class (Rs." + selectedShow.getUpperClassPrice() + " per seat)"));
        System.out.println(AnimationUtil.center("2. LOWER Class (Rs." + selectedShow.getLowerClassPrice() + " per seat)"));
        System.out.print(AnimationUtil.center("Enter choice: "));
        int classChoice = getIntInput();
        
        String seatClass;
        double pricePerSeat;
        if (classChoice == 1) {
            seatClass = "UPPER";
            pricePerSeat = selectedShow.getUpperClassPrice();
        } else if (classChoice == 2) {
            seatClass = "LOWER";
            pricePerSeat = selectedShow.getLowerClassPrice();
        } else {
            System.out.println(AnimationUtil.center(ColorCode.error("Invalid seat class selection!")));
            waitForEnter();
            return;
        }
        
       
        selectedShow.displaySeatLayout(seatClass);
        
        System.out.print(AnimationUtil.center("\nHow many seats do you want to book? "));
        int numSeats = getIntInput();
        
        if (numSeats < 1 || numSeats > 10) {
            System.out.println(AnimationUtil.center(ColorCode.error("Invalid number of seats! (1-10 allowed)")));
            waitForEnter();
            return;
        }
        
        List<Integer> selectedSeats = new ArrayList<>();
        for (int i = 0; i < numSeats; i++) {
            System.out.print(AnimationUtil.center("Enter seat number " + (i + 1) + ": "));
            int seatNum = getIntInput();
            
            if (selectedShow.isSeatAvailable(seatClass, seatNum)) {
                selectedSeats.add(seatNum);
            } else {
                System.out.println(AnimationUtil.center(ColorCode.error("Seat " + seatNum + " is not available!")));
                waitForEnter();
                return;
            }
        }
        
        
        double totalPrice = pricePerSeat * numSeats;
        
        System.out.println(AnimationUtil.center("\n=== PAYMENT SUMMARY ==="));
        System.out.println(AnimationUtil.center("Movie: " + selectedShow.getMovieName()));
        System.out.println(AnimationUtil.center("Theater: " + selectedTheater.getName()));
        System.out.println(AnimationUtil.center("Show Time: " + selectedShow.getShowTime()));
        System.out.println(AnimationUtil.center("Seats: " + selectedSeats));
        System.out.println(AnimationUtil.center("Seat Class: " + seatClass));
        System.out.println(AnimationUtil.center("Total Amount: Rs." + totalPrice));
        System.out.println(AnimationUtil.center("\nYour Bank Balance: Rs." + account.getBalance()));
        
       
        if (!account.hasBalance(totalPrice)) {
            System.out.println(AnimationUtil.center("\n" + ColorCode.error("Insufficient balance!")));
            System.out.println(AnimationUtil.center("Required: Rs." + totalPrice));
            System.out.println(AnimationUtil.center("Available: Rs." + account.getBalance()));
            System.out.println(AnimationUtil.center(ColorCode.warning("Please add money to your account.")));
            waitForEnter();
            return;
        }
        
        System.out.print(AnimationUtil.center("\nConfirm booking? (yes/no): "));
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("yes") || confirm.equals("y")) {
            
            String description = "Ticket booking - " + selectedShow.getMovieName();
            if (account.debit(totalPrice, description)) {
                
                
                for (int seat : selectedSeats) {
                    selectedShow.bookSeat(seatClass, seat);
                }
                
               
                Booking booking = new Booking(currentUserId, selectedTheater.getName(), 
                    selectedShow.getMovieName(), selectedShow.getShowDateTime(), 
                    selectedSeats, seatClass, totalPrice);
                
                profileManager.addBookingToHistory(currentUserId, booking);
                
                
                System.out.println();
                AnimationUtil.printCenteredWithDelay("╔════════════════════════════════════════════════════════╗", 200);
                AnimationUtil.printCenteredWithDelay("║           BOOKING CONFIRMED!                           ║", 200);
                AnimationUtil.printCenteredWithDelay("╠════════════════════════════════════════════════════════╣", 200);
                AnimationUtil.printCenteredWithDelay("║ Booking ID: " + String.format("%-40s", booking.getBookingId()) + "║", 200);
                AnimationUtil.printCenteredWithDelay("║ Amount Paid: Rs." + String.format("%-33.2f", totalPrice) + "║", 200);
                AnimationUtil.printCenteredWithDelay("║ Remaining Balance: Rs." + String.format("%-26.2f", account.getBalance()) + "║", 200);
                AnimationUtil.printCenteredWithDelay("║ Your seats have been booked successfully!              ║", 200);
                AnimationUtil.printCenteredWithDelay("╚════════════════════════════════════════════════════════╝", 200);
                waitForEnter();
            } else {
                System.out.println(AnimationUtil.center(ColorCode.error("Payment failed! Please try again.")));
                waitForEnter();
            }
        } else {
            System.out.println(AnimationUtil.center(ColorCode.error("Booking cancelled.")));
            waitForEnter();
        }
    }
    
    private static void cancelBooking() {
        AnimationUtil.clearScreen();
        BookingHistory history = profileManager.getBookingHistory(currentUserId);
        BankAccount account = profileManager.getBankAccount(currentUserId);
        List<Booking> activeBookings = history.getActiveBookings();
        
        if (activeBookings.isEmpty()) {
            System.out.println(AnimationUtil.center(ColorCode.warning("\nNo active bookings to cancel.")));
            waitForEnter();
            return;
        }
        
        System.out.println(AnimationUtil.center("\n=== CANCEL BOOKING ==="));
        for (int i = 0; i < activeBookings.size(); i++) {
            Booking booking = activeBookings.get(i);
            System.out.println(AnimationUtil.center((i + 1) + ". " + booking.getBookingId() + " - " + 
                booking.getMovieName() + " (Rs." + booking.getTotalPrice() + ")"));
        }
        
        System.out.print(AnimationUtil.center("Select booking to cancel: "));
        int choice = getIntInput();
        
        if (choice < 1 || choice > activeBookings.size()) {
            System.out.println(AnimationUtil.center(ColorCode.error("Invalid choice!")));
            waitForEnter();
            return;
        }
        
        Booking booking = activeBookings.get(choice - 1);
        
        if (booking.canBeCancelled()) {
            if (booking.cancelBooking()) {
                double refund = booking.getRefundAmount();
                account.credit(refund, "Refund - " + booking.getMovieName());
                
                System.out.println(AnimationUtil.center(ColorCode.success("\nBooking cancelled successfully!")));
                System.out.println(AnimationUtil.center("Refund amount: Rs." + refund));
                System.out.println(AnimationUtil.center(ColorCode.success("Credited to your account!")));
                System.out.println(AnimationUtil.center("Current Balance: Rs." + account.getBalance()));
                waitForEnter();
            } else {
                System.out.println(AnimationUtil.center(ColorCode.error("Unable to cancel booking!")));
                waitForEnter();
            }
        } else {
            System.out.println(AnimationUtil.center(ColorCode.error("Booking cannot be cancelled (less than 2 hours until show)!")));
            waitForEnter();
        }
    }
    
    private static void logout() {
        currentUserId = null;
        System.out.println(AnimationUtil.center("\n" + ColorCode.success("Logged out successfully!")));
        AnimationUtil.delay(2000);
    }
    
    private static void waitForEnter() {
        System.out.print(AnimationUtil.center("\nPress Enter to continue..."));
        scanner.nextLine();
    }
    
    private static int getIntInput() {
        try {
            int value = scanner.nextInt();
            scanner.nextLine();
            return value;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }
    }
    
    private static double getDoubleInput() {
        try {
            double value = scanner.nextDouble();
            scanner.nextLine();
            return value;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }
    }
}


class ColorCode {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String BOLD = "\u001B[1m";
    public static final String BLINK = "\u001B[5m";
    
    public static final String ERROR = RED + BOLD + BLINK;
    public static final String SUCCESS = GREEN + BOLD;
    public static final String WARNING = YELLOW + BOLD;
    public static final String INFO = CYAN;
    
    public static String error(String message) {
        return ERROR + "X " + message + RESET;
    }
    
    public static String success(String message) {
        return SUCCESS + message + RESET;
    }
    
    public static String warning(String message) {
        return WARNING + message + RESET;
    }
    
    public static String info(String message) {
        return INFO + message + RESET;
    }
}


class AnimationUtil {
    private static final int TERMINAL_WIDTH = 80;
    
    public static void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public static String center(String text) {
        int padding = (TERMINAL_WIDTH - text.length()) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padding; i++) {
            sb.append(" ");
        }
        sb.append(text);
        return sb.toString();
    }
    
    public static void printWithDelay(String text, int delayMs) {
        System.out.println(text);
        delay(delayMs);
    }
    
    public static void printCenteredWithDelay(String text, int delayMs) {
        System.out.println(center(text));
        delay(delayMs);
    }
    
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    
    public static void showWelcomeBanner() {
        clearScreen();
        
        String[] banner = {
            "========================================================================================================",
            "",
            "        ███╗   ███╗ ██████╗ ██╗   ██╗██╗███████╗    ████████╗██╗ ██████╗██╗  ██╗███████╗████████╗",
            "        ████╗ ████║██╔═══██╗██║   ██║██║██╔════╝    ╚══██╔══╝██║██╔════╝██║ ██╔╝██╔════╝╚══██╔══╝",
            "        ██╔████╔██║██║   ██║██║   ██║██║█████╗         ██║   ██║██║     █████╔╝ █████╗     ██║   ",
            "        ██║╚██╔╝██║██║   ██║╚██╗ ██╔╝██║██╔══╝         ██║   ██║██║     ██╔═██╗ ██╔══╝     ██║   ",
            "        ██║ ╚═╝ ██║╚██████╔╝ ╚████╔╝ ██║███████╗       ██║   ██║╚██████╗██║  ██╗███████╗   ██║   ",
            "        ╚═╝     ╚═╝ ╚═════╝   ╚═══╝  ╚═╝╚══════╝       ╚═╝   ╚═╝ ╚═════╝╚═╝  ╚═╝╚══════╝   ╚═╝   ",
            "",
            "       ██████╗  ██████╗  ██████╗ ██╗  ██╗██╗███╗   ██╗ ██████╗     ███████╗██╗   ██╗███████╗",
            "       ██╔══██╗██╔═══██╗██╔═══██╗██║ ██╔╝██║████╗  ██║██╔════╝     ██╔════╝╚██╗ ██╔╝██╔════╝",
            "       ██████╔╝██║   ██║██║   ██║█████╔╝ ██║██╔██╗ ██║██║  ███╗    ███████╗ ╚████╔╝ ███████╗",
            "       ██╔══██╗██║   ██║██║   ██║██╔═██╗ ██║██║╚██╗██║██║   ██║    ╚════██║  ╚██╔╝  ╚════██║",
            "       ██████╔╝╚██████╔╝╚██████╔╝██║  ██╗██║██║ ╚████║╚██████╔╝    ███████║   ██║   ███████║",
            "       ╚═════╝  ╚═════╝  ╚═════╝ ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝ ╚═════╝     ╚══════╝   ╚═╝   ╚══════╝",
            "",
            "========================================================================================================",
            "",
            "                           Welcome to Movie Ticket Booking System",
            "                                Book Your Favorite Movies Easily!",
            "",
            "========================================================================================================",
            ""
        };
        
        for (String line : banner) {
            printCenteredWithDelay(ColorCode.CYAN + ColorCode.BOLD + line + ColorCode.RESET, 500);
        }
        
        printCenteredWithDelay(ColorCode.YELLOW + "Loading System..." + ColorCode.RESET, 500);
        System.out.println();
        
        String loadingPrefix = center("                    [");
        System.out.print(loadingPrefix);
        for (int i = 0; i < 30; i++) {
            System.out.print(ColorCode.GREEN + "█" + ColorCode.RESET);
            delay(50);
        }
        System.out.println("]");
        System.out.println();
        
        printCenteredWithDelay(ColorCode.SUCCESS + "System Ready!" + ColorCode.RESET, 1000);
        delay(500);
    }
}


