package org.hemostaza;

import java.util.List;

public class View {

    public void show(List<Item> list){
        for (Item i : list){
            System.out.println(i.getName());
        }
    }

}
