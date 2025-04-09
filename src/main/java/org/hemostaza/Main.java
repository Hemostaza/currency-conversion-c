package org.hemostaza;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;

public class Main {

    private static final String DB_URL = "jdbc:h2:./exchange_rate_db";
    private static final String API_URL = "http://api.nbp.pl/api/exchangerates/rates/a/usd/";

    List<Item> itemsToSave;


    public static void main(String[] args) {
        DataBaseController dbController = new DataBaseController();
        ExchangeController exController = new ExchangeController();
        XMLController xmlController = new XMLController();
        View view = new View();
        try {
            dbController.initializeDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        switch (args[0]) {
            case "add":
                try {
                    LocalDate date = LocalDate.parse(args[2]);
                    int day = date.getDayOfWeek().getValue();
                    DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    if(day>5){
                        int daychange = day-5;
                        System.out.println(daychange);
                        String newDate = date.minusDays(daychange).format(sdf);
                        System.out.println(newDate);

                        double rate = exController.getRate(newDate);

                        System.out.println("Podany dzień to weekend, czy chcesz użyć kursu "+rate+" z piątku "+newDate+"? (y/n)");
                        //skaner chuje muje
                        Scanner scanner = new Scanner(System.in);
                        char response = scanner.next().charAt(0);
                        if(response!='y') {
                            break;
                        }
                        double usd = Double.parseDouble(args[3]);
                        double pln = usd * rate;
                        dbController.saveItem(args[1], args[2], usd, pln);
                    }


                } catch (Exception e) {
                    System.out.println("add {nazwa} {data} {kwota_usd}");
                    System.out.println(e);
                    break;
                    //throw new RuntimeException(e);
                }
                break;
            case "show-all":
                try {
                    List<Item> list = dbController.getAll();
                    view.show(list);
                } catch (Exception e) {
                    System.out.println(e);
                    break;
                }
                break;
            case "find-by":
                try {
                    List<Item> list = new ArrayList<>();
                    String sortBy = "name";
                    String order = "DESC";
                    String partName = args[2];
                    if (args.length > 3) {
                        if (args[3].equals("sort-by")) {
                            sortBy = args[4];
                            order = args[5].toUpperCase();
                        }
                    }
                    if (args[1].equals("name")) {
                        list = dbController.getByName(partName, sortBy, order);
                    }

                    view.show(list);
                } catch (Exception e) {
                    System.out.println(e);
                    break;
                }

                break;
            case "save-xml":
                try {
                    xmlController.saveToXml(dbController.getAll(),args[1]);
                } catch (ParserConfigurationException | SQLException | TransformerException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "insert-xml":
                try {
                    List<Item> itemsList = xmlController.getFromXml(args[1]);
                    dbController.saveMultiple(itemsList);
                    view.show(itemsList);
                } catch (ParserConfigurationException | SAXException | IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                break;
        }

        Scanner scanner = new Scanner(System.in);

//        while (!Objects.equals(scanner.nextLine(), "exit")) {
//            double rate = 0;
//            while (rate == 0) {
//                System.out.println("Podaj datę w formacie RRRR-MM-DD (lub pozostaw puste dla dzisiejszej daty):");
//                String dateInput = scanner.nextLine().trim();
//                String date = dateInput.isEmpty() ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) : dateInput;
//                //double rate = 0;
//                //while(rate==0){
//                try {
//                    //rate = exController.getRate(date);
//                    rate = exController.getRate(date);
//                } catch (Exception e) {
//                    System.out.println(e);
//                    //break;
//                    //throw new RuntimeException(e);
//                }
//                //}
//            }
//        }

        //System.out.println("Podaj datę w formacie RRRR-MM-DD (lub pozostaw puste dla dzisiejszej daty):");

//        String dateInput = scanner.nextLine().trim();
//        String date = dateInput.isEmpty() ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) : dateInput;
//        double rate = 3.50;
//        try {
//            //rate = exController.getRate(date);
//            exController.getMidRate(date);
//        } catch (Exception e) {
//            System.out.println("chuj xD");
//            //throw new RuntimeException(e);
//        }
//        System.out.println("Podaj nazwę wprowadzanego rekordu.");
//        String name = scanner.nextLine();
//        System.out.println("Podaj kwotę USD");
//        double usd = scanner.nextDouble();
//        double pln = usd * rate;
//        Item item = new Item(name,date,usd,pln);
//
//        try {
//            dbController.saveItem(item);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        try{
//            xmlController.saveToXml(item);
//        } catch (ParserConfigurationException | TransformerException e) {
//            throw new RuntimeException(e);
//        }
    }
}