package com.ratz.wemanage.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;

public class SmsUtils {

    @Value("${FROM_NUMBER}")
    private static String FROM_NUMBER;

    @Value("${SID_KEY}")
    private static String SID_KEY;

    @Value("${TOKEN_KEY}")
    private static String TOKEN_KEY;

    public static void sendSms(String toNumber, String messageBody) {
        Twilio.init(SID_KEY, TOKEN_KEY);
        var message = Message.creator(new PhoneNumber(toNumber), new PhoneNumber(FROM_NUMBER), messageBody).create();
        System.out.println(message);
    }
}
