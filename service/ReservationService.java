package service;

import model.customer.Customer;
import model.reservation.Reservation;
import model.room.IRoom;

import java.util.*;
import java.util.stream.Collectors;

public class ReservationService {

    private static final int RECOMMENDED_ROOMS_DEFAULT_PLUS_DAYS = 7;
    private static final ReservationService SINGLETON = new ReservationService();
    private final Map<String, IRoom> rooms = new HashMap<>();
    private final Map<String, Collection<Reservation>> reservations = new HashMap<>();


    public static ReservationService getSingleton(){
        return SINGLETON;
    }
    public void addRoom(IRoom room){
        rooms.put(room.getRoomNumber(),room);

    }

    public IRoom getARoom(String roomId){
        return rooms.get(roomId);

    }

    public Collection<IRoom> getAllRooms(){
        return rooms.values();
    }
    public Collection<Reservation> getCustomerReservation(Customer customer){
        return reservations.get(customer.getEmail());

    }
    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate){
        final Reservation reservation = new Reservation(customer,room, checkInDate, checkOutDate);
        Collection<Reservation>customerReservation = getCustomerReservation(customer);
        if(customerReservation == null){
            customerReservation = new LinkedList<>();
        }
        customerReservation.add(reservation);
        reservations.put(customer.getEmail(),customerReservation);
        return reservation;
    }

    public boolean reservationOverLaps(final Date checkInDate, final Date checkOutDate, final Reservation reservation){
        return checkInDate.before(reservation.getCheckOutDate()) && checkOutDate.after(reservation.getCheckInDate());
    }
    private Collection<IRoom>findAvaliableRooms(final Date checkInDate,final Date checkOutDate){
        final Collection<Reservation>allReservations = getAllReservation();
        final Collection<IRoom>notAvaliableRooms = new LinkedList<>();
        for(Reservation reservation1 : allReservations){
            if(reservationOverLaps(checkInDate,checkOutDate, reservation1)){
                notAvaliableRooms.add(reservation1.getRoom());
            }
        }
        return rooms.values().stream().filter(room -> notAvaliableRooms.stream()
                        .noneMatch(notAvailableRoom -> notAvailableRoom.equals(room)))
                .collect(Collectors.toList());
    }

    public Collection<IRoom>findRooms(final Date checkInDate, final Date checkOutDate){
        return findAvaliableRooms(checkInDate,checkOutDate);
    }

    public Collection<Reservation> getAllReservation(){
        final Collection<Reservation>allReservations = new LinkedList<>();
        for(Collection<Reservation> reservation: reservations.values()){
            allReservations.addAll(reservation);
        }
        return allReservations;
    }

    public Date addDefaultPlusDays(final Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, RECOMMENDED_ROOMS_DEFAULT_PLUS_DAYS);
        return calendar.getTime();
    }
    public Collection<IRoom> findAlternativeRooms(final Date checkInDate, final Date checkOutDate) {
        return findAvaliableRooms(addDefaultPlusDays(checkInDate), addDefaultPlusDays(checkOutDate));
    }

    public void printAllReservations(){
        final Collection<Reservation>reservations = getAllReservation();
        if(reservations.isEmpty()){
            System.out.println("No reservation found");
        }else{
            for(Reservation reservation:reservations){
                System.out.println(reservation + "\n");
            }
        }
    }
}

