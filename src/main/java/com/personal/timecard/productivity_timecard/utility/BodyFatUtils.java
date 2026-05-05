package com.personal.timecard.productivity_timecard.utility;

import com.personal.timecard.productivity_timecard.dto.BodyFatRequest;
import com.personal.timecard.productivity_timecard.enums.Gender;
import org.springframework.stereotype.Component;

@Component
public class BodyFatUtils {
    public double calculateFatPercentage (Gender gender, double height, BodyFatRequest bodyFatRequest) {
        double fatPercentage;
        if (gender == Gender.MALE) {

            fatPercentage = 86.010 * Math.log10(bodyFatRequest.getWaistCircumference() - bodyFatRequest.getNeckCircumference())
                    - 70.041 * Math.log10(height)
                    + 36.76;

        } else if (gender == Gender.FEMALE) {

            fatPercentage = 163.205 * Math.log10(bodyFatRequest.getWaistCircumference() + bodyFatRequest.getHipCircumference() - bodyFatRequest.getNeckCircumference())
                    - 97.684 * Math.log10(height)
                    - 78.387;

        } else {
            throw new IllegalArgumentException("Unsupported gender type");
        }

        return Math.round(fatPercentage * 100.0) / 100.0;
    }
}
