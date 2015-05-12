package com.udelvr.login.driver;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class  DriverDO {

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
    private String driverLicenseNo;

    @JsonProperty
    private String licenseExpiry;

    @JsonProperty
    private Integer driverRating;

    @JsonProperty
    private String profileURL;

    @JsonProperty
    private String licensePhotoURL;


    public DriverDO(
            @JsonProperty String userId,
            @JsonProperty String fullName,
            @JsonProperty String email,
            @JsonProperty String mobileNo,
            @JsonProperty String password,
            @JsonProperty String deviceId,
            @JsonProperty String driverLicenseNo,
            @JsonProperty String licenseExpiry,
            @JsonProperty Integer driverRating,
            @JsonProperty String profileURL,
            @JsonProperty String licensePhotoURL)
    {
        this.userId = userId;
        this.fullName           = fullName;
        this.mobileNo           = mobileNo;
        this.email              = email;
        this.password           = password;
        this.deviceId           = deviceId;
        this.driverLicenseNo    = driverLicenseNo;
        this.licenseExpiry      = licenseExpiry;
        this.driverRating       = driverRating;
        this.profileURL         = profileURL;
        this.licensePhotoURL    = licensePhotoURL;
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

    public String getDriverLicenseNo() {    return driverLicenseNo;     }

    public void setDriverLicenseNo(String driverLicenseNo) {    this.driverLicenseNo = driverLicenseNo;     }

    public String getLicenseExpiry() {    return licenseExpiry;   }

    public void setLicenseExpiry(String licenseExpiry) {  this.licenseExpiry = licenseExpiry;     }

    public Integer getDriverRating() {  return driverRating;   }

    public void setDriverRating(Integer driverRating) {     this.driverRating = driverRating;   }

    public String getProfileURL() {     return profileURL;     }

    public void setProfileURL(String profileURL) {      this.profileURL = profileURL;   }

    public String getLicensePhotoURL() { return licensePhotoURL;    }

    public void setLicensePhotoURL(String licensePhotoURL) {  this.licensePhotoURL = licensePhotoURL;   }

}
