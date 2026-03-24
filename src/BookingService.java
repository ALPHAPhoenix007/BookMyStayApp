import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * Processes booking requests and allocates rooms.
 * Ensures no double booking occurs.
 * Supports validation, history tracking, cancellation, and concurrency.
 */
public class BookingService {

    private RoomInventory inventory;
    private HashMap<String, Set<String>> allocatedRooms;

    // UC8: Booking history
    private BookingHistory history;

    // UC10: Guest → Room mapping
    private HashMap<String, String> reservationToRoom;

    // ✅ CONSTRUCTOR
    public BookingService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        this.allocatedRooms = new HashMap<>();
        this.reservationToRoom = new HashMap<>();
    }

    /**
     * UC1–UC9: Normal FIFO processing
     */
    public void processRequests(Queue<Reservation> requestQueue) {

        while (!requestQueue.isEmpty()) {

            Reservation request = requestQueue.poll();

            try {
                // UC9: Validation
                BookingValidator.validate(request, inventory);

                String roomType = request.getRoomType();
                int available = inventory.getAvailability(roomType);

                if (available > 0) {

                    String roomID = generateRoomID(roomType);

                    allocatedRooms
                            .computeIfAbsent(roomType, k -> new HashSet<>())
                            .add(roomID);

                    inventory.updateAvailability(roomType, available - 1);

                    System.out.println("Reservation Confirmed: "
                            + request.getGuestName()
                            + " -> " + roomType + " [" + roomID + "]");

                    // UC8: History
                    history.addBooking(request);

                    // UC10: Mapping for cancellation
                    reservationToRoom.put(request.getGuestName(), roomID);

                } else {
                    System.out.println("Reservation Failed: "
                            + request.getGuestName()
                            + " -> " + roomType + " (No rooms available)");
                }

            } catch (InvalidBookingException e) {
                System.out.println("Invalid Booking: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }

    /**
     * UC11: Concurrent processing (Thread-safe)
     */
    public void processRequestsConcurrently(BookingRequestQueue bookingQueue) {

        while (true) {

            Reservation request;

            // 🔒 Critical section: Queue access
            synchronized (bookingQueue) {
                request = bookingQueue.getNextRequest();
            }

            if (request == null) {
                break;
            }

            try {
                // Validation
                BookingValidator.validate(request, inventory);

                String roomType = request.getRoomType();

                // 🔒 Critical section: Inventory + Allocation
                synchronized (inventory) {

                    int available = inventory.getAvailability(roomType);

                    if (available > 0) {

                        String roomID = generateRoomID(roomType);

                        allocatedRooms
                                .computeIfAbsent(roomType, k -> new HashSet<>())
                                .add(roomID);

                        inventory.updateAvailability(roomType, available - 1);

                        System.out.println(Thread.currentThread().getName()
                                + " → Confirmed: "
                                + request.getGuestName()
                                + " [" + roomID + "]");

                        history.addBooking(request);
                        reservationToRoom.put(request.getGuestName(), roomID);

                    } else {
                        System.out.println(Thread.currentThread().getName()
                                + " → Failed: "
                                + request.getGuestName());
                    }
                }

            } catch (InvalidBookingException e) {
                System.out.println("Invalid Booking: " + e.getMessage());
            }
        }
    }

    /**
     * Generate unique room ID
     */
    private String generateRoomID(String roomType) {
        Set<String> assigned = allocatedRooms.getOrDefault(roomType, new HashSet<>());
        int counter = assigned.size() + 1;
        return roomType.substring(0, 1).toUpperCase() + counter;
    }

    /**
     * Display allocated rooms
     */
    public void displayAllocatedRooms() {
        System.out.println("\nAllocated Rooms:");
        for (String roomType : allocatedRooms.keySet()) {
            System.out.println(roomType + " Rooms: " + allocatedRooms.get(roomType));
        }
    }

    // ================= UC10: Cancellation Support =================

    public String getAllocatedRoom(String guestName) {
        return reservationToRoom.get(guestName);
    }

    public void removeReservation(String guestName) {
        reservationToRoom.remove(guestName);
    }
}