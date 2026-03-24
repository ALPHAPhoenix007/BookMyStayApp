import java.util.ArrayList;
import java.util.List;

public class BookingHistory {

    private List<Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new ArrayList<>();
    }

    // Add confirmed booking
    public void addBooking(Reservation reservation) {
        confirmedBookings.add(reservation);
    }

    // Get all bookings
    public List<Reservation> getBookings() {
        return confirmedBookings;
    }

    // Display history
    public void displayHistory() {
        System.out.println("\nBooking History:");

        if (confirmedBookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : confirmedBookings) {
            r.displayReservation();
        }
    }
}