package com.udelvr.login.driver;

import com.udelvr.exceptions.BadRequestException;
import com.udelvr.login.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;

@Repository
public class DriverService {

    public static final String COLLECTION_NAME = "users";

    @Autowired
    private MongoTemplate mongoTemplate;

    public DriverDO getDriverData(String driver_id)
    {
        User user = mongoTemplate.findById(driver_id , User.class, "users");
        String profileURL = "/user/"+driver_id+"/profilepic";
        String licensePhotoURL = "/driver/"+driver_id+"/licensePhoto";

        if(user.getDriverLicenseNo().equals(""))
            licensePhotoURL = "";

        Date d = user.getLicenseExpiry();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String licenseExpiry = sdf.format(d);

        DriverDO user_domain = new DriverDO(
                user.getUserId(),
                user.getFullName(),
                user.getEmail(),
                user.getMobileNo(),
                user.getPassword(),
                user.getDeviceId(),
                user.getDriverLicenseNo(),
                licenseExpiry,
                user.getDriverRating(),
                profileURL,
                licensePhotoURL);

        return user_domain;
    }


    public DriverDO addDriver(String driver_id, User driver) throws Exception
    {
        //Update the User with ID: user_id
        User updateDriver = mongoTemplate.findById(driver_id, User.class, "users");
        updateDriver.setDriverLicenseNo(driver.getDriverLicenseNo());
        updateDriver.setLicensePhoto(driver.getLicensePhoto());
        updateDriver.setLicenseExpiry(driver.getLicenseExpiry());
        updateDriver.setDriverRating(driver.getDriverRating());

        mongoTemplate.save(updateDriver);
        System.out.println("Driver Details Added.");

        return getDriverData(updateDriver.getUserId());
    }


    public DriverDO getDriver(String driver_id) throws Exception
    {
        try{
            User driver = mongoTemplate.findById(driver_id, User.class, "users");

            if(driver.getDriverLicenseNo().equalsIgnoreCase(" "))
            {
                throw new BadRequestException("Driver details not found for the User.");
            }
            else
                return getDriverData(driver.getUserId());
        }
        catch (Exception e)
        {
            throw new BadRequestException("Driver not found.");
        }
    }


    public Integer updateRating(String driver_id, Integer new_rating) throws Exception
    {
        try{
            User driver = mongoTemplate.findById(driver_id, User.class, "users");

            if(driver.getDriverLicenseNo().equals(""))
            {
                throw new BadRequestException("User is Not Registered as Driver.");
            }
            else
            {
                double current_rating = driver.getDriverRating().doubleValue();
                if(current_rating != 0.0)
                    new_rating = new Integer((int) Math.ceil( (current_rating + new_rating)/2 ));

                driver.setDriverRating(new_rating);
                mongoTemplate.save(driver,COLLECTION_NAME);
            }
        }catch (Exception e)
        {
            throw new BadRequestException("Driver not found.");
        }
        return new_rating;
    }

}
