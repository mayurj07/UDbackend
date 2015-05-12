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
        SendGrid sendgrid = new SendGrid("apikey"); //add api key or username & password

        SendGrid.Email email1 = new SendGrid.Email();
        email1.addTo(""); //add email here
        email1.setFrom("registration@udelvr.com");
        email1.setSubject("Welcome to Udelvr");
        email1.setText("Hello " + request.getParameter("fullName") + ". Welcome to Udelvr." +
                " We hope you have a great time using UDelvr! Happy Shipping!");

        try {
            SendGrid.Response response = sendgrid.send(email1);
            System.out.println(response.getMessage());
        }
        catch (SendGridException e) {
            System.err.println(e);
        }

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
        File convFile = compressUtility.convertToFile(imageFile);
        File compressedFile = compressUtility.compress(convFile);
        byte[] compressByteArrary = compressUtility.convertFileToByteArray(compressedFile);

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
        User newUser = new User(id, fullName, email, mobileNo, password, deviceID, created_at, userImage, compressByteArrary);

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


/*  @RequestMapping(value = "/user/verify", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public boolean sendMsg(MultipartHttpServletRequest request) throws TwilioRestException
    {
        String smsAuthCode = request.getParameter("SmsAuthCode");
        String mobileNo = request.getParameter("mobileNo");
        return smsAuthenticator.verifyPhone(mobileNo, smsAuthCode);
    }*/


    @RequestMapping(value = "/compress", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void compressImg(MultipartHttpServletRequest request) throws IOException {
        Iterator<String> itr    = request.getFileNames();
        MultipartFile imageFile = request.getFile(itr.next());

        File convFile = compressUtility.convertToFile(imageFile);

        compressUtility.compress(convFile);

        //String sourceImage = "/Users/mayur/photu.jpg";
        String targetImage = "/Users/mayur/photu_thumbnail.jpg";

        compressUtility.saveScaledImage(convFile, targetImage);
    }

}
