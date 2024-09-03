
package HospitalManagemntSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Doctors {

    private final Connection connection;

    public Doctors(Connection connection) {
        this.connection = connection;
    }

    public void viewDoctors() {
        String query = "SELECT * FROM doctors";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("DOCTORS:  ");
            System.out.println("+-------------+------------------+---------------------+");
            System.out.println("| Doctors ID  | Name             | Specialization      |");
            System.out.println("+-------------+------------------+---------------------+");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");

                // Corrected the format specifier for specialization
                System.out.printf("| %-12d | %-16s | %-19s |\n", id, name, specialization);
            }

            System.out.println("+-------------+------------------+---------------------+");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getDoctorById(int id) {
        if (connection == null) {
            System.out.println("Connection is not initialized.");
            return false;
        }

        String query = "SELECT * FROM doctors WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Displaying the doctor's details
                System.out.println("Doctor Found:");
                System.out.println("ID: " + resultSet.getInt("id"));
                System.out.println("Name: " + resultSet.getString("name"));
                System.out.println("Specialization: " + resultSet.getString("specialization"));
                return true;
            } else {
                System.out.println("No doctor found with ID: " + id);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
