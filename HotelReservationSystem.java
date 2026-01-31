
    import java.sql.*;
import java.util.Scanner;

public class HotelReservationSystem {

    static final String URL = "jdbc:mysql://127.0.0.1:3306/hotel_db";
    static final String USER = "root";
    static final String PASSWORD = "disha_1909";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
             
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to Hotel DB");

            while (true) {
                System.out.println("\n HOTEL RESERVATION SYSTEM ");
                System.out.println("1. Retrieve Guest Info");
                System.out.println("2. Allocate Room");
                System.out.println("3. Get Room Info");
                System.out.println("4. Checkout");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case 1:
                        retrieveGuestInfo(con, sc);
                        break;
                    case 2:
                        allocateRoom(con, sc);
                        break;
                    case 3:
                        getRoomInfo(con, sc);
                        break;
                    case 4:
                        checkout(con, sc);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        con.close();
                        sc.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     
    static void retrieveGuestInfo(Connection con, Scanner sc) throws SQLException {
        String query = "SELECT * FROM reservations";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        if(rs.next()) {
            System.out.println("Reservation ID: " + rs.getInt("reservation_id"));
            System.out.println("Contact: " + rs.getString("contact_number"));
            System.out.println("Address: " + rs.getString("address"));
            System.out.println("Room No: " + rs.getInt("room_number"));
            System.out.println("Check-in: " + rs.getTimestamp("check_in"));
           
        } else {
            System.out.println("Guest not found.");
        }
    }

     
    static void allocateRoom(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Guest Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Contact Number: ");
        String contact = sc.nextLine();
        System.out.println("enter address:");
        String address = sc.nextLine();
        System.out.print("Enter Room Number: ");
        int room = sc.nextInt();
            

        String query = "INSERT INTO reservations (guest_name, contact_number,address, room_number) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, name);
        ps.setString(2, contact);
        ps.setString(3, address);
        ps.setInt(4, room);

        ps.executeUpdate();
        System.out.println("Room allocated successfully!");
       
    }

     
    static void getRoomInfo(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Room Number: ");
        int room = sc.nextInt();
        String query = "SELECT * FROM reservations WHERE room_number=? AND check_out IS NULL";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, room);
        ResultSet rs = ps.executeQuery();

        if(rs.next()) {
            System.out.println("Guest ID: " + rs.getInt("guest_id"));
            System.out.println("Guest Name: " + rs.getString("guest_name"));
            System.out.println("Check-in: " + rs.getTimestamp("check_in"));
        } else {
            System.out.println("Room is empty or checked out.");
        }
    }

     
    static void checkout(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Reservation ID: ");
        int resId = sc.nextInt();
        String query = "UPDATE reservations SET check_out=NOW() WHERE reservation_id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, resId);
        int updated = ps.executeUpdate();
        if(updated > 0) System.out.println("Checkout successful!");
        else System.out.println("Reservation not found.");
    }
}
    

