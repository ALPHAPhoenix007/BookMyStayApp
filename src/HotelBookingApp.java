public class HotelBookingApp{
    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println(" Welcome to Hotel Booking System ");
        System.out.println(" Version : 1.0");
        System.out.println("=================================");

        System.out.println("Application started successfully.");

        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        RoomInventory inventory = new RoomInventory();

        Room[] rooms = {singleRoom, doubleRoom, suiteRoom};
        String[] roomTypes = {"Single", "Double", "Suite"};

        SearchService searchService = new SearchService(inventory);
        searchService.displayAvailableRooms(rooms, roomTypes);

        // UC5 – Booking request queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        Reservation r1 = new Reservation("Alice", "Single");
        Reservation r2 = new Reservation("Bob", "Double");
        Reservation r3 = new Reservation("Charlie", "Suite");

        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        bookingQueue.displayRequests();
    }
}