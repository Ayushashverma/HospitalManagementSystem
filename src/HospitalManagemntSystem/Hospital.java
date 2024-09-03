package HospitalManagemntSystem;

import com.mysql.cj.exceptions.ConnectionIsClosedException;

import java.sql.*;
import java.util.Scanner;

public class Hospital {

    private static final String url ="jdbc:mysql://localhost:3306/hospital";

    private static final String username ="root";

    private static final String password ="root";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Patient patient = new Patient(connection, scanner);
            Doctors doctors = new Doctors(connection);
            while (true) {
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1.   ADD PATIENTS");
                System.out.println("2.   VIEW PATIENTS");
                System.out.println("3.   VIEW DOCTORS");
                System.out.println("4.   BOOK APPOINTEMENTS");
                System.out.println("5.   EXIT");
                System.out.println("ENTER YOUR CHOICE");

                int choice = scanner.nextInt();


                switch (choice) {
                    case 1:

                        //add patients
                        patient.addPatient();
                        System.out.println();
                        break;

                    case 2:
                        //view patients
                        patient.viewPatient();
                        System.out.println();
                        break;


                    case 3:

                        //view doctors
                        doctors.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        //Book Appointments
                        bookappointements(patient,doctors,connection,scanner);
                        System.out.println();
                        break;


                    case 5:
                        return;
                    default:
                        System.out.println("ENter a valid choice ");
                        break;

                }


            }
        }   catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void bookappointements( Patient patient,Doctors doctors,Connection connection,Scanner scanner){
        System.out.println("ENTER PATIENTS ID ");
        int patientId = scanner.nextInt();
        System.out.println("ENTER DOCTORS ID");
        int doctorsId = scanner.nextInt();
        System.out.println("ENTER  APPOINTMENT date (YYYY-MM-DD):");
        String appointmentdate = scanner.next();
        if (patient.getPatientsById(patientId)&& doctors.getDoctorById(doctorsId)){
            if (checkDoctorAvailibility(doctorsId,appointmentdate,connection)){
                String appointementQuery ="INSERT INTO appointments( patient_id , doctor_id,appointment_date )VALUES(?, ? ,?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement( appointementQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2,doctorsId);
                    preparedStatement.setString(3,appointmentdate);
                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("APPOINTEMENT BOOKED");
                    }
                    else{
                        System.out.println("FAILED TO BOOK APPOINTEMENT");
                    }

                }
                catch (SQLException  e){
                    e.printStackTrace();
                }


            }else {
                System.out.println("Doctor not available");

            }

        }
        else
        {
            System.out.println("EITHER DOCTOR OR PATIENT DOES NOT EXISTS");
        }

    }
    public  static boolean checkDoctorAvailibility(int doctorId,String appointementDate,Connection connection) {
        String query = "SELECT COUNT (*) FROM  appointments WHERE doctor_id = ?AND  appointment_date =?";
        try {


            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointementDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count == 0) {

                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false;
    }
}
