package com.interview.sports.weather.conversion;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class FloatConverter {

    public static String roundStringFloatNPlaces(int places, String value) {
        BigDecimal decVal = new BigDecimal(value);
        return decVal.setScale(places, RoundingMode.HALF_UP).toString();
    }
}
