package com.udelvr.login.User;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "users")
public class User {

    @Id
    @JsonProperty
    private String userId;

    @JsonProperty @NotBlank
    private String fullName;

    @JsonProperty
    private String mobileNo;

    @JsonProperty @NotBlank
    @Email
    private String email;

    @JsonProperty @NotBlank
    private String password;

    @JsonProperty
    private String deviceId;

    @JsonProperty
    private String created_at;

    @JsonProperty
    private String driverLicenseNo;

    @JsonProperty  @JsonIgnore
    public byte[] licensePhoto;

    @JsonProperty
    private Date licenseExpiry;

    @JsonProperty
    private Integer driverRating;

    @JsonProperty
    private String profileURL;

    @JsonProperty
    private String licensePhotoURL;

    @JsonProperty @JsonIgnore
    public byte[] photo;

    @JsonProperty @JsonIgnore
    public byte[] compressedImage;

    @JsonProperty @JsonIgnore
    public byte[] thumbnailImage;

    public User(){}


    public User(
            @JsonProperty String userId,
            @JsonProperty String fullName,
            @JsonProperty String email,
            @JsonProperty String mobileNo,
            @JsonProperty String password,
            @JsonProperty String deviceId,
            @JsonProperty String created_at,
            @JsonProperty byte[] userImage,
            @JsonProperty byte[] compressedImage,
            @JsonProperty byte[] thumbnailImage)
            {
                this.userId = userId;
                this.fullName           = fullName;
                this.mobileNo           = mobileNo;
                this.email              = email;
                this.password           = password;
                this.deviceId           = deviceId;
                this.created_at         = created_at;
                this.driverLicenseNo    = "";
                this.licensePhoto       = new byte[0];
                this.licenseExpiry      = new Date();
                this.driverRating       = 0;
                this.photo              = userImage;
                this.compressedImage    = compressedImage;
                this.thumbnailImage     = thumbnailImage;
            }


    public User(
            @JsonProperty String driverLicenseNo,
            @JsonProperty byte[] licensePhoto,
            @JsonProperty Date licenseExpiry,
            @JsonProperty Integer driverRating)
            {
                this.driverLicenseNo    = driverLicenseNo;
                this.licensePhoto       = licensePhoto;
                this.licenseExpiry      = licenseExpiry;
                this.driverRating       = driverRating;
            }


    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getPassword() {
        return password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUserId() { return userId;  }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public byte[] getPhoto() {  return photo;   }

    public void setPhoto(byte[] photo) {    this.photo = photo; }

    public String getDriverLicenseNo() {  return driverLicenseNo;   }

    public void setDriverLicenseNo(String driverLicenseNo) {    this.driverLicenseNo = driverLicenseNo;     }

    public byte[] getLicensePhoto() {   return licensePhoto;    }

    public void setLicensePhoto(byte[] licensePhoto) {  this.licensePhoto = licensePhoto;   }

    public Date getLicenseExpiry() {  return licenseExpiry;   }

    public void setLicenseExpiry(Date licenseExpiry) {    this.licenseExpiry = licenseExpiry;    }

    public Integer getDriverRating() {  return driverRating;    }

    public void setDriverRating(Integer driverRating) { this.driverRating = driverRating;   }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public String getLicensePhotoURL() { return licensePhotoURL;    }

    public void setLicensePhotoURL(String licensePhotoURL) {  this.licensePhotoURL = licensePhotoURL;   }

    public byte[] getCompressedImage() {    return compressedImage; }

    public void setCompressedImage(byte[] compressedImage) {    this.compressedImage = compressedImage; }

    public byte[] getThumbnailImage() { return thumbnailImage;  }

    public void setThumbnailImage(byte[] thumbnailImage) {  this.thumbnailImage = thumbnailImage;   }

}

