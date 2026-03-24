import java.util.Stack;

public class CancellationService {

    private RoomInventory inventory;
    private BookingService bookingService;
    private BookingHistory history;

    // LIFO rollback
    private Stack<String> rollbackStack;

    public CancellationService(RoomInventory inventory,
                               BookingService bookingService,
                               BookingHistory history) {

        this.inventory = inventory;
        this.bookingService = bookingService;
        this.history = history;
        this.rollbackStack = new Stack<>();
    }

    public void cancelBooking(String guestName, String roomType) {

        // ✅ VALIDATION
        String roomID = bookingService.getAllocatedRoom(guestName);

        if (roomID == null) {
            System.out.println("Cancellation Failed: No booking found for " + guestName);
            return;
        }

        // ✅ PUSH TO STACK (rollback tracking)
        rollbackStack.push(roomID);

        // ✅ RESTORE INVENTORY
        int current = inventory.getAvailability(roomType);
        inventory.updateAvailability(roomType, current + 1);

        // ✅ REMOVE FROM BOOKING SERVICE
        bookingService.removeReservation(guestName);

        // ✅ UPDATE HISTORY (simple message)
        System.out.println("Booking cancelled for " + guestName +
                " | Room Released: " + roomID);

        System.out.println("Rollback Stack: " + rollbackStack);
    }
}