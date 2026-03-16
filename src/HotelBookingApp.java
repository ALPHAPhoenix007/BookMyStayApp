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

        // Booking service
        BookingService bookingService = new BookingService(inventory);
        bookingService.processRequests(bookingQueue.getQueue()); // FIFO processing

        // Show allocated rooms
        bookingService.displayAllocatedRooms();

        // Show remaining inventory
        System.out.println("\nRemaining Inventory:");
        inventory.displayInventory();
    }
}