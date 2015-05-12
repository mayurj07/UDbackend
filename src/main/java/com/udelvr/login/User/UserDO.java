package com.udelvr.login.User;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class UserDO {

    @Id
    @JsonProperty
    private String userId;

    @JsonProperty
    private String fullName;

    @JsonProperty
    private String mobileNo;

    @JsonProperty
    private String email;

    @JsonProperty @NotBlank
    private String password;

    @JsonProperty
    private String deviceId;

    @JsonProperty
    private String profileURL;

    public UserDO(){}

    public UserDO(
            @JsonProperty String userId,
            @JsonProperty String fullName,
            @JsonProperty String email,
            @JsonProperty String mobileNo,
            @JsonProperty String password,
            @JsonProperty String deviceId,
            @JsonProperty String profileURL)
            {
                this.userId             = userId;
                this.fullName           = fullName;
                this.mobileNo           = mobileNo;
                this.email              = email;
                this.password           = password;
                this.deviceId           = deviceId;
                this.profileURL         = profileURL;
            }


    public String getUserId() { return userId;  }

    public void setUserId(String userId) {  this.userId = userId;   }

    public String getFullName() {   return fullName;    }

    public void setFullName(String fullName) {  this.fullName = fullName;    }

    public String getMobileNo() {   return mobileNo;    }

    public void setMobileNo(String mobileNo) {  this.mobileNo = mobileNo;   }

    public String getEmail() {  return email;   }

    public void setEmail(String email) {    this.email = email; }

    public String getPassword() {   return password;    }

    public void setPassword(String password) {  this.password = password;   }

    public String getDeviceId() {   return deviceId;    }

    public void setDeviceId(String deviceId) {  this.deviceId = deviceId;   }

    public String getProfileURL() {     return profileURL;     }

    public void setProfileURL(String profileURL) {    this.profileURL = profileURL;   }

}
