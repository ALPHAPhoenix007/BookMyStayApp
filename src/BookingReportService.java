import java.util.List;

public class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Display all bookings
    public void generateFullReport() {
        System.out.println("\n===== BOOKING REPORT =====");

        List<Reservation> bookings = history.getBookings();

        if (bookings.isEmpty()) {
            System.out.println("No bookings available.");
            return;
        }

        for (Reservation r : bookings) {
            r.displayReservation();
        }
    }

    // Summary report
    public void generateSummary() {
        System.out.println("\n===== BOOKING SUMMARY =====");

        List<Reservation> bookings = history.getBookings();

        System.out.println("Total Confirmed Bookings: " + bookings.size());
    }
}