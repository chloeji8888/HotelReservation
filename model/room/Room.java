package model.room;

public class Room implements IRoom{
    public String roomNumber;
    public Double price;
    public RoomType enumeration;

    public Room(String _roomNumber, double _price, RoomType _enumeration) {
        this.roomNumber = _roomNumber;
        this.price = _price;
        this.enumeration = _enumeration;
    }

    @Override
    public String getRoomNumber(){
        return this.roomNumber;
    }

    @Override
    public Double getRoomPrice() {
        return this.price;
    }

    @Override
    public RoomType getRoomType(){
        return this.enumeration;
    }

    @Override
    public boolean isFree(){
        return this.price != null && this.price.equals(0.0);

    }

    @Override
    public String toString(){
        return "Room Number: " + this.roomNumber +
                " Price: $" + this.price +
                " Enumeration: "+ this.enumeration;

    }



}
