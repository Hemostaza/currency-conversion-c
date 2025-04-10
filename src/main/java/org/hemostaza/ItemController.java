package org.hemostaza;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ItemController {
    DataBaseController dataBaseController;
    ExchangeController exchangeController;
    XMLController xmlController;

    public ItemController() {
        dataBaseController = new DataBaseController();
        try {
            dataBaseController.initializeDatabase();
        } catch (SQLException e) {
            throw new RuntimeException("Baza danych nie została utworzona: " + e);
        }
        exchangeController = new ExchangeController();
        xmlController = new XMLController();
    }

    boolean addItem(String[] args) {
        if (args.length < 4) {
            return false;
        }
        try {
            //Sprawzamy czy data jest git
            LocalDate date;
            date = LocalDate.parse(args[2]);
            double usd = Double.parseDouble(args[3]);
            int day = date.getDayOfWeek().getValue();
            DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            double rate = 0;
            //Jeżeli weekend:
            if (day > 5) {
                int daychange = day - 5;
                String newDate = date.minusDays(daychange).format(sdf);

                rate = exchangeController.getRate(newDate);
                System.out.println("Podany dzień to weekend, czy chcesz użyć kursu " + rate + " z piątku " + newDate + "? (y/n)");
                //Sprawdzamy czy się godzi na piatek
                Scanner scanner = new Scanner(System.in);
                char response = scanner.next().charAt(0);
                if (response != 'y') {
                    return false;
                }
            } else {
                rate = exchangeController.getRate(args[2]);
            }
            double pln = usd * rate;
            dataBaseController.saveItem(args[1], Date.valueOf(args[2]), usd, pln);
            return true;

        } catch (DateTimeParseException e) {
            System.out.println("Błędny format daty");
            return false;
        } catch (NumberFormatException e) {
            System.out.println("Nieprawidłowa wartość USD");
            return false;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

    }

    public boolean showAll(String[] args) {
        List<Item> list;
        if (args.length > 1) {
            if (!args[1].equalsIgnoreCase("sort-by")) {
                return false;
            }
        }
        String sortBy = "nazwa";
        String order = "asc";
        if (args.length > 2) {
            if(args[2].equalsIgnoreCase("date"))
            {
                sortBy = "data_ksiegowania";
            }
        }
        if (args.length > 3) {
            order = args[3];
        }
        try {
            list = dataBaseController.getAll(sortBy, order);
        } catch (SQLException e) {
            return false;
        }
        System.out.println("\n");
        for (Item item : list) {
            System.out.println(item);
        }
        return true;
    }

    public boolean findBy(String[] args) {
        List<Item> list;
        String findBy = "nazwa";
        String sortBy = "nazwa";
        String order = "desc";
        String findingPart;

        if (args.length < 3) {
            return false;
        }
        if (args.length > 4) {
            if (!args[3].equalsIgnoreCase("sort-by")) {
                return false;
            }
            if(args[4].equalsIgnoreCase("date")){
                sortBy = "data_ksiegowania";
            }
            //sortBy = args[4];
        }
        if(args.length>5){
            order = args[5];
        }
        if(args[1].equalsIgnoreCase("date")){
            findBy = "data_ksiegowania";
        }
        findingPart = args[2];

        try {
            list = dataBaseController.getByName(findBy, findingPart, sortBy, order);
        } catch (SQLException e) {
            System.out.println("Problem z bazą danych");
            return false;
        }
        for (Item item : list) {
            System.out.println(item);
        }
        return true;
    }

    public boolean saveToXml(String[] args){
        if(args.length<2){
            return false;
        }
        String xmlName = args[1];
        try {
            xmlController.saveToXml(dataBaseController.getAll("nazwa","desc"),xmlName);
        } catch (ParserConfigurationException | SQLException | TransformerException e) {
            return false;
        }
        return true;
    }

    public boolean insertXml(String[] args){
        if(args.length<2){
            return false;
        }
        try {
            List<Item> itemsList = xmlController.getFromXml(args[1]);
            dataBaseController.saveMultiple(itemsList);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
