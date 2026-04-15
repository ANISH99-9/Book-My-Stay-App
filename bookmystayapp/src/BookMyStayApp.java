import java.util.*;

// Booking Request
class BookingRequest {
    private String customerName;
    private String roomType;

    public BookingRequest(String customerName, String roomType) {
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

// Thread-safe Room Inventory
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);
    }

    // Critical Section (synchronized)
    public synchronized boolean allocateRoom(String roomType) {

        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            // Simulate processing delay (to expose race conditions if unsynchronized)
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public synchronized void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }
}

// Shared Booking Queue
class BookingQueue {
    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        queue.offer(request);
    }

    public synchronized BookingRequest getRequest() {
        return queue.poll();
    }
}

// Booking Processor (Runnable)
class BookingProcessor implements Runnable {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(BookingQueue queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        while (true) {
            BookingRequest request;

            // Fetch request safely
            synchronized (queue) {
                request = queue.getRequest();
            }

            if (request == null) {
                break; // No more requests
            }

            boolean success = inventory.allocateRoom(request.getRoomType());

            if (success) {
                System.out.println(Thread.currentThread().getName() +
                        " SUCCESS: Booked " + request.getRoomType() +
                        " for " + request.getCustomerName());
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " FAILED: No availability for " + request.getRoomType() +
                        " (" + request.getCustomerName() + ")");
            }
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        BookingQueue queue = new BookingQueue();
        RoomInventory inventory = new RoomInventory();

        // Simulate multiple guest requests
        queue.addRequest(new BookingRequest("Arun", "Deluxe"));
        queue.addRequest(new BookingRequest("Meena", "Deluxe"));
        queue.addRequest(new BookingRequest("Ravi", "Deluxe")); // should fail

        queue.addRequest(new BookingRequest("Kumar", "Suite"));
        queue.addRequest(new BookingRequest("Anita", "Suite")); // should fail

        queue.addRequest(new BookingRequest("John", "Standard"));
        queue.addRequest(new BookingRequest("Priya", "Standard"));
        queue.addRequest(new BookingRequest("David", "Standard")); // should fail

        // Create multiple threads (simulating concurrent users)
        Thread t1 = new Thread(new BookingProcessor(queue, inventory), "Thread-1");
        Thread t2 = new Thread(new BookingProcessor(queue, inventory), "Thread-2");
        Thread t3 = new Thread(new BookingProcessor(queue, inventory), "Thread-3");

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for all threads to finish
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final system state
        inventory.displayInventory();
    }
}