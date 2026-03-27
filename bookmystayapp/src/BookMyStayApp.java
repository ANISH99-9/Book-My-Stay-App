import java.util.LinkedList;
import java.util.Queue;

// Reservation class representing a guest's booking request
class Reservation {
    private String guestName;
    private String roomType;
    private int nights;

    public Reservation(String guestName, String roomType, int nights) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    @Override
    public String toString() {
        return "Reservation [Guest=" + guestName +
                ", RoomType=" + roomType +
                ", Nights=" + nights + "]";
    }
}

// Booking Request Queue manager
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add booking request to queue
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added: " + reservation);
    }

    // View all queued requests
    public void viewQueue() {
        if (requestQueue.isEmpty()) {
            System.out.println("No booking requests in queue.");
            return;
        }

        System.out.println("\nCurrent Booking Request Queue:");
        for (Reservation r : requestQueue) {
            System.out.println(r);
        }
    }

    // Get next request (for future processing)
    public Reservation getNextRequest() {
        return requestQueue.peek(); // does NOT remove
    }
}

// Main class
public class BookMyStayApp{
    public static void main(String[] args) {

        BookingRequestQueue queue = new BookingRequestQueue();

        // Simulating incoming booking requests (arrival order matters)
        Reservation r1 = new Reservation("Alice", "Deluxe", 2);
        Reservation r2 = new Reservation("Bob", "Standard", 1);
        Reservation r3 = new Reservation("Charlie", "Suite", 3);

        // Add requests to queue
        queue.addRequest(r1);
        queue.addRequest(r2);
        queue.addRequest(r3);

        // View queue (FIFO order preserved)
        queue.viewQueue();

        // Peek next request (no allocation yet)
        System.out.println("\nNext request to process: " + queue.getNextRequest());
    }
}