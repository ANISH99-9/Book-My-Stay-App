import java.util.*;

// Custom Exception
class CancellationException extends Exception {
    public CancellationException(String message) {
        super(message);
    }
}

// Reservation Class
class Reservation {
    private String bookingId;
    private String customerName;
    private String roomType;
    private String roomId;
    private boolean isCancelled;

    public Reservation(String bookingId, String customerName, String roomType, String roomId) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isCancelled = false;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        this.isCancelled = true;
    }

    @Override
    public String toString() {
        return "BookingID: " + bookingId +
                ", Customer: " + customerName +
                ", RoomType: " + roomType +
                ", RoomID: " + roomId +
                ", Status: " + (isCancelled ? "Cancelled" : "Confirmed");
    }
}

// Inventory Management
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);
    }

    public void increment(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void display() {
        System.out.println("\nUpdated Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }
}

// Booking History
class BookingHistory {
    private Map<String, Reservation> bookings = new HashMap<>();

    public void addReservation(Reservation r) {
        bookings.put(r.getBookingId(), r);
    }

    public Reservation getReservation(String bookingId) {
        return bookings.get(bookingId);
    }

    public void displayAll() {
        System.out.println("\nBooking History:");
        for (Reservation r : bookings.values()) {
            System.out.println(r);
        }
    }
}

// Cancellation Service with Stack (LIFO rollback)
class CancellationService {
    private BookingHistory history;
    private RoomInventory inventory;
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(BookingHistory history, RoomInventory inventory) {
        this.history = history;
        this.inventory = inventory;
    }

    public void cancelBooking(String bookingId) throws CancellationException {

        // Validate existence
        Reservation r = history.getReservation(bookingId);
        if (r == null) {
            throw new CancellationException("Booking does not exist: " + bookingId);
        }

        // Prevent duplicate cancellation
        if (r.isCancelled()) {
            throw new CancellationException("Booking already cancelled: " + bookingId);
        }

        // Step 1: Record room ID in rollback stack
        rollbackStack.push(r.getRoomId());

        // Step 2: Restore inventory
        inventory.increment(r.getRoomType());

        // Step 3: Mark booking as cancelled
        r.cancel();

        System.out.println("Cancellation successful for BookingID: " + bookingId);
    }

    public void showRollbackStack() {
        System.out.println("\nRollback Stack (LIFO): " + rollbackStack);
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();

        // Simulated confirmed bookings
        Reservation r1 = new Reservation("B001", "Arun", "Deluxe", "D101");
        Reservation r2 = new Reservation("B002", "Meena", "Standard", "S201");

        history.addReservation(r1);
        history.addReservation(r2);

        CancellationService service = new CancellationService(history, inventory);

        // Test cancellations
        String[] testCases = {"B001", "B003", "B001"};

        for (String bookingId : testCases) {
            try {
                service.cancelBooking(bookingId);
            } catch (CancellationException e) {
                System.out.println("Cancellation Failed: " + e.getMessage());
            }
        }

        // Display system state
        history.displayAll();
        inventory.display();
        service.showRollbackStack();
    }
}