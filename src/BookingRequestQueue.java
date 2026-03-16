import java.util.LinkedList;
import java.util.Queue;

public class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    public void displayRequests() {
        System.out.println("\nCurrent Booking Requests:");
        for (Reservation r : requestQueue) {
            r.displayReservation();
        }
    }

    // <<< THIS IS THE FIX >>>
    // Add this public getter so BookingService can access the queue
    public Queue<Reservation> getQueue() {
        return requestQueue;
    }
}