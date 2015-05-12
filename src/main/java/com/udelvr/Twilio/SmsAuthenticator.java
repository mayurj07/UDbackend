package com.udelvr.Twilio;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SmsAuthenticator {

    public String hmacDigest(String msg, String keyString, String algo)
    {
        String digest = null;
        try {
            SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), algo);
            Mac mac = Mac.getInstance(algo);
            mac.init(key);

            byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));

            StringBuffer hash = new StringBuffer();

            for (int i = 0; i < bytes.length; i++)
            {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            hash.setLength(5);
            digest = hash.toString().toUpperCase();

        } catch (UnsupportedEncodingException e) {
        } catch (InvalidKeyException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        return digest;
    }


    public boolean verifyPhone(String email, String smsCode)
    {
        String generatedAuth = hmacDigest(email, "udelvr", "HmacSHA1");
        if (generatedAuth.equals(smsCode))
            return true;
        else
            return false;
    }


}
