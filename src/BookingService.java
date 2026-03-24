import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * Processes booking requests and allocates rooms.
 * Ensures no double booking occurs.
 */
public class BookingService {

    private RoomInventory inventory;
    private HashMap<String, Set<String>> allocatedRooms;

    // UC8: Booking history
    private BookingHistory history;

    // UC10: Track guest → room mapping
    private HashMap<String, String> reservationToRoom;

    // ✅ UPDATED CONSTRUCTOR
    public BookingService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        this.allocatedRooms = new HashMap<>();
        this.reservationToRoom = new HashMap<>();
    }

    /**
     * Processes queued booking requests (FIFO)
     */
    public void processRequests(Queue<Reservation> requestQueue) {

        while (!requestQueue.isEmpty()) {

            Reservation request = requestQueue.poll();

            try {
                // ✅ UC9: Validation
                BookingValidator.validate(request, inventory);

                String roomType = request.getRoomType();
                int available = inventory.getAvailability(roomType);

                if (available > 0) {

                    // Generate room ID
                    String roomID = generateRoomID(roomType);

                    // Allocate room
                    allocatedRooms
                            .computeIfAbsent(roomType, k -> new HashSet<>())
                            .add(roomID);

                    // Update inventory
                    inventory.updateAvailability(roomType, available - 1);

                    System.out.println("Reservation Confirmed: "
                            + request.getGuestName()
                            + " -> " + roomType + " [" + roomID + "]");

                    // ✅ UC8: Add to history
                    history.addBooking(request);

                    // ✅ UC10: Store mapping for cancellation
                    reservationToRoom.put(request.getGuestName(), roomID);

                } else {
                    System.out.println("Reservation Failed: "
                            + request.getGuestName()
                            + " -> " + roomType + " (No rooms available)");
                }

            } catch (InvalidBookingException e) {

                // ✅ Graceful failure
                System.out.println("Invalid Booking: " + e.getMessage());

            } catch (Exception e) {

                // Safety fallback
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }

    /**
     * Generates a unique room ID using room type and counter
     */
    private String generateRoomID(String roomType) {
        Set<String> assigned = allocatedRooms.getOrDefault(roomType, new HashSet<>());
        int counter = assigned.size() + 1;
        return roomType.substring(0, 1).toUpperCase() + counter;
    }

    /**
     * Displays all allocated rooms
     */
    public void displayAllocatedRooms() {
        System.out.println("\nAllocated Rooms:");
        for (String roomType : allocatedRooms.keySet()) {
            System.out.println(roomType + " Rooms: " + allocatedRooms.get(roomType));
        }
    }

    // ================= UC10 METHODS =================

    // Get allocated room for a guest
    public String getAllocatedRoom(String guestName) {
        return reservationToRoom.get(guestName);
    }

    // Remove reservation after cancellation
    public void removeReservation(String guestName) {
        reservationToRoom.remove(guestName);
    }
}