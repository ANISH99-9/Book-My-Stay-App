// ------------------------------------------------------------
// CLASS : RoomSearchService
// ------------------------------------------------------------
// USE CASE 4 : Room Search & Availability Check
//
// Description:
// - Users can search for rooms based on:
//   room type, size, and price range
// - It reads room availability from inventory
//   and filters from database
// - Implements validation and booking logic
// - Uses ArrayList as collection
// ------------------------------------------------------------

import java.util.*;

// ------------------------------------------------------------
// Base Room Class
// ------------------------------------------------------------
class Room {
    protected String type;
    protected int size;
    protected double pricePerNight;
    protected boolean available;

    public Room(String type, int size, double pricePerNight, boolean available) {
        this.type = type;
        this.size = size;
        this.pricePerNight = pricePerNight;
        this.available = available;
    }

    public String getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public boolean isAvailable() {
        return available;
    }

    public void display() {
        System.out.println("Room Type: " + type);
        System.out.println("Size: " + size + " sqft");
        System.out.println("Price per night: " + pricePerNight);
        System.out.println("Available: " + available);
        System.out.println();
    }
}

// ------------------------------------------------------------
// Suite Class (Inheritance)
// ------------------------------------------------------------
class Suite extends Room {
    public Suite(String type, int size, double pricePerNight, boolean available) {
        super(type, size, pricePerNight, available);
    }
}

// ------------------------------------------------------------
// Inventory Class
// ------------------------------------------------------------
// Stores available rooms
// ------------------------------------------------------------
class RoomInventory {
    private List<Room> rooms;

    public RoomInventory() {
        rooms = new ArrayList<>();
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public List<Room> getAllRooms() {
        return rooms;
    }
}

// ------------------------------------------------------------
// RoomSearchService Class
// ------------------------------------------------------------
public class BookMyStayApp {

    // --------------------------------------------------------
    // Search function receives filters:
    // type, size and price range
    // --------------------------------------------------------
    public static List<Room> searchAvailableRooms(
            RoomInventory inventory,
            String type,
            int minSize,
            double maxPrice) {

        List<Room> result = new ArrayList<>();

        // Fetches inventory availability
        List<Room> allRooms = inventory.getAllRooms();

        // Core search logic using filters
        for (Room room : allRooms) {

            // Check availability
            if (!room.isAvailable()) continue;

            // Check type match
            if (!room.getType().equalsIgnoreCase(type)) continue;

            // Check size
            if (room.getSize() < minSize) continue;

            // Check price
            if (room.getPricePerNight() > maxPrice) continue;

            result.add(room);
        }

        return result;
    }

    // --------------------------------------------------------
    // MAIN CLASS : UseCaseRoomSearch
    // --------------------------------------------------------
    // USE CASE 4 : Room Search & Availability Check
    //
    // Description:
    // - Driver class for user interaction
    // - Takes user inputs
    // - Displays filtered room results
    //
    // Duration: 1.0
    // --------------------------------------------------------
    public static void main(String[] args) {

        // Application entry point
        // Reads command-line inputs

        Scanner sc = new Scanner(System.in);

        // Create inventory and add sample rooms
        RoomInventory inventory = new RoomInventory();

        inventory.addRoom(new Room("Single", 100, 1500.0, true));
        inventory.addRoom(new Room("Double", 200, 2500.0, false));
        inventory.addRoom(new Suite("Suite", 350, 5000.0, true));

        // Input from user
        System.out.print("Enter room type: ");
        String type = sc.nextLine();

        System.out.print("Enter minimum size: ");
        int minSize = sc.nextInt();

        System.out.print("Enter max price: ");
        double maxPrice = sc.nextDouble();

        // Search rooms
        List<Room> results = searchAvailableRooms(inventory, type, minSize, maxPrice);

        // Display results
        System.out.println("\n--- Available Rooms ---\n");

        if (results.isEmpty()) {
            System.out.println("No rooms found.");
        } else {
            for (Room room : results) {
                room.display();
            }
        }

        sc.close();
    }
}
