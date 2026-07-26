import java.util.*;
import java.io.*;

public class BookingManager {

    ArrayList<Room> rooms = new ArrayList<>();
    ArrayList<Customer> customers = new ArrayList<>();

    double revenue = 0;

    public void addRoom(Room r){
        rooms.add(r);
        saveToFile();
    }

    public boolean bookRoom(int roomNo, String name, String contact){
        for(Room r: rooms){
            if(r.getRoomNo() == roomNo && r.isAvailable()){
                r.setAvailable(false);
                customers.add(new Customer(roomNo, name, contact));
                saveToFile();
                return true;
            }
        }
        return false;
    }

    public void checkout(int roomNo){
        for(Room r: rooms){
            if(r.getRoomNo() == roomNo && !r.isAvailable()){
                r.setAvailable(true);
                revenue += r.getPrice();
            }
        }
        customers.removeIf(c -> c.getRoomNo() == roomNo);
        saveToFile();
    }

    public double calculateBill(int roomNo, int days){
        for(Room r: rooms){
            if(r.getRoomNo() == roomNo){
                return r.getPrice() * days;
            }
        }
        return 0;
    }

    public void saveToFile(){
        try{
            FileWriter fw = new FileWriter("data.txt");
            for(Room r: rooms){
                fw.write(r.getRoomNo()+","+r.getType()+","+r.getPrice()+","+r.isAvailable()+"\n");
            }
            fw.close();
        }catch(Exception e){}
    }

    public void loadFromFile(){
        try{
            BufferedReader br = new BufferedReader(new FileReader("data.txt"));
            String line;
            while((line = br.readLine()) != null){
                String[] d = line.split(",");
                Room r = new Room(Integer.parseInt(d[0]), d[1], Double.parseDouble(d[2]));
                r.setAvailable(Boolean.parseBoolean(d[3]));
                rooms.add(r);
            }
            br.close();
        }catch(Exception e){}
    }

    public ArrayList<Room> getRooms(){ return rooms; }
    public ArrayList<Customer> getCustomers(){ return customers; }
    public double getRevenue(){ return revenue; }
}