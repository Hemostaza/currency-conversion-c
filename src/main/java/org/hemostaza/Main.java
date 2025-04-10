package org.hemostaza;

public class Main {

    public static void main(String[] args) {
        ItemController itemController = new ItemController();
        if (args.length < 1) {
            showHelp();
            return;
        }
        switch (args[0]) {
            case "add":
                if (!itemController.addItem(args)) {
                    System.out.println("add [name] [RRRR-MM-DD] [usd]");
                }
                break;
            case "show-all":
                if (!itemController.showAll(args)) {
                    System.out.println("show-all sort-by name/date asc/desc");
                }
                break;
            case "find-by":
                if (!itemController.findBy(args)) {
                    System.out.println("find-by name/date [name/date] sort-by name/date asc/desc");
                }
                break;
            case "export-xml":
                if (!itemController.saveToXml(args)) {
                    System.out.println("export-xml fileName");
                }
                break;
            case "insert-xml":
                if (!itemController.insertXml(args)) {
                    System.out.println("insert-xml fileName.xml");
                }
                break;
            default:
                showHelp();
                break;
        }
    }

    static void showHelp(){
        System.out.println("Help for currency-conversion:");
        System.out.println("add [name] [RRRR-MM-DD] [usd]");
        System.out.println("    Used to add new record to database.");
        System.out.println("show-all");
        System.out.println("    Use to show all records in database. ");
        System.out.println("    Use with optional  sort-by name/date asc/desc  to sort results.");
        System.out.println("find-by name/date [name/date]");
        System.out.println("    Use to find and show recors in database by name or date.");
        System.out.println("    Use with optional  sort-by name/date asc/desc  to sort results.");
        System.out.println("export-xml fileName");
        System.out.println("    Use to save all records from database to xml file with provided name.");
        System.out.println("insert-xml fileName.xml");
        System.out.println("    Use to insert items from provided xml file into database.");
    }
}