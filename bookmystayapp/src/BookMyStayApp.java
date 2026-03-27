import java.util.*;

/**
 * ============================================================
 * CLASS - Reservation
 * ============================================================
 *
 * Represents a booking request made by a guest.
 */
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

/**
 * ============================================================
 * CLASS - RoomInventory
 * ============================================================
 *
 * Manages available rooms for each type.
 */
class RoomInventory {

    private Map<String, Integer> roomStock;

    public RoomInventory() {
        roomStock = new HashMap<>();

        // Initial stock
        roomStock.put("Single", 2);
        roomStock.put("Double", 2);
        roomStock.put("Suite", 1);
    }

    public boolean isAvailable(String roomType) {
        return roomStock.getOrDefault(roomType, 0) > 0;
    }

    public void bookRoom(String roomType) {
        roomStock.put(roomType, roomStock.get(roomType) - 1);
    }
}

/**
 * ============================================================
 * CLASS - RoomAllocationService
 * ============================================================
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * Description:
 * This class is responsible for confirming
 * booking requests and assigning rooms.
 *
 * - It ensures:
 *   Each room ID is unique
 *   Inventory is updated immediately
 *   No room is double-booked
 *
 * @version 6.0
 */
class RoomAllocationService {

    /**
     * Stores all allocated room IDs to
     * prevent duplicate assignments.
     */
    private Set<String> allocatedRooms;

    /**
     * Stores assigned room IDs by room type.
     *
     * Key   -> Room type
     * Value -> Set of assigned room IDs
     */
    private Map<String, Set<String>> assignedRoomsByType;

    /**
     * Initializes allocation tracking structures.
     */
    public RoomAllocationService() {
        allocatedRooms = new HashSet<>();
        assignedRoomsByType = new HashMap<>();
    }

    /**
     * Confirms a booking request by assigning
     * an available room ID and updating inventory.
     *
     * @param reservation booking request
     * @param inventory   centralized room inventory
     */
    public void allocateRoom(Reservation reservation, RoomInventory inventory) {

        String roomType = reservation.getRoomType();

        if (!inventory.isAvailable(roomType)) {
            System.out.println("No rooms available for type: " + roomType);
            return;
        }

        // Generate unique room ID
        String roomId = generateRoomId(roomType);

        // Track allocation
        allocatedRooms.add(roomId);

        assignedRoomsByType.putIfAbsent(roomType, new HashSet<>());
        assignedRoomsByType.get(roomType).add(roomId);

        // Update inventory
        inventory.bookRoom(roomType);

        System.out.println("Booking confirmed for Guest: "
                + reservation.getGuestName()
                + ", Room ID: " + roomId);
    }

    /**
     * Generates a unique room ID
     * for the given room type.
     *
     * @param roomType type of room
     * @return unique room ID
     */
    private String generateRoomId(String roomType) {
        int count = assignedRoomsByType.getOrDefault(roomType, new HashSet<>()).size() + 1;
        return roomType + "-" + count;
    }
}

/**
 * ============================================================
 * MAIN CLASS - UseCase6RoomAllocation
 * ============================================================
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * Description:
 * This class demonstrates how booking
 * requests are confirmed and rooms
 * are allocated safely.
 *
 * It consumes booking requests in FIFO
 * order and updates inventory immediately.
 *
 * @version 6.0
 */
public class BookMyStayApp {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        System.out.println("Room Allocation Processing");

        // Create inventory
        RoomInventory inventory = new RoomInventory();

        // Create allocation service
        RoomAllocationService service = new RoomAllocationService();

        // Create booking requests
        Reservation r1 = new Reservation("Abhi", "Single");
        Reservation r2 = new Reservation("Subha", "Single");
        Reservation r3 = new Reservation("Vamathi", "Suite");

        // Process bookings
        service.allocateRoom(r1, inventory);
        service.allocateRoom(r2, inventory);
        service.allocateRoom(r3, inventory);
    }
}