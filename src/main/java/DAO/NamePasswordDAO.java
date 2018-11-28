package DAO;

import org.postgresql.util.PSQLException;

import java.sql.*;

public class NamePasswordDAO {

    private String jdbUrl = "jdbc:postgresql://localhost:5432/exerciselogin";
    private String user = "loginn";
    private String password = "loginn";

    private Connection connection;

    private boolean result;
    private String passwordFromDB = "";
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;


    public void connection () throws SQLException {
        connection = DriverManager.getConnection(jdbUrl, user, password);
    }


    public boolean checkNamePassword(String name, String password){
        result = true;
        try {
            connection ();
            preparedStatement = connection.prepareStatement("SELECT password FROM temp WHERE name = ?");
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            passwordFromDB = createQuest(resultSet);
            resultSet.close();
            connection.close();
        }
        catch (SQLException exc) {
            exc.printStackTrace();
        }
        if(password.equals(passwordFromDB)){
            result = true;
        }else{
            result = false;
        }
        return result;
    }

    private String createQuest(ResultSet resultSet) {
        String quest=null;
        try{
            while (resultSet.next()) {
                quest = resultSet.getString("password");
            }
        }catch(SQLException e ){
            e.printStackTrace();
        }
        return quest;
    }
}
