import java.util.LinkedList;
import java.util.Queue;

public class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // ✅ Thread-safe add (UC11)
    public synchronized void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    // ✅ Thread-safe retrieval (UC11)
    public synchronized Reservation getNextRequest() {
        return requestQueue.poll();
    }

    // Optional: display (read-only)
    public void displayRequests() {
        System.out.println("\nCurrent Booking Requests:");
        for (Reservation r : requestQueue) {
            r.displayReservation();
        }
    }
}