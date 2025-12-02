import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataManager {

    private BasicDataSource dataSource;

    public DataManager(BasicDataSource dataSource) {
        this.dataSource = dataSource;

    }

    public ArrayList<Actor> getActor(String name) {

        ArrayList<Actor> actors = new ArrayList<Actor>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT actor_id, first_name, last_name FROM actor WHERE first_name = ?")) {

            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Actor actor = new Actor(resultSet.getInt(1),
                            resultSet.getString(2), resultSet.getString(3));
                    actors.add(actor);
                }
                return actors;
            }

        } catch (SQLException e) {
            System.err.println("An error occurred: " + e);
        }

        return null;
    }
}
