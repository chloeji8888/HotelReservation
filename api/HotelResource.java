package api;

import model.customer.Customer;
import model.reservation.Reservation;
import model.room.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class HotelResource {
    private final ReservationService reservationService = ReservationService.getSingleton();
    private final CustomerService customerService= CustomerService.getSingleton();
    private static HotelResource SINGLETON = new HotelResource();

    public static HotelResource getSingleton(){
        return SINGLETON;
    }
    public Customer getCustomer(String email){
        return customerService.getCustomer(email);
    }

    public void createACustomer(String email, String firstName, String lastName)throws IllegalAccessException{
        customerService.addCustomer(email, firstName,lastName);
    }

    public IRoom getRoom(String roomNumber){
        return reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate){
        return reservationService.reserveARoom(getCustomer(customerEmail),room,checkInDate,checkOutDate);

    }

    public Collection<Reservation> getCustomerReservations(String customerEmail){
        final Customer customer = getCustomer(customerEmail);
        if(customer == null){
            return Collections.emptyList();
        }
        return reservationService.getCustomerReservation(getCustomer(customerEmail));
    }
    public Collection<IRoom>findARoom(Date checkInDate, Date checkOutDate){
        return reservationService.findRooms(checkInDate, checkOutDate);
    }
    public Collection<IRoom>findAlternativeRooms(final Date checkInDate, final Date checkOutDate){
        return reservationService.findAlternativeRooms(checkInDate,checkOutDate);
    }
    public Date addDefaultPlusDays(final Date date){
        return reservationService.addDefaultPlusDays(date);
    }

}
