package co.tiozao.desafioandroid.controller.utils;

import android.view.View;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class StringUtils {

    public static String stringFromResource(int res, View view) {
        return view.getContext().getResources().getString(res);
    }

    public static String dateFormatted(String originalDate, String newFormat){
        try{
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date date = format.parse(originalDate);

            return new SimpleDateFormat(newFormat).format(date);
        }catch(Exception e){
            e.printStackTrace();
        }

        return "";
    }
}
