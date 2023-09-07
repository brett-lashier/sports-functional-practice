package com.interview.sports.weather.validation;

import com.interview.sports.weather.netty.request.GameWeatherRequest;
import com.interview.sports.weather.netty.request.VenueWeatherRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

public abstract class WeatherValidator {

    public static boolean isValidGameWeatherRequest(GameWeatherRequest request){
        if(request == null || request.team() == null || request.date() == null) return false;
        try {
            Integer.parseInt(request.team());
            Pattern pattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
            return pattern.matcher(request.date()).matches();

        }catch(NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isValidVenueWeatherRequest(VenueWeatherRequest request){
        if(request == null || request.venue() == null) return false;
        try {
            Integer.parseInt(request.venue());
        }catch(Exception ex) {
            return false;
        }

        return true;
    }

    public static boolean isDateWithinAWeek(GameWeatherRequest request) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date todaysDate = calendar.getTime();
        try {
            Date date1 = formatter.parse(request.date());
            boolean isPast = todaysDate.getTime() >= date1.getTime();
            if(isPast) return true;

            long daysDiff = (date1.getTime() - todaysDate.getTime());
            long diffInDays = (daysDiff / (1000 * 60 * 60 * 24)) % 365;
            if(diffInDays > 7) return false;
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
