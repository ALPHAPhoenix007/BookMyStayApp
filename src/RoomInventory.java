import java.util.HashMap;
import java.util.Map;

/**
 * Manages the availability of rooms in the hotel.
 * Acts as the centralized inventory system.
 */
public class RoomInventory {

    private Map<String, Integer> availability;

    /**
     * Constructor initializes room availability.
     */
    public RoomInventory() {
        availability = new HashMap<>();

        availability.put("Single", 5);
        availability.put("Double", 3);
        availability.put("Suite", 2);
    }

    /**
     * Returns the availability of a specific room type.
     */
    public int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    /**
     * Updates availability after booking or cancellation.
     */
    public void updateAvailability(String roomType, int count) {
        availability.put(roomType, count);
    }

    /**
     * Displays current inventory.
     */
    public void displayInventory() {
        System.out.println("\nCurrent Room Inventory:");

        for (Map.Entry<String, Integer> entry : availability.entrySet()) {
            System.out.println(entry.getKey() + " Rooms Available: " + entry.getValue());
        }
    }
}