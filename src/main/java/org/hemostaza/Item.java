package org.hemostaza;


import java.math.BigDecimal;
import java.sql.Date;

public class Item {
    private String name;
    private Date date;
    private BigDecimal usd;
    private BigDecimal pln;

    public Item(String name, Date date, BigDecimal usd, BigDecimal pln) {
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

    public BigDecimal getUsd() {
        return usd;
    }

    public BigDecimal getPln() {
        return pln;
    }

    @Override
    public String toString() {
        return name+" | "+date+" | "+usd+" USD | "+pln+" PLN";
    }
}
