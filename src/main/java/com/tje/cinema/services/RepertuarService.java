package com.tje.cinema.services;

import com.tje.cinema.domain.Seans;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class RepertuarService {

    private final ArrayList<Seans> seansList = new ArrayList<Seans>();


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
