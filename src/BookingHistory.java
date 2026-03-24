import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BookingHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new ArrayList<>();
    }

    public void addBooking(Reservation reservation) {
        confirmedBookings.add(reservation);
    }

    public List<Reservation> getBookings() {
        return confirmedBookings;
    }

    public void displayHistory() {
        System.out.println("\nBooking History:");
        for (Reservation r : confirmedBookings) {
            r.displayReservation();
        }
    }
}