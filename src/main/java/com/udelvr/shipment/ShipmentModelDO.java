package com.udelvr.shipment;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

public class ShipmentModelDO {

    @JsonProperty
    private String shipmentID;

    @JsonProperty @NotBlank
    private String recipientName;

    private String currentLocationLatitude;
    private String currentLocationLongitude;

    @JsonProperty @NotBlank
    private String sourceAddress;
    private String sourceLocationLatitude;
    private String sourceLocationLongitude;

    @JsonProperty @NotBlank
    private String destinationAddress;
    private String destinationLocationLatitude;
    private String destinationLocationLongitude;

    private String packageDescription;

    @JsonProperty @NotBlank
    private String packageWeight;

    @JsonProperty @NotBlank
    private String pickupTime;

    @JsonProperty @NotBlank
    private String pickupDate;

    private String imageUrl;
    private String compressedImageUrl;
    private String thumbnailUrl;

    @JsonProperty
    private String customerID;

    @JsonProperty @NotBlank
    private String status;

    @JsonProperty
    private String driverID;

    @JsonProperty
    private String amount;

    public ShipmentModelDO() {
    }

    public ShipmentModelDO(String ShipmentID, String recipientName, String currentLocationLatitude, String currentLocationLongitude,
                           String sourceAddress, String sourceLocationLatitude, String sourceLocationLongitude,
                           String destinationAddress, String destinationLocationLatitude, String destinationLocationLongitude,
                           String packageDescription, String packageWeight, String pickupTime, String pickupDate, String imageUrl,
                           String customerID, String status, String driverID , String amount,
                           String compressedImageUrl, String thumbnailUrl)
    {
        this.shipmentID = ShipmentID;
        this.recipientName = recipientName;
        this.currentLocationLatitude = currentLocationLatitude;
        this.currentLocationLongitude= currentLocationLongitude;
        this.sourceAddress = sourceAddress;
        this.sourceLocationLatitude = sourceLocationLatitude;
        this.sourceLocationLongitude = sourceLocationLongitude;
        this.destinationAddress=destinationAddress;
        this.destinationLocationLatitude=destinationLocationLatitude;
        this.destinationLocationLongitude=destinationLocationLongitude;
        this.packageDescription=packageDescription;
        this.packageWeight = packageWeight;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
        this.imageUrl = imageUrl;
        this.driverID = driverID;
        this.customerID = customerID;
        this.status = status;
        this.amount = amount;
        this.compressedImageUrl = compressedImageUrl;
        this.thumbnailUrl = thumbnailUrl;

    }


    public String getShipmentID() {
        return shipmentID;
    }

    public void setShipmentID(String shipmentID) {
        this.shipmentID = shipmentID;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getCurrentLocationLatitude() {
        return currentLocationLatitude;
    }

    public void setCurrentLocationLatitude(String currentLocationLatitude) {
        this.currentLocationLatitude = currentLocationLatitude;
    }

    public String getCurrentLocationLongitude() {
        return currentLocationLongitude;
    }

    public void setCurrentLocationLongitude(String currentLocationLongitude) {
        this.currentLocationLongitude = currentLocationLongitude;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getSourceLocationLatitude() {
        return sourceLocationLatitude;
    }

    public void setSourceLocationLatitude(String sourceLocationLatitude) {
        this.sourceLocationLatitude = sourceLocationLatitude;
    }

    public String getSourceLocationLongitude() {
        return sourceLocationLongitude;
    }

    public void setSourceLocationLongitude(String sourceLocationLongitude) {
        this.sourceLocationLongitude = sourceLocationLongitude;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getDestinationLocationLatitude() {
        return destinationLocationLatitude;
    }

    public void setDestinationLocationLatitude(String destinationLocationLatitude) {
        this.destinationLocationLatitude = destinationLocationLatitude;
    }

    public String getDestinationLocationLongitude() {
        return destinationLocationLongitude;
    }

    public void setDestinationLocationLongitude(String destinationLocationLongitude) {
        this.destinationLocationLongitude = destinationLocationLongitude;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public void setPackageDescription(String packageDescription) {
        this.packageDescription = packageDescription;
    }

    public String getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(String packageWeight) {
        this.packageWeight = packageWeight;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCompressedImageUrl() {
        return compressedImageUrl;
    }

    public void setCompressedImageUrl(String compressedImageUrl) {
        this.compressedImageUrl = compressedImageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }


}
