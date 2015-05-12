package com.udelvr.login.signIn;

import com.udelvr.exceptions.BadRequestException;
import com.udelvr.login.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Repository;


@Repository
public class SignInService {

    public static final String COLLECTION_NAME = "users";

    @Autowired
    private MongoTemplate mongoTemplate;



    public User signInUser(User user) throws Exception
    {
        BasicQuery query = new BasicQuery("{email:'"+user.getEmail()+"'},{}");

        try{
            User userLogin = mongoTemplate.findOne(query, User.class, COLLECTION_NAME);

            if(user.getPassword().equals(userLogin.getPassword()))
            {
                return getSignInData(userLogin.getUserId());
            }
            else
                throw new BadRequestException("Incorrect Login Credentials.");

        }catch (Exception e)
        {
            throw new BadRequestException("User does not exist.");
        }
    }


    public User getSignInData(String user_id)
    {
        User user = mongoTemplate.findById(user_id , User.class, "users");
        String profileURL = "/user/"+user_id+"/profilepic";
        String licensePhotoURL = "/driver/"+user_id+"/licensePhoto";

        if(user.getDriverLicenseNo().equals(""))
            licensePhotoURL = "";

        user.setProfileURL(profileURL);
        user.setLicensePhotoURL(licensePhotoURL);
        user.setLicensePhoto(null);
        user.setPhoto(null);

        return user;
    }






}
