package ch.ffhs.vity.vity.Mock;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.ffhs.vity.vity.Helper.ActivityItem;

public class Activities_Mock {

    private static List<ActivityItem> liste;

    public Activities_Mock(){

        liste = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy | HH:mm");
        String currentDateandTime = sdf.format(new Date());

        for(int i = 0; i < 30; i++){

            ActivityItem a1 = new ActivityItem();
            a1.setTitle("Puchini Belp" + i);
            a1.setDescription("Puchini ist ein super italienisches Restaurant in Belp. Abends meist relativ gut ausgebucht - Reservation! Tipp: Pizza Puchini");
            a1.setCategory("Essen und Trinken");
            a1.setLink("www.puchinibelp.ch");
            a1.setDate(currentDateandTime);
            a1.setOwner("Michael");
            a1.setLink_image("https://www.puccinibelp.ch/images/slider/_MG_3534.jpg");

            liste.add(a1);
        }
    }

    public List<ActivityItem> getActivities(){
        return liste;
    }

    public ActivityItem getActivity(int id){
        return liste.get(id);
    }
}
