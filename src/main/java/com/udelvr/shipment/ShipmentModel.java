package com.udelvr.shipment;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

public class ShipmentModel {

    @JsonProperty
    private String shipmentID;

    @JsonProperty @NotBlank
    private String recipientName;

    private Location currentLocation;

    @JsonProperty @NotBlank
    private String sourceAddress;
    private Location sourceLocation;

    @JsonProperty @NotBlank
    private String destinationAddress;
    private Location destinationLocation;

    private String packageDescription;

    @JsonProperty @NotBlank
    private String packageWeight;

    @JsonProperty @NotBlank
    private String pickupTime;

    @JsonProperty @NotBlank
    private String pickupDate;

    @JsonProperty
    private String customerID;

    @JsonProperty @NotBlank
    private String status;

    @JsonProperty
    private String driverID;

    private float amount;

    private byte[] shipmentImage;

    public byte[] compressedImage;

    public byte[] thumbnailImage;


    public ShipmentModel() {
    }

    public ShipmentModel(String ShipmentID, String recipientName, String sourceAddress, double sourceLat, double sourceLong, String destinationAddress, double destinationLat, double destinationLong,
                         String packageDescription, String packageWeight, String pickupTime, String pickupDate, String driverID, String customerID, String status, float amount,
                         byte[] shipmentImage, byte[] compressedImage, byte[] thumbnailImage)
    {
        this.shipmentID             = ShipmentID;
        this.recipientName          = recipientName;
        this.currentLocation        = new Location(sourceLong,sourceLat);
        this.sourceAddress          = sourceAddress;
        this.sourceLocation         = new Location(sourceLong,sourceLat);
        this.destinationAddress     = destinationAddress;
        this.destinationLocation    = new Location(destinationLong,destinationLat);
        this.packageDescription     = packageDescription;
        this.packageWeight          = packageWeight;
        this.pickupDate             = pickupDate;
        this.pickupTime             = pickupTime;
        this.driverID               = driverID;
        this.customerID             = customerID;
        this.status                 = status;
        this.amount                 = amount;
        this.shipmentImage          = shipmentImage;
        this.compressedImage        = compressedImage;
        this.thumbnailImage         = thumbnailImage;
    }


    public String getShipmentID() {
        return shipmentID;
    }

    public void setShipmentID(String shipmentID) {
        shipmentID = shipmentID;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
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

    public byte[] getShipmentImage() {
        return shipmentImage;
    }

    public void setShipmentImage(byte[] shipmentImage) {
        this.shipmentImage = shipmentImage;
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

    public void setDriverID(String driver) {
        this.driverID = driver;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Location getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(Location sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public Location getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(Location destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public byte[] getCompressedImage() {    return compressedImage; }

    public void setCompressedImage(byte[] compressedImage) {    this.compressedImage = compressedImage; }

    public byte[] getThumbnailImage() { return thumbnailImage;  }

    public void setThumbnailImage(byte[] thumbnailImage) {  this.thumbnailImage = thumbnailImage;   }
}
