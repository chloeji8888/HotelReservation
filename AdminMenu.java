import api.AdminResource;
import model.customer.Customer;
import model.room.IRoom;
import model.room.Room;
import model.room.RoomType;

import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.Scanner;

public class AdminMenu {
    private static final AdminResource adminResource = AdminResource.getSINGLETON();

    public static void adminMenu(){
        String line = "";
        final Scanner scanner = new Scanner(System.in);

        printMenu();

        try{
            do{
                line = scanner.nextLine();
                if(line.length() == 1){
                    switch (line.charAt(0)){
                        case'1':
                            displayAllCustomers();
                            break;
                        case'2':
                            displayAllRooms();
                            break;
                        case'3':
                            displayAllReservations();
                            break;
                        case'4':
                            addARoom();
                            break;
                        case'5':
                            MainMenu.printMainMenu();
                            break;
                        default:
                            break;

                    }
                }else{
                    System.out.println("error: invalide action: adminMenu is greater than 1\n");
                }
            }while (line.charAt(0) != 5|| line.length() != 1);
        }catch (StringIndexOutOfBoundsException e){
            System.out.println("Empty input ");
        }
    }
    public static void printMenu(){
        System.out.print("\nAdmin Menu\n" +
                "--------------------------------------------\n" +
                "1. See all Customers\n" +
                "2. See all Rooms\n" +
                "3. See all Reservations\n" +
                "4. Add a Room\n" +
                "5. Back to Main Menu\n" +
                "--------------------------------------------\n" +
                "Please select a number for the menu option:\n");


    }

    private static RoomType enterRoomType(final Scanner scanner){
        try{
            return RoomType.valueOf(scanner.nextLine());
        }catch (IllegalArgumentException ex){
            System.out.println("Invalide room type! Please enter again");
            return enterRoomType(scanner);
        }
    }
    private static double enterRoomPrice(final Scanner scanner){
        try{
            return Double.parseDouble(scanner.nextLine());
        }catch (NumberFormatException ex){
            System.out.println("Invalide room price! Please enter again");
            return enterRoomPrice(scanner);
        }
    }

    private static void addARoom(){
        final Scanner scanner = new Scanner(System.in) ;
        System.out.println("Enter room number: ");
        final String roomNumber = scanner.nextLine();
        System.out.println("Enter price per night: ");
        final double roomPrice = enterRoomPrice(scanner);
        System.out.println("Enter room type : SINGLE/DOUBLE");
        final RoomType roomType = enterRoomType(scanner);

        final Room room = new Room(roomNumber, roomPrice,roomType);
        adminResource.addRoom((Collections.singletonList(room)));
        System.out.println("Room added successfully");
        printMenu();
    }

    private static void displayAllCustomers(){
        Collection<Customer>customers = adminResource.getAllCustomers();
        if(customers.isEmpty()){
            System.out.println("no customers found");
            MainMenu.mainMenu();
        }else{
            //adminResource.getAllCustomers().forEach(System.out::println);
            for(Customer people: customers){
                System.out.println(adminResource.getAllCustomers());
            }
        }
    }

    private static void displayAllRooms(){
        Collection<IRoom>rooms = adminResource.getAllRooms();
        if(rooms.isEmpty()){
            System.out.println("no rooms found");
        }else{
            //adminResource.getAllRooms().forEach(System.out::println);
            for(IRoom room : rooms) {
                System.out.println(adminResource.getAllRooms());
            }
        }
    }

    private static void displayAllReservations(){
        adminResource.displayAllReservations();
    }





}
