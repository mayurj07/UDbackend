package com.udelvr.email;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import org.junit.Test;

public class EmailTest{

    @Test
    public void emailTest() throws Exception{

        SendGrid sendgrid = new SendGrid(""); //add api key or username & password
        String url = "http://localhost:8080";
        String html = "Hello Test,<br><br> Welcome to Udelvr." +
                " We hope you have a great time using UDelvr! Happy Shipping!<br><br>" +
                "<a href=\"" + url + "/user/verifyemail/id/auth\">Please click on the link to verify your account</a><br><br>" +
                "Best Regards,<br>Udelvr Team";
        SendGrid.Email email1 = new SendGrid.Email();
        email1.addTo(""); //add email here
        email1.setFrom("registration@udelvr.com");
        email1.setSubject("Welcome to Udelvr");
//        email1.setText("Hello " + request.getParameter("fullName") + ". Welcome to Udelvr." +
//                " We hope you have a great time using UDelvr! Happy Shipping!");
        email1.setHtml(html);

        try {
            SendGrid.Response response = sendgrid.send(email1);
            System.out.println(response.getMessage());
        } catch (SendGridException e) {
            System.err.println(e);
        }
    }
}
