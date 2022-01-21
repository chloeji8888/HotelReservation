import api.HotelResource;
import model.reservation.Reservation;
import model.room.IRoom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class MainMenu {
    private static final String DEFAULT_DATE_FORMATE = "MM/dd/yyyy";
    private static final HotelResource hotelResource = HotelResource.getSingleton();

    public static void mainMenu(){
        String line = "";
        Scanner scanner = new Scanner(System.in);

        printMainMenu();

        try{
            do{
                line = scanner.nextLine();
                if(line.length() == 1){
                    switch (line.charAt(0)){
                        case'1':
                            findAndReserveRoom();
                            break;
                        case'2':
                            seeMyReservation();
                            break;
                        case'3':
                            CreateAccount();
                            break;
                        case'4':
                            AdminMenu.adminMenu();
                            break;
                        case'5':
                           System.out.println("Exit");
                            break;
                        default:
                            System.out.println("Unknow action\n");
                            break;
                    }
                }else{
                    System.out.println("error:Invalide action: input is greatter that 1");
                }
            }while(line.charAt(0) != 5 || line.length()!= 1);
        }catch (StringIndexOutOfBoundsException ex){
            System.out.println("Empty input recieved");
        }
    }

    public static void printMainMenu(){
        System.out.print("\nWelcome to the Hotel Reservation Application\n" +
                "--------------------------------------------\n" +
                "1. Find and reserve a room\n" +
                "2. See my reservations\n" +
                "3. Create an Account\n" +
                "4. Admin\n" +
                "5. Exit\n" +
                "--------------------------------------------\n" +
                "Please select a number for the menu option:\n");
    }

    private static Date getInputDate(final Scanner scanner){
        try{
            return new SimpleDateFormat(DEFAULT_DATE_FORMATE).parse(scanner.nextLine());
        } catch (ParseException e) {
            System.out.println("Error: Invalid Date");
            findAndReserveRoom();
        }
        return null;
    }

    private static void printRooms(final Collection<IRoom>rooms){
        if(rooms.isEmpty()){
            System.out.println("No rooms found");
        }else{
            rooms.forEach(System.out::println);
        }
    }

    private static void findAndReserveRoom() {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Check In Date mm/dd/yyy");
        Date checkIn = getInputDate(scanner);
        System.out.println("Enter Check out Date mm/dd/yyy");
        Date checkOut = getInputDate(scanner);

        while (checkIn != null && checkOut != null) {
            Collection<IRoom>avaliableRooms = hotelResource.findARoom(checkIn, checkOut);
            if (avaliableRooms.isEmpty()) {
                Collection<IRoom>alternativeRooms = hotelResource.findAlternativeRooms(checkIn, checkOut);
                if(alternativeRooms.isEmpty()){
                    System.out.println("No Rooms to any alternation and check in");
                    printMainMenu();
                }else{
                    final Date alternativeCheckIn = hotelResource.addDefaultPlusDays(checkIn);
                    final Date alternativeCheckOut = hotelResource.addDefaultPlusDays(checkOut);
                    System.out.println("We've only found rooms on alternative dates:" +
                            "\nCheck-In Date:" + alternativeCheckIn +
                            "\nCheck-Out Date:" + alternativeCheckOut);
                    printRooms(alternativeRooms);
                    reserveARoom(scanner,checkIn,checkOut,alternativeRooms);
                }
            }else{
                printRooms(avaliableRooms);
                reserveARoom(scanner,checkIn,checkOut,avaliableRooms);
            }
        }
    }

    private static void reserveARoom(final Scanner scanner, final Date checkInDate, final Date checkOutDate, final Collection<IRoom>rooms){
        System.out.println("would you like to book? y/n");
        final String bookRoom = scanner.nextLine();
        if("y".equals(bookRoom)){
            System.out.println("Do you have an account? y/n");
            final String haveAccount = scanner.nextLine();
            if("y".equals(haveAccount)){
                System.out.println("Enter you Email");
                final String customerEmail = scanner.nextLine();
                if(hotelResource.getCustomer(customerEmail) == null){
                    System.out.println("Customer Not Found");
                }else{
                    System.out.println("What room number would you like to reserve?");
                    final String roomNumber = scanner.nextLine();
                    if(rooms.stream().anyMatch(room -> room.getRoomNumber().equals(roomNumber))){
                        final IRoom room = hotelResource.getRoom(roomNumber);
                        final Reservation reservation = hotelResource.bookARoom(customerEmail,room,checkInDate,checkOutDate);
                        System.out.println("Reserved Successfully");
                        System.out.println(reservation);
                    }else{
                        System.out.println("Error: Room number not avaliable. \nStart reserve again");
                    }
                }
                printMainMenu();
            }else{
                System.out.println("Please create an account.");
                printMainMenu();
            }
        }else if("n".equals(bookRoom)){
            printMainMenu();
        }else {
            reserveARoom(scanner,checkInDate,checkOutDate,rooms);
        }
    }

    private static void printReservations(final Collection<Reservation>reservations){
        if(reservations == null || reservations.isEmpty()){
            System.out.println("No reservation found");
        }else{
            reservations.forEach(reservation -> System.out.println("\n" + reservation));
        }
    }

    private static void seeMyReservation(){
        final Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your Email: ");
        final String customerEmail = scanner.nextLine();
        printReservations(hotelResource.getCustomerReservations(customerEmail));
    }

    private static void CreateAccount(){
        final Scanner scanner = new Scanner(System.in);
        System.out.println("Enter you email: ");
        final String customerEmail = scanner.nextLine();
        System.out.println("First Name: ");
        final String firstName = scanner.nextLine();
        System.out.println("Lasr Name: ");
        final String lastName = scanner.nextLine();

        try{
            hotelResource.createACustomer(customerEmail,firstName,lastName);
            System.out.println("Account created successfully");
        } catch (IllegalAccessException e) {
           System.out.println(e.getLocalizedMessage());
           CreateAccount();
        }
    }
}
