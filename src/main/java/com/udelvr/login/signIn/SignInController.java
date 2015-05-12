package com.udelvr.login.signIn;


import com.udelvr.exceptions.BadRequestException;
import com.udelvr.login.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;


@RestController
@Component("SignInController")
public class SignInController{

    @Autowired
    SignInService signInService = new SignInService();

    /*************************************   Sign-In REST APIs  **********************************************/

    @RequestMapping(value="/user/signin", method=RequestMethod.POST)
    public ResponseEntity<User> userSignIn(MultipartHttpServletRequest request) throws Exception
    {
        String email            = request.getParameter("email");
        String password         = request.getParameter("password");

        User user = new User();
        if(email == null || email.trim().equals(""))
            throw new BadRequestException("Email Required.");

        if(password == null || password.trim().equals(""))
            throw new BadRequestException("Password Required.");

        user.setEmail(email); user.setPassword(password);

        return new ResponseEntity<User>(signInService.signInUser(user),HttpStatus.OK);

    }

}
