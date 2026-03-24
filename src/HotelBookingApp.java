import java.util.Queue;

public class HotelBookingApp {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println(" Welcome to Hotel Booking System ");
        System.out.println(" Version : 1.0");
        System.out.println("=================================");

        System.out.println("Application started successfully.");

        // ================= UC12: LOAD DATA =================
        Object[] data = PersistenceService.load();

        RoomInventory inventory;
        BookingHistory history;

        if (data != null) {
            inventory = (RoomInventory) data[0];
            history = (BookingHistory) data[1];
        } else {
            inventory = new RoomInventory();
            history = new BookingHistory();
        }

        // Room domain objects
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        Room[] rooms = {singleRoom, doubleRoom, suiteRoom};
        String[] roomTypes = {"Single", "Double", "Suite"};

        // Search service
        SearchService searchService = new SearchService(inventory);
        searchService.displayAvailableRooms(rooms, roomTypes);

        // ================= BOOKING QUEUE =================
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        bookingQueue.addRequest(new Reservation("Alice", "Single"));
        bookingQueue.addRequest(new Reservation("Bob", "Double"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite"));
        bookingQueue.addRequest(new Reservation("David", "Single"));
        bookingQueue.addRequest(new Reservation("Eve", "Suite"));

        // ================= UC9: INVALID CASES =================
        bookingQueue.addRequest(new Reservation("", "Single"));
        bookingQueue.addRequest(new Reservation("John", "Deluxe"));

        // ================= BOOKING SERVICE =================
        BookingService bookingService = new BookingService(inventory, history);
        bookingService.processRequests(bookingQueue.getQueue());

        bookingService.displayAllocatedRooms();

        // ================= INVENTORY =================
        System.out.println("\nRemaining Inventory:");
        inventory.displayInventory();

        // ================= UC8: HISTORY + REPORT =================
        history.displayHistory();

        BookingReportService reportService = new BookingReportService(history);
        reportService.generateFullReport();
        reportService.generateSummary();

        // ================= UC7: ADD-ON SERVICES =================
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        AddOnService wifi = new AddOnService("WiFi", 10);
        AddOnService breakfast = new AddOnService("Breakfast", 20);
        AddOnService spa = new AddOnService("Spa", 50);

        serviceManager.addService("Alice", wifi);
        serviceManager.addService("Alice", breakfast);
        serviceManager.addService("Bob", spa);

        serviceManager.displayServices("Alice");
        serviceManager.displayServices("Bob");

        System.out.println("\nTotal Add-On Cost for Alice: $" +
                serviceManager.calculateTotalCost("Alice"));

        System.out.println("Total Add-On Cost for Bob: $" +
                serviceManager.calculateTotalCost("Bob"));

        // ================= UC10: CANCELLATION =================
        CancellationService cancellationService =
                new CancellationService(inventory, bookingService, history);

        cancellationService.cancelBooking("Alice", "Single");
        cancellationService.cancelBooking("Unknown", "Double");

        System.out.println("\nInventory After Cancellation:");
        inventory.displayInventory();

        // ================= UC11: CONCURRENT BOOKING =================
        System.out.println("\n=== Concurrent Booking Simulation ===");

        BookingRequestQueue concurrentQueue = new BookingRequestQueue();

        concurrentQueue.addRequest(new Reservation("User1", "Single"));
        concurrentQueue.addRequest(new Reservation("User2", "Single"));
        concurrentQueue.addRequest(new Reservation("User3", "Single"));
        concurrentQueue.addRequest(new Reservation("User4", "Single"));
        concurrentQueue.addRequest(new Reservation("User5", "Single"));

        Thread t1 = new Thread(() ->
                bookingService.processRequestsConcurrently(concurrentQueue), "Thread-1");

        Thread t2 = new Thread(() ->
                bookingService.processRequestsConcurrently(concurrentQueue), "Thread-2");

        Thread t3 = new Thread(() ->
                bookingService.processRequestsConcurrently(concurrentQueue), "Thread-3");

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nInventory After Concurrent Booking:");
        inventory.displayInventory();

        // ================= UC12: SAVE DATA =================
        PersistenceService.save(inventory, history);

        System.out.println("\nApplication finished.");
    }
}