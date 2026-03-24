import java.util.Arrays;
import java.util.List;

public class BookingValidator {

    private static final List<String> VALID_ROOM_TYPES =
            Arrays.asList("Single", "Double", "Suite");

    public static void validate(Reservation reservation,
                                RoomInventory inventory)
            throws InvalidBookingException {

        if (reservation == null) {
            throw new InvalidBookingException("Reservation cannot be null");
        }

        if (reservation.getGuestName() == null ||
                reservation.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name is required");
        }

        String roomType = reservation.getRoomType();

        if (!VALID_ROOM_TYPES.contains(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        int available = inventory.getAvailability(roomType);

        if (available < 0) {
            throw new InvalidBookingException(
                    "System error: Invalid inventory state for " + roomType);
        }
    }
}