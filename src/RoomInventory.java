import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RoomInventory implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
        availability.put("Single", 5);
        availability.put("Double", 3);
        availability.put("Suite", 2);
    }

    public int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    public void updateAvailability(String roomType, int count) {

        if (count < 0) {
            throw new IllegalArgumentException("Inventory cannot be negative");
        }

        availability.put(roomType, count);
    }

    public Map<String, Integer> getAvailabilityMap() {
        return availability;
    }

    public void setAvailabilityMap(Map<String, Integer> availability) {
        this.availability = availability;
    }

    public void displayInventory() {
        System.out.println("\nCurrent Room Inventory:");
        for (Map.Entry<String, Integer> entry : availability.entrySet()) {
            System.out.println(entry.getKey() + " Rooms Available: " + entry.getValue());
        }
    }
}