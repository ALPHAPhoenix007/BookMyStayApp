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

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.allocatedRooms = new HashMap<>();
    }

    /**
     * Processes queued booking requests (FIFO)
     */
    public void processRequests(Queue<Reservation> requestQueue) {
        while (!requestQueue.isEmpty()) {

            Reservation request = requestQueue.poll(); // FIFO
            String roomType = request.getRoomType();
            int available = inventory.getAvailability(roomType);

            if (available > 0) {
                // Generate unique room ID
                String roomID = generateRoomID(roomType);

                // Allocate room
                allocatedRooms.computeIfAbsent(roomType, k -> new HashSet<>()).add(roomID);

                // Update inventory
                inventory.updateAvailability(roomType, available - 1);

                System.out.println("Reservation Confirmed: " + request.getGuestName()
                        + " -> " + roomType + " [" + roomID + "]");
            } else {
                System.out.println("Reservation Failed: " + request.getGuestName()
                        + " -> " + roomType + " (No rooms available)");
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
}