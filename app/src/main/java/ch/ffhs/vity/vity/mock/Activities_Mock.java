package ch.ffhs.vity.vity.mock;

import java.util.ArrayList;
import java.util.List;

import ch.ffhs.vity.vity.activity.VityItem;

/**
 * Created by hamme on 10.03.2018.
 */

public class Activities_Mock {

    private static List<VityItem> liste;

    public Activities_Mock(){

        liste = new ArrayList<>();

        for(int i = 0; i < 30; i++){

            VityItem a1 = new VityItem();
            a1.setTitle("Puchini Belp" + i);
            a1.setDescription("Puchini ist ein super italienisches Restaurant in Belp. Abends meist relativ gut ausgebucht - Reservation! Tipp: Pizza Puchini");
            a1.setCategory("Food and drinks");
            a1.setLink("www.puchinibelp.ch");
            //a1.setDate(Instant.now());
            a1.setOwner("Michael");
            a1.setLink_image("Image_05.jpg");

            liste.add(a1);
        }
    }

    public List<VityItem> getActivities(){
        return liste;
    }

    public VityItem getActivity(int id){
        return liste.get(id);
    }
}
