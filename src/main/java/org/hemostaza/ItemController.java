package org.hemostaza;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

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
            LocalDate date;
            date = LocalDate.parse(args[2]);
            BigDecimal usd = new BigDecimal(args[3]).setScale(2, RoundingMode.HALF_EVEN);
            int day = date.getDayOfWeek().getValue();
            DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            double rate = 0;
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
            BigDecimal pln = usd.multiply(BigDecimal.valueOf(rate)).setScale(2, RoundingMode.HALF_EVEN);
            Item item = new Item(args[1], Date.valueOf(args[2]), usd, pln);
            dataBaseController.saveItem(item);
            xmlController.saveToXml(Arrays.asList(item));

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

    public void showAll(String[] args) throws Exception{
        if (args.length > 1) {
            if (!args[1].equalsIgnoreCase("sort-by") && !args[1].equalsIgnoreCase("sortuj-po")) {
                throw new Exception("show-all sort-by [name/date]");
            }
        }
        String sortBy = "nazwa";
        String order = "asc";
        if (args.length > 2) {
            sortBy = switch (args[2].toLowerCase()) {
                case "date","data" -> "data_ksiegowania";
                case "name","nazwa" -> "nazwa";
                default -> throw new Exception("sort-by only by name or date");
            };
        }
        if (args.length > 3) {
            order = switch (args[3].toLowerCase()) {
                case "asc","ros", "" -> "ASC";
                case "desc","mal" -> "DESC";
                default -> throw new Exception("Order of sorting is only asc or desc");
            };
        }

        showList(dataBaseController.getAll(sortBy, order));
    }

    public List<Item> findBy(String[] args) throws Exception {
        List<Item> itemList = new ArrayList<>();
        String findBy;
        String sortBy = "nazwa";
        String order = "desc";
        String findingPart;

        if (args.length < 3) {
            throw new Exception("find-by name/date [name/date]");
        }
        if (args.length > 3) {
            if (!args[3].equalsIgnoreCase("sort-by")) {
                throw new Exception("find-by name/date [name/date] sort-by name/date asc/desc");
            }
            if (args.length > 4) {
                sortBy = switch (args[4].toLowerCase()) {
                    case "date","data" -> "data_ksiegowania";
                    case "name","nazwa" -> "nazwa";
                    default -> throw new Exception("sort-by only by name or date");
                };
            }else throw new Exception("find-by name/date [name/date] sort-by name/date asc/desc");
        }

        if (args.length > 5) {
            order = switch (args[5].toLowerCase()) {
                case "asc","ros", "" -> "ASC";
                case "desc","mal" -> "DESC";
                default -> throw new Exception("Order of sorting is only asc or desc");
            };
        }

        findBy = switch (args[1].toLowerCase()) {
            case "date","data" -> "data_ksiegowania";
            case "name","nazwa" -> "nazwa";
            default -> throw new Exception("find-by name/date [name/date]");
        };
        findingPart = args[2];

        itemList = dataBaseController.getByName(findBy, findingPart, sortBy, order);
        showList(itemList);
        return itemList;
    }

    public void showList(List<Item> itemsList) throws Exception {
        if (itemsList == null || itemsList.isEmpty()) {
            throw new Exception("Wynik wyszukiwania jest pusty.");
        }
        System.out.println("Nazwa | Data księgowania | koszt USD | koszt PLN");
        for (Item item : itemsList) {
            System.out.println(item);
        }
    }

    public void exportToXml(String[] args) throws Exception {
        if (args.length < 2) {
            throw new Exception("export-xml [file-name]");
        }
        String xmlName = args[1];
        List<Item> itemList;
        if(args.length>2){
            if(args[2].equalsIgnoreCase("find-by")){
                String[] newArgs = Arrays.copyOfRange(args,2,args.length);
                itemList = findBy(newArgs);
                xmlController.exportToXml(itemList,xmlName);
            }else throw new Exception("export-xml [file-name] find-by name/date [name/date]");
        }else{
            itemList = dataBaseController.getAll("nazwa","asc");
            xmlController.exportToXml(itemList,xmlName);
        }
    }

    public boolean insertXml(String[] args) {
        if (args.length < 2) {
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
