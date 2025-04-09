package org.hemostaza;

public class Item {

    private String name;
    private String date;
    private double usd;
    private double pln;

    public Item(String name, String date, double usd, double pln) {
        this.name = name;
        this.date = date;
        this.usd = usd;
        this.pln = pln;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public double getUsd() {
        return usd;
    }

    public double getPln() {
        return pln;
    }

    public void setPln(double rate){
        pln = usd * rate;
    }
}
