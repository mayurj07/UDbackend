package com.udelvr.login.driver;


import com.udelvr.exceptions.BadRequestException;
import com.udelvr.login.User.User;
import com.udelvr.login.signUp.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;


@RestController()
@Component("DriverController")
public class DriverController {

    public DriverController(){}

    public DriverController(DriverService driverService){
        this.driverService=driverService;
    }

    @Autowired
    DriverService driverService = new DriverService();

    @Autowired
    SignupService signupService = new SignupService();

    /*************************************   driver REST APIs  **************************************************************/

    @RequestMapping(value="/user/{uid}/driver", method=RequestMethod.POST, produces = {"application/json"} )
    @ResponseStatus(HttpStatus.CREATED)
    public DriverDO createDriver(MultipartHttpServletRequest request, @PathVariable(value = "uid") String user_id) throws Exception
    {
        String driverLicenseNo  = request.getParameter("driverLicenseNo");
        String expiryDate    = request.getParameter("licenseExpiry");
        Integer driverRating    = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date licenseExpiry = sdf.parse(expiryDate);

        Iterator<String> itr        = request.getFileNames();
        MultipartFile licenseImage  = request.getFile(itr.next());
        byte [] licensePhoto        = licenseImage.getBytes();

        //validation part
        if(driverLicenseNo == null || driverLicenseNo.trim().equals(""))
            throw new BadRequestException("Driver License No. Required.");

        if(licensePhoto == null )
            throw new BadRequestException("License Photo Required.");

        if(expiryDate == null || expiryDate.trim().equals(""))
            throw new BadRequestException("License Expiry Required.");

        //create new driver object
        User newDriver = new User(driverLicenseNo, licensePhoto, licenseExpiry, driverRating);

        return driverService.addDriver(user_id, newDriver);
    }

    @RequestMapping(value="/user/{user_id}/driver", method=RequestMethod.GET, produces = {"application/json"} )
    @ResponseStatus(HttpStatus.OK)
    public DriverDO getDriver(@PathVariable(value = "user_id") String user_id) throws Exception
    {
        return driverService.getDriver(user_id);
    }


    @RequestMapping(value="/user/{driver_id}/driver", method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public Integer updateDriverRating(@PathVariable(value = "driver_id") String driver_id, @RequestParam("rating") Integer rating) throws Exception
    {
        return driverService.updateRating(driver_id, rating);
    }


    @RequestMapping(value="/driver/{driver_id}/licensePhoto", method=RequestMethod.GET, produces = "image/jpeg")
    @ResponseStatus(HttpStatus.OK)
    public byte[] getImage(@PathVariable String driver_id) throws Exception
    {

        return signupService.getUserImage(driver_id);
    }

}
