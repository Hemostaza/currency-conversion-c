package org.hemostaza;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseController {

    private Connection connection;
    private static final String DB_URL = "jdbc:h2:./exchange_rate_db";

    public void initializeDatabase() throws SQLException {

        connection = DriverManager.getConnection(DB_URL);
        Statement statement = connection.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS `items` (\n" +
                "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `nazwa` VARCHAR(45) NOT NULL,\n" +
                "  `data_ksiegowania` DATE NOT NULL,\n" +
                "  `koszt_USD` DECIMAL(12,2) NOT NULL,\n" +
                "  `koszt_PLN` DECIMAL(12,2) NOT NULL,\n" +
                "  PRIMARY KEY (`id`));";

        statement.executeUpdate(sql);
    }

    public void saveItem(Item item) throws SQLException {
        String sql = "INSERT INTO items (nazwa, data_ksiegowania, koszt_USD, koszt_PLN) " +
                "VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, item.getName());
        preparedStatement.setDate(2, item.getDate());
        preparedStatement.setDouble(3, item.getUsd());
        preparedStatement.setDouble(4, item.getPln());

        preparedStatement.executeUpdate();

        System.out.println("Zapisano dane do bazy danych.");
    }
    public void saveItem(String name, Date date, double usd, double pln) throws SQLException {
        String sql = "INSERT INTO items (nazwa, data_ksiegowania, koszt_USD, koszt_PLN) " +
                "VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, name);
        preparedStatement.setDate(2, date);
        preparedStatement.setDouble(3, usd);
        preparedStatement.setDouble(4, pln);

        preparedStatement.executeUpdate();

        System.out.println("Zapisano dane do bazy danych.");
    }

    public void saveMultiple(List<Item> list) throws SQLException {
        for(Item i : list){
            saveItem(i);
        }

    }

    public List<Item> getAll(String sortBy, String order) throws SQLException {
        List<Item> itemsList = new ArrayList<>();

        String sql = "SELECT nazwa,data_ksiegowania,koszt_USD,koszt_PLN FROM items";

        if(sortBy!=null || order!=null){
            sql+=" ORDER BY "+sortBy+" "+order;
        }

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            String name = rs.getString("nazwa");
            Date date = rs.getDate("data_ksiegowania");
            double usd = rs.getDouble("koszt_USD");
            double pln = rs.getDouble("koszt_PLN");
            Item itm = new Item(name, date, usd, pln);
            itemsList.add(itm);
            //System.out.println(name+" "+date+" "+usd+" "+pln);
        }
        return itemsList;
    }

    public List<Item> getByName(String findBy, String findingPart,String sortBy, String order) throws SQLException {
        List<Item> itemsList = new ArrayList<>();
        String sql = "SELECT * FROM items WHERE "+findBy+" LIKE ?";

        sql+=" ORDER BY "+sortBy+" "+order;

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, "%"+findingPart+"%");

        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            String name = rs.getString("nazwa");
            Date date = rs.getDate("data_ksiegowania");
            double usd = rs.getDouble("koszt_USD");
            double pln = rs.getDouble("koszt_PLN");
            Item itm = new Item(name, date, usd, pln);
            itemsList.add(itm);
        }
        return itemsList;
    }

}
