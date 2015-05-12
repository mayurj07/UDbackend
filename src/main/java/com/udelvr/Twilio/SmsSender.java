package com.udelvr.Twilio;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class SmsSender {


    public void sendSMS(String toNumber, String authCode) throws TwilioRestException
    {
        String ACCOUNT_SID = "AC8f1b29f708ce9ce3a8b3d80370ea3338";
        String AUTH_TOKEN = "709304e90d0c61091b7ac0c0275c5fdb";
        TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

        // Build the parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("To", toNumber));
        params.add(new BasicNameValuePair("From", "+12057917998"));
        params.add(new BasicNameValuePair("Body", "Hi, This is Udelvr Team. Verification Code: "+authCode+""));
        MessageFactory messageFactory = client.getAccount().getMessageFactory();
        Message message = messageFactory.create(params);
        System.out.println(message.getSid());
    }

}
