import org.apache.commons.dbcp2.BasicDataSource;
import utilities.InputGetter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class App {

    private static final String[] USER_INFO = {"root", "yearup"};
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sakila";
    private static DataManager dataManager;

    public static void main(String[] args) {
        try (BasicDataSource dataSource = new BasicDataSource()) {
            dataSource.setUrl(DB_URL);
            dataSource.setUsername(USER_INFO[0]);
            dataSource.setPassword(USER_INFO[1]);

            dataManager = new DataManager(dataSource);

            String name = "Uma";

            ArrayList<Actor> actors = dataManager.getActor(name);

            actors.forEach(System.out::println);

        } catch (SQLException e) {
            System.err.println("An error occurred: " + e);
        }
    }

    private static void queryFilmsByActorName(BasicDataSource dataSource, String query) {

        try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT film.film_id, film.title\n" +
                "FROM film\n" +
                "JOIN film_actor ON film_actor.film_id = film.film_id\n" +
                "JOIN actor ON film_actor.actor_id = actor.actor_id\n" +
                "WHERE actor.first_name = \"Uma\" AND actor.last_name = \"Wood\";")) {


            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.printf("Film ID: %d | Title: %s\n", resultSet.getInt(1), resultSet.getString(2));
                }
            }

        } catch (SQLException e) {
            System.err.println("An error occurred: " + e);
        }
    }

    private static void queryActorLastName(BasicDataSource dataSource, String userInput) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT first_name, last_name FROM actor WHERE last_name = ? ORDER BY last_name, first_name;")) {
            preparedStatement.setString(1, userInput);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.printf("%s %s\n", resultSet.getString(1), resultSet.getString(2));
                }
            }

            InputGetter.getString("\nPlease input any character to continue: ");

        } catch (SQLException e) {
            System.err.println("An error occurred: " + e);
        }
    }
}
