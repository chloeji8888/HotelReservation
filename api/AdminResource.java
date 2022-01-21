package api;


import model.customer.Customer;
import model.room.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.List;

public class AdminResource {
    private static final AdminResource SINGLETON = new AdminResource();
    private final CustomerService customerService = CustomerService.getSingleton();
    private final ReservationService reservationService = ReservationService.getSingleton();

    public Customer getCustomer(String email){
        return customerService.getCustomer(email);
    }
    public void addRoom(List<IRoom>rooms){
        rooms.forEach(reservationService::addRoom);
    }

    public static AdminResource getSINGLETON() {
        return SINGLETON;
    }
    public Collection<IRoom>getAllRooms(){
        return reservationService.getAllRooms();
    }

    public Collection<Customer>getAllCustomers(){
        return customerService.getAllCustomers();

    }
    public void displayAllReservations(){
        reservationService.printAllReservations();

    }

}
