package com.udelvr.login.signUp;


import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import com.twilio.sdk.TwilioRestException;
import com.udelvr.Twilio.SmsAuthenticator;
import com.udelvr.Twilio.SmsSender;
import com.udelvr.compression.CompressImage;
import com.udelvr.exceptions.BadRequestException;
import com.udelvr.login.User.User;
import com.udelvr.login.User.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;


@RestController()
@Component("SignupController")
public class SignupController {

    public SignupController(){}

    public SignupController(SignupService signupService){
        this.signupService=signupService;
    }

    EmailAuth emailAuth = new EmailAuth();


    @Autowired
    SignupService signupService = new SignupService();

    SmsSender twilio = new SmsSender();
    SmsAuthenticator smsAuthenticator = new SmsAuthenticator();
    CompressImage compressUtility = new CompressImage();

    public String giveDate()
    {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(timeZone);
        return(df.format(new Date()));
    }

    public static String generateID()
    {
        int aNumber = 0;
        aNumber = (int)((Math.random() * 900000)+100000);
        return ""+aNumber;
    }


    //Create new User using POST method
    @ResponseBody
    @RequestMapping(value="/user/signup", method=RequestMethod.POST, produces = "application/json")
    public ResponseEntity<UserDO> create_user(MultipartHttpServletRequest request) throws IOException
    {

        Iterator<String> itr    = request.getFileNames();
        String id               = generateID();
        String fullName         = request.getParameter("fullName");
        String email            = request.getParameter("email");
        String mobileNo         = request.getParameter("mobileNo");
        String password         = request.getParameter("password");
        String deviceID         = request.getParameter("deviceId");
        String created_at       = giveDate();

        MultipartFile imageFile = request.getFile(itr.next());

        //compression
        File originalFile           = compressUtility.convertToFile(imageFile);
        File compressedFile         = compressUtility.compress(originalFile);
        byte[] compressImage        = compressUtility.convertFileToByteArray(compressedFile);
        File thumbnailFile          = compressUtility.saveScaledImage(compressedFile);
        byte[] thumbnail            = compressUtility.convertFileToByteArray(thumbnailFile);
        //end of compression

        byte [] userImage = imageFile.getBytes();

        //validation part
        if(fullName == null || fullName.trim().equals(""))
            throw new BadRequestException("Full Name Required.");

        if(email == null || email.trim().equals(""))
            throw new BadRequestException("Email Required.");

        if(mobileNo == null || mobileNo.trim().equals(""))
            throw new BadRequestException("MobileNo Required.");

        if(password == null || password.trim().equals(""))
            throw new BadRequestException("Password Required.");

        if(deviceID == null || deviceID.trim().equals(""))
            throw new BadRequestException("DeviceID Required.");

        if(itr == null)
            throw new BadRequestException("Profile Picture Required.");


        //create new USER object
        User newUser = new User(id, fullName, email, mobileNo, password, deviceID, created_at, userImage, compressImage, thumbnail);


        String auth = emailAuth.hmacDigest(id, "udelvr", "HmacSHA1");
        SendGrid sendgrid = new SendGrid("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"); //add api key or username & password
        String url = "http://localhost:8080";
        String html = "Hello " + request.getParameter("fullName") + ",<br><br> Welcome to Udelvr." +
                " We hope you have a great time using UDelvr! Happy Shipping!<br><br>" +
                "<a href=\""+url+"/user/verifyemail/"+id+"/"+auth+"\">Please click on the link to verify your account</a><br><br>" +
                "Best Regards,<br>Udelvr Team";
        SendGrid.Email email1 = new SendGrid.Email();
        email1.addTo(request.getParameter("email")); //add email here
        email1.setFrom("registration@udelvr.com");
        email1.setSubject("Welcome to Udelvr");
//        email1.setText("Hello " + request.getParameter("fullName") + ". Welcome to Udelvr." +
//                " We hope you have a great time using UDelvr! Happy Shipping!");
        email1.setHtml(html);

        try {
            SendGrid.Response response = sendgrid.send(email1);
            System.out.println(response.getMessage());
        }
        catch (SendGridException e) {
            System.err.println(e);
        }

        return new ResponseEntity<UserDO>(signupService.addUser(newUser), HttpStatus.CREATED);
    }


    //View User using GET method
    //Returns the UserDO object
    @RequestMapping(value="/user/{id}", method=RequestMethod.GET)
    public ResponseEntity<UserDO> get_user(@PathVariable(value = "id") String user_id) throws IOException
    {
        return new ResponseEntity(signupService.getUserData(user_id), HttpStatus.OK);
    }


    @RequestMapping(value="/user/{uid}/profilepic", method=RequestMethod.GET, produces = "image/jpeg")
    @ResponseStatus(HttpStatus.OK)
    public byte[] getImage(@PathVariable String uid) throws Exception
    {
        return signupService.getUserImage(uid);
    }


    @RequestMapping(value="/user/phone/{phone}", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String checkMobileNo(@PathVariable(value = "phone") String phone){

        //generate a digest of the user fullname
        String auth = smsAuthenticator.hmacDigest(phone, "udelvr", "HmacSHA1");

        //send SMS to user with verification code
        try {
            twilio.sendSMS(phone, auth);
        } catch (TwilioRestException e) {
            e.printStackTrace();
        }
        return auth;
    }


    @RequestMapping(value = "/user/verifyemail/{id}/{auth}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public boolean verifyEmail(@PathVariable(value="id") String id,@PathVariable(value="auth") String auth) throws Exception
    {
        return signupService.verifyEmail(id,auth);
    }

}
