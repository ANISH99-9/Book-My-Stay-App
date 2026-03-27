import java.util.*;

class Reservation {

    private String guestName;
    private String roomType;
    private String roomId;

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

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void displayReservation() {
        System.out.println("Guest: " + guestName + ", Room Type: " + roomType + ", Room ID: " + roomId);
    }
}

class Service {

    private String serviceName;
    private double cost;

    public Service(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    public void displayService() {
        System.out.println("Service: " + serviceName + ", Cost: " + cost);
    }
}

class AddOnServiceManager {

    private Map<String, List<Service>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    public void addService(String roomId, Service service) {
        reservationServices.putIfAbsent(roomId, new ArrayList<>());
        reservationServices.get(roomId).add(service);
        System.out.println("Added service " + service.getServiceName() + " to reservation " + roomId);
    }

    public void displayServices(String roomId) {
        List<Service> services = reservationServices.getOrDefault(roomId, new ArrayList<>());
        if (services.isEmpty()) {
            System.out.println("No add-on services selected for reservation " + roomId);
        } else {
            System.out.println("\n--- Add-On Services for Reservation " + roomId + " ---");
            double totalCost = 0;
            for (Service s : services) {
                s.displayService();
                totalCost += s.getCost();
            }
            System.out.println("Total Add-On Cost: " + totalCost);
        }
    }
}

public class BookMyStayApp{

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Hotel Booking System v7.0");

        // Example reservation
        Reservation reservation = new Reservation("Alice", "Single Room");
        reservation.setRoomId("S-12");
        reservation.displayReservation();

        // Add-on services
        Service breakfast = new Service("Breakfast", 200);
        Service spa = new Service("Spa Session", 500);
        Service airportPickup = new Service("Airport Pickup", 300);

        AddOnServiceManager serviceManager = new AddOnServiceManager();

        serviceManager.addService(reservation.getRoomId(), breakfast);
        serviceManager.addService(reservation.getRoomId(), spa);
        serviceManager.addService(reservation.getRoomId(), airportPickup);

        serviceManager.displayServices(reservation.getRoomId());
    }
}