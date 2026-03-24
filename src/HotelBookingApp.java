import java.util.Queue;

public class HotelBookingApp{
    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println(" Welcome to Hotel Booking System ");
        System.out.println(" Version : 1.0");
        System.out.println("=================================");

        System.out.println("Application started successfully.");

        // Room domain objects
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        Room[] rooms = {singleRoom, doubleRoom, suiteRoom};
        String[] roomTypes = {"Single", "Double", "Suite"};

        // Inventory
        RoomInventory inventory = new RoomInventory();

        // Search service (optional)
        SearchService searchService = new SearchService(inventory);
        searchService.displayAvailableRooms(rooms, roomTypes);

        // Booking request queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        bookingQueue.addRequest(new Reservation("Alice", "Single"));
        bookingQueue.addRequest(new Reservation("Bob", "Double"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite"));
        bookingQueue.addRequest(new Reservation("David", "Single")); // extra to test FIFO
        bookingQueue.addRequest(new Reservation("Eve", "Suite")); // may fail if only 2 suites
        // ================= UC9: Invalid Test Cases =================
        bookingQueue.addRequest(new Reservation("", "Single"));       // Invalid name
        bookingQueue.addRequest(new Reservation("John", "Deluxe"));   // Invalid room type

        // Booking service
        BookingHistory history = new BookingHistory();
        BookingService bookingService = new BookingService(inventory, history);
        bookingService.processRequests(bookingQueue.getQueue()); // FIFO processing

        // Show allocated rooms
        bookingService.displayAllocatedRooms();

        // Show remaining inventory
        System.out.println("\nRemaining Inventory:");
        inventory.displayInventory();

        // ================= UC7: Add-On Services =================

        AddOnServiceManager serviceManager = new AddOnServiceManager();

// Create services
        AddOnService wifi = new AddOnService("WiFi", 10);
        AddOnService breakfast = new AddOnService("Breakfast", 20);
        AddOnService spa = new AddOnService("Spa", 50);

// Attach services to reservation (using guest name as ID)
        serviceManager.addService("Alice", wifi);
        serviceManager.addService("Alice", breakfast);

        serviceManager.addService("Bob", spa);

// Display services
        serviceManager.displayServices("Alice");
        serviceManager.displayServices("Bob");

// Total cost
        System.out.println("\nTotal Add-On Cost for Alice: $" +
                serviceManager.calculateTotalCost("Alice"));

        System.out.println("Total Add-On Cost for Bob: $" +
                serviceManager.calculateTotalCost("Bob"));
    }
}