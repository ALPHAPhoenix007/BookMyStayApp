/**
 * Handles read-only search operations for available rooms.
 * Ensures inventory data is not modified.
 */
public class SearchService {

    private RoomInventory inventory;

    public SearchService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Displays all available rooms and their details.
     */
    public void displayAvailableRooms(Room[] rooms, String[] roomTypes) {

        System.out.println("\nAvailable Rooms:");

        for (int i = 0; i < rooms.length; i++) {

            int available = inventory.getAvailability(roomTypes[i]);

            // Defensive check: only show rooms with availability > 0
            if (available > 0) {

                System.out.println("\nRoom Type: " + roomTypes[i]);
                rooms[i].displayRoomDetails();
                System.out.println("Available: " + available);
            }
        }
    }
}