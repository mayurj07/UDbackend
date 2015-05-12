package com.udelvr.login.signUp;

import com.udelvr.UdelvrConfig;
import com.udelvr.compression.CompressImage;
import com.udelvr.exceptions.BadRequestException;
import com.udelvr.login.User.User;
import com.udelvr.login.User.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Repository;

@Repository
public class SignupService {

    public static final String COLLECTION_NAME = "users";

    EmailAuth emailAuth = new EmailAuth();

    @Autowired
    private MongoTemplate mongoTemplate;

    CompressImage compressUtility = new CompressImage();

    //genetrate alphanumericID
    public static String generateBase36ID()
    {
        int id = 0;
        id = (int)((Math.random() * 900000000)+100000000);
        //System.out.print((aNumber));
        return Integer.toString(id, 36).toUpperCase();
    }


    public UserDO addUser(User user) {

        if (!mongoTemplate.collectionExists(COLLECTION_NAME)) {
            mongoTemplate.createCollection(COLLECTION_NAME);
        }

        ApplicationContext ctx = new AnnotationConfigApplicationContext(UdelvrConfig.class);
        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
        BasicQuery findIfUserExists = new BasicQuery("{ email : '"+ user.getEmail() +"' }");
        User userTest = mongoOperation.findOne(findIfUserExists, User.class);

        if(userTest != null)
        {
            System.out.println("User Already Exits. Sorry.");
            throw new BadRequestException("User Already Exits. Sorry.");
        }
        else
        {
            try     //insert User's image into MongoDB
            {
                mongoTemplate.insert(user, COLLECTION_NAME);
            } catch (Exception e) { e.printStackTrace(); }

            return getUserData(user.getUserId());
        }
    }


    public UserDO getUserData(String user_id)
    {
        User user = mongoTemplate.findById(user_id , User.class, "users");

        String profileURL = "/user/"+user_id+"/profilepic";

        UserDO user_domain = new UserDO(
                user.getUserId(),
                user.getFullName(),
                user.getEmail(),
                user.getMobileNo(),
                user.getPassword(),
                user.getDeviceId(),
                profileURL);

        return user_domain;
    }


    public byte[] getUserImage(String user_id)throws Exception
    {
        try
        {
            User user = mongoTemplate.findById(user_id, User.class, "users");
            byte[] c = user.getPhoto();
            return c;

        }catch (Exception e) {
            throw new BadRequestException("User Not Found.");
        }

    }

    public boolean verifyEmail(String id,String auth) throws Exception{

        //System.out.println("id---" + id);

        User user = mongoTemplate.findById(id, User.class, "users");

        String reverseAuth = emailAuth.hmacDigest(user.getUserId(), "udelvr", "HmacSHA1");

        if(reverseAuth.equals(auth))
        {
            //System.out.println("Authenticated");
            return true;
        }
        else
        {
            return false;
        }
    }

}
