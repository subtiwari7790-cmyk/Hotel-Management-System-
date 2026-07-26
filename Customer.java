public class Customer {
    private int roomNo;
    private String name;
    private String contact;

    public Customer(int roomNo, String name, String contact){
        this.roomNo = roomNo;
        this.name = name;
        this.contact = contact;
    }

    public int getRoomNo(){ return roomNo; }
    public String getName(){ return name; }
    public String getContact(){ return contact; }
}