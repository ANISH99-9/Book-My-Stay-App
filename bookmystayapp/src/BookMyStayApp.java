import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Room Inventory (to manage availability safely)
class RoomInventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Standard", 2);
        rooms.put("Deluxe", 2);
        rooms.put("Suite", 1);
    }

    // Validate room type
    public void validateRoomType(String roomType) throws InvalidBookingException {
        if (!rooms.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
    }

    // Validate availability
    public void validateAvailability(String roomType) throws InvalidBookingException {
        if (rooms.get(roomType) <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }
    }

    // Reserve room safely
    public void reserveRoom(String roomType) throws InvalidBookingException {
        validateRoomType(roomType);
        validateAvailability(roomType);

        // Safe update (no negative values)
        rooms.put(roomType, rooms.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Room Inventory:");
        for (String type : rooms.keySet()) {
            System.out.println(type + ": " + rooms.get(type));
        }
    }
}

// Reservation Class
class Reservation {
    private String customerName;
    private String roomType;

    public Reservation(String customerName, String roomType) {
        this.customerName = customerName;
        this.roomType = roomType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Booking Service with Validation
class BookingService {
    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void createBooking(Reservation reservation) throws InvalidBookingException {

        // Fail-fast validation
        if (reservation.getCustomerName() == null || reservation.getCustomerName().isEmpty()) {
            throw new InvalidBookingException("Customer name cannot be empty.");
        }

        if (reservation.getRoomType() == null || reservation.getRoomType().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty.");
        }

        // Validate and reserve
        inventory.reserveRoom(reservation.getRoomType());

        System.out.println("Booking successful for " + reservation.getCustomerName() +
                " (" + reservation.getRoomType() + ")");
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        // Test cases (valid + invalid scenarios)
        Reservation[] testBookings = {
                new Reservation("Arun", "Deluxe"),      // valid
                new Reservation("", "Standard"),        // invalid name
                new Reservation("Meena", "Premium"),    // invalid room type
                new Reservation("Ravi", "Suite"),       // valid
                new Reservation("Kumar", "Suite")       // no availability
        };

        for (Reservation r : testBookings) {
            try {
                bookingService.createBooking(r);
            } catch (InvalidBookingException e) {
                // Graceful failure handling
                System.out.println("Booking Failed: " + e.getMessage());
            }
        }

        // System remains stable
        inventory.displayInventory();
    }
}