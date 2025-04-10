package org.hemostaza;


import java.sql.Date;

public class Item {

    private String name;
    private Date date;
    private double usd;
    private double pln;

    public Item(String name, Date date, double usd, double pln) {
        this.name = name;
        this.date = date;
        this.usd = usd;
        this.pln = pln;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
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

    @Override
    public String toString() {
        return name+" | "+date+" | "+usd+" USD | "+pln+" PLN";
    }
}
