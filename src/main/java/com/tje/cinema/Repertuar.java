package com.tje.cinema;

import java.util.ArrayList;

public class Repertuar {
    private ArrayList<Seans> seansList;

    public Repertuar() {
        this.seansList = new ArrayList<Seans>();
    }

    public void addSeans(Seans seans){
        this.seansList.add(seans);
    }

//    def wyswietl_repertuar(dzien):
//        for seans in lista_seansow:
//            if seans.data == dzien:
//    print(seans.pobierz_szczegoly_seansu())

}
