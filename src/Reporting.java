

/**
 * CS3431
 * Phase 3 Part 3
 * Zachary Emil and Ethan Chang
 */

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class Reporting{

    public static void main(String[] args) {
        String username, password, command;
        username = "";
        password = "";
        command = "";

        if (args.length == 0 ||  args.length == 1 || args.length > 3) { //either no input or too much input, exit
            System.exit(0);
        } else if (args.length == 2) { // username and password input
            username = args[0];
            password = args[1];
        } else if (args.length == 3) { // username, password, and command input
            username = args[0];
            password = args[1];
            command = args[2];
        }

        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println("Command: " + command);


        System.out.println("-------- Oracle JDBC Connection Testing ------");
        System.out.println("-------- Step 1: Registering Oracle Driver ------");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver? Did you follow the execution steps. ");
            System.out.println("");
            System.out.println("*****Open the file and read the comments in the beginning of the file****");
            System.out.println("");
            e.printStackTrace();
            return;
        }

        System.out.println("Oracle JDBC Driver Registered Successfully !");

        System.out.println("-------- Step 2: Building a Connection ------");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@oracle.wpi.edu:1521:orcl", username, password);

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            System.out.println("You made it. Connection is successful. Take control of your database now!");
        } else {
            System.out.println("Failed to make connection!");
        }



        if (command.equals("")) {
            System.out.println("1- Report Patients Basic Information");
            System.out.println("2- Report Doctors Basic Information");
            System.out.println("3- Report Admissions Information");
            System.out.println("4- Update Admissions Payment");
        }

        else if (command.equals("1")) {

            try {
                PreparedStatement pstmt_SSN = connection.prepareStatement("SELECT * FROM Patient WHERE SSN = ?");

                Scanner sc = new Scanner(System.in);
                System.out.print("Enter Patient SSN: ");
                String ssn = sc.next();

                pstmt_SSN.setString(1, ssn);
                ResultSet rset = pstmt_SSN.executeQuery();

                while(rset.next()) {
                    System.out.println("Patient SSN: " + rset.getString("SSN"));
                    System.out.println("Patient First Name: " + rset.getString("FirstName"));
                    System.out.println("Patient Last Name: " + rset.getString("LastName"));
                    System.out.println("Patient Address: " + rset.getString("Address"));
                }

                System.out.println("Closing set and statement");

                rset.close();
                pstmt_SSN.close();
                System.out.println("Closed set and statement");

            }catch (SQLException e) {
                System.out.println("Bad statement");
                e.printStackTrace();
                return;
            }
        }

        else if (command.equals("2")) {

            try {
                PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Doctor WHERE ID = ?");

                Scanner sc = new Scanner(System.in);
                System.out.print("Enter Doctor ID: ");
                String id = sc.next();

                pstmt.setString(1, id);
                ResultSet rset = pstmt.executeQuery();

                while(rset.next()) {
                    System.out.println("Doctor ID: " + rset.getString("ID"));
                    System.out.println("Doctor First Name: " + rset.getString("FirstName"));
                    System.out.println("Doctor Last Name: " + rset.getString("LastName"));
                    System.out.println("Doctor Gender: " + rset.getString("gender"));
                }


                System.out.println("Closing set and statement");
                rset.close();
                pstmt.close();

                System.out.println("Closed set and statement");


            }catch (SQLException e) {
                System.out.println("Bad statement");
                e.printStackTrace();
                return;
            }
        }
        else if (command.equals("3")) {

            try {
                PreparedStatement pstmt_Admission = connection.prepareStatement("SELECT * "
                        + "FROM Admission "
                        + "WHERE Num = ?");

                PreparedStatement pstmt_StayIn = connection.prepareStatement("SELECT * "
                        + "FROM StayIn "
                        + "WHERE AdmissionNum = ?");

                PreparedStatement pstmt_Examine = connection.prepareStatement("SELECT * "
                        + "FROM Examine "
                        + "WHERE AdmissionNum = ?");

                Scanner sc = new Scanner(System.in);
                System.out.print("Enter Admission Number: ");
                String adNum = sc.next();

                pstmt_Admission.setString(1, adNum);
                pstmt_StayIn.setString(1, adNum);
                pstmt_Examine.setString(1, adNum);

                ResultSet rset_Admission = pstmt_Admission.executeQuery();
                ResultSet rset_StayIn = pstmt_StayIn.executeQuery();
                ResultSet rset_Examine = pstmt_Examine.executeQuery();

                while(rset_Admission.next()) {
                    System.out.println("Admission Number: " + rset_Admission.getString("Num"));
                    System.out.println("Patient SSN: " + rset_Admission.getString("Patient_SSN"));
                    System.out.println("Admission Date (start date): " + rset_Admission.getString("AdmissionDate"));
                    System.out.println("Total Payment: " + rset_Admission.getString("TotalPayment"));
                }


                System.out.println("Rooms:");
                while (rset_StayIn.next()) {
                    String num = rset_StayIn.getString("RoomNum");
                    String from = rset_StayIn.getString("StartDate");
                    String to = rset_StayIn.getString("EndDate");
                    System.out.println("RoomNum: " + num + " FromDate: " + from + " ToDate: " + to );
                }

                System.out.println("Doctors examined the patient in this admission:");
                while (rset_Examine.next()) {
                    String id = rset_Examine.getString("Doctor_ID");
                    System.out.println("Doctor ID: " + id);
                }


                System.out.println("Closing set and statement");

                rset_Admission.close();
                rset_Examine.close();
                rset_StayIn.close();
                pstmt_Admission.close();
                pstmt_StayIn.close();
                pstmt_Examine.close();

                System.out.println("Closed set and statement");


            }catch (SQLException e) {
                System.out.println("Bad statement");
                e.printStackTrace();
                return;
            }
        }

        else if (command.equals("4")) {

            try {
                PreparedStatement pstmt = connection.prepareStatement("UPDATE " +
                        "Admission " +
                        "SET TotalPayment = ? "
                        +"WHERE Num = ?");

                Scanner sc = new Scanner(System.in);
                System.out.print("Enter Admission Number: ");
                String adNum = sc.next();
                System.out.print("Enter the new total payment: ");
                String newTotalPayment = sc.next();

                pstmt.setString(1, newTotalPayment);
                pstmt.setString(2, adNum);

                System.out.println("rows updated: " + pstmt.executeUpdate());


                System.out.println("Closing set and statement");

                pstmt.close();

                System.out.println("Closed set and statement");

            }catch (SQLException e) {
                System.out.println("Bad statement");
                e.printStackTrace();
                return;
            }
        }//end if

        try {
            connection.close();
            System.out.println("Connection closed");

        }catch (SQLException e) {
            System.out.println("Connection Closure Failed! Check output console");
            e.printStackTrace();
            return;
        }//end try-catch

    }//end main
}//end class



