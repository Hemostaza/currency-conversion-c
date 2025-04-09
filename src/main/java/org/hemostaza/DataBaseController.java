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
                "  `name` VARCHAR(45) NOT NULL,\n" +
                "  `date` VARCHAR(45) NOT NULL,\n" +
                "  `usd_value` DECIMAL(12,2) NOT NULL,\n" +
                "  `pln_value` DECIMAL(12,2) NOT NULL,\n" +
                "  PRIMARY KEY (`id`));";

        statement.executeUpdate(sql);
    }

    public void saveItem(Item item) throws SQLException {
        String sql = "INSERT INTO items (name, date, usd_value, pln_value) " +
                "VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, item.getName());
        preparedStatement.setString(2, item.getDate());
        preparedStatement.setDouble(3, item.getUsd());
        preparedStatement.setDouble(4, item.getPln());

        preparedStatement.executeUpdate();

        System.out.println("Zapisano dane do bazy danych.");
    }
    public void saveItem(String name, String date, double usd, double pln) throws SQLException {
        String sql = "INSERT INTO items (name, date, usd_value, pln_value) " +
                "VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, name);
        preparedStatement.setString(2, date);
        preparedStatement.setDouble(3, usd);
        preparedStatement.setDouble(4, pln);

        preparedStatement.executeUpdate();

        System.out.println("Zapisano dane do bazy danych.");
    }

    public void saveMultiple(List<Item> list) throws SQLException {
//        String sql = "INSERT INTO items (name, date, usd_value, pln_value) VALUES ";
//
//        for(Item i : list){
//            sql+="("+i.getName()+" "+i.getDate()+" "+i.getUsd()+" "+i.getPln()+"),";
//        }
//
//        Statement statement = connection.createStatement();
//        statement.executeUpdate(sql);

        for(Item i : list){
            saveItem(i);
        }

    }

    public List<Item> getAll() throws SQLException {
        List<Item> itemsList = new ArrayList<>();

        String sql = "SELECT name,date,usd_value,pln_value FROM items";

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            String name = rs.getString("name");
            String date = rs.getString("date");
            double usd = rs.getDouble("usd_value");
            double pln = rs.getDouble("pln_value");
            Item itm = new Item(name, date, usd, pln);
            itemsList.add(itm);
            //System.out.println(name+" "+date+" "+usd+" "+pln);
        }
        return itemsList;
    }

    public List<Item> getByName(String partName,String sortBy, String order) throws SQLException {
        List<Item> itemsList = new ArrayList<>();
        String sql = "SELECT * FROM items WHERE name LIKE ?";

        sql+=" ORDER BY "+sortBy+" "+order;

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, "%"+partName+"%");

        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            String name = rs.getString("name");
            String date = rs.getString("date");
            double usd = rs.getDouble("usd_value");
            double pln = rs.getDouble("pln_value");
            Item itm = new Item(name, date, usd, pln);
            itemsList.add(itm);
        }


        return itemsList;
    }

}
