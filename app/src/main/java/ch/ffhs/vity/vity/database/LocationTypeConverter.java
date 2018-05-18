package ch.ffhs.vity.vity.database;

import android.arch.persistence.room.TypeConverter;
import android.location.Location;

import java.util.Date;
import java.util.Locale;

public class LocationTypeConverter {
    @TypeConverter
    public static String locationToString(Location location) {
        if (location==null) {
            return(null);
        }

        return(String.format(Locale.getDefault(), "%f,%f", location.getLatitude(), location.getLongitude()));
    }

    @TypeConverter
    public static Location toLocation(String latlon) {
        if (latlon==null) {
            return(null);
        }

        String[] pieces=latlon.split(",");
        Location result=new Location("");

        result.setLatitude(Double.parseDouble(pieces[0]));
        result.setLongitude(Double.parseDouble(pieces[1]));

        return(result);
    }
}
