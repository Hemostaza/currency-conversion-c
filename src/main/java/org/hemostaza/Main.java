package org.hemostaza;

public class Main {

    public static void main(String[] args) {
        ItemController itemController = new ItemController();
        if(args.length<1){
            System.out.println("use help parameter to get informations about application.");
        }
        switch (args[0]) {
            case "add":
                if (!itemController.addItem(args)) {
                    System.out.println("add [nazwa] [RRRR-MM-DD] [usd]");}
                break;
            case "show-all":
                if(!itemController.showAll(args)){
                    System.out.println("show-all sort-by name/date asc/desc");
                }
                break;
            case "find-by":
                if(!itemController.findBy(args)){
                    System.out.println("find-by name/date [nazwa/data] sort-by name/date asc/desc");
                }
                break;
            case "export-xml":
                if(!itemController.saveToXml(args)){
                    System.out.println("export-xml fileName");
                }
                break;
            case "insert-xml":
                if(!itemController.insertXml(args)){
                    System.out.println("insert-xml fileName.xml");
                }
                break;
            default:
                break;
        }
    }
}