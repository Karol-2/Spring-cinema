package com.tje.cinema;

import java.time.LocalDate;
import java.util.ArrayList;

public class Repertuar {
    private ArrayList<Seans> seansList;

    public Repertuar() {
        this.seansList = new ArrayList<Seans>();
    }

    public void addSeans(Seans seans){
        this.seansList.add(seans);
    }

    public ArrayList<Seans> getSeansesByDate(LocalDate date){
        System.out.println(date);
        ArrayList<Seans> results = new ArrayList<Seans>();
        for (int i = 0; i < seansList.size(); i++) {
            Seans currentSeans = seansList.get(i);
            if (currentSeans.getDateAndTime().toLocalDate().isEqual(date)){
                results.add(currentSeans);
            }
        }

        return results;
    }



}
