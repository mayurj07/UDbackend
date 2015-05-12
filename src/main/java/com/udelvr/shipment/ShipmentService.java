package com.udelvr.shipment;

import com.mongodb.*;
import com.udelvr.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class ShipmentService {

    public static final String COLLECTION_NAME = "shipment";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBCollection collection;

    public ShipmentModelDO addShipment(ShipmentModel shipment) {
        if (!mongoTemplate.collectionExists(COLLECTION_NAME)) {
            mongoTemplate.createCollection(COLLECTION_NAME);
        }
        mongoTemplate.insert(shipment,COLLECTION_NAME);
        ShipmentModelDO shipment1 = getShipment(shipment.getShipmentID());
        return shipment1;
    }

    public byte[] getShipmentImage(String shipmentId, String image_type)
    {
        Query searchUserQuery = new Query(Criteria.where("shipmentID").is(shipmentId));
        ShipmentModel shipment = mongoTemplate.findOne(searchUserQuery, ShipmentModel.class, COLLECTION_NAME);
        if(shipment == null)
            throw new BadRequestException("Shipment does not exists.");

        if(image_type.equalsIgnoreCase("original"))
        {
            byte[] originalImage = shipment.getShipmentImage();
            return originalImage;
        }
        else if(image_type.equalsIgnoreCase("compressed"))
        {
            byte[] compressedImage = shipment.getCompressedImage();
            return compressedImage;
        }else if (image_type.equalsIgnoreCase("thumbnail"))
        {
            byte[] thumbnailImage = shipment.getThumbnailImage();
            return thumbnailImage;
        }
        else {
            throw new BadRequestException("Shipment image not found");
        }
    }


    public ShipmentModelDO getShipment(String shipmentId)
    {
        Query searchUserQuery = new Query(Criteria.where("shipmentID").is(shipmentId));
        ShipmentModel shipment = mongoTemplate.findOne(searchUserQuery, ShipmentModel.class, COLLECTION_NAME);
        if(shipment == null)
            throw new BadRequestException("Shipment does not exists.");

        String imageURL = "/shipment/"+shipment.getShipmentID()+"/image/original";
        String compressedImageURL = "/shipment/"+shipment.getShipmentID()+"/image/compressed";
        String thumbnailURL = "/shipment/"+shipment.getShipmentID()+"/image/thumbnail";

        ShipmentModelDO shipmentDO = new ShipmentModelDO(
                shipment.getShipmentID(),
                shipment.getRecipientName(),
                shipment.getCurrentLocation().getCoordinates()[0],
                shipment.getCurrentLocation().getCoordinates()[1],
                shipment.getSourceAddress(),
                shipment.getSourceLocation().getCoordinates()[0],
                shipment.getSourceLocation().getCoordinates()[1],
                shipment.getDestinationAddress(),
                shipment.getDestinationLocation().getCoordinates()[0],
                shipment.getDestinationLocation().getCoordinates()[1],
                shipment.getPackageDescription(),
                shipment.getPackageWeight(),
                shipment.getPickupTime(),
                shipment.getPickupDate(),
                imageURL,
                shipment.getCustomerID(),
                shipment.getStatus(),
                shipment.getDriverID(),
                Float.toString(shipment.getAmount()),
                compressedImageURL,
                thumbnailURL
                );
        return shipmentDO;
    }


    public List<ShipmentModelDO> getAllCustomerShipment(String customerId) {
        Query searchUserQuery = new Query(Criteria.where("customerID").is(customerId));
        return getShipmentResults(searchUserQuery);
    }


    public List<ShipmentModelDO> getAllActiveShipmentsForDriver()
    {
        Query searchUserQuery = new Query(Criteria.where("status").is("created").andOperator(Criteria.where("driverID").is("")));
        return getShipmentResults(searchUserQuery);
    }


    public List<ShipmentModelDO> getShipmentResults(Query query)
    {
        List<ShipmentModel> shipments = mongoTemplate.find(query, ShipmentModel.class, COLLECTION_NAME);
        String shipmentURL;
        List<ShipmentModelDO> newShipmentDOs = new ArrayList<ShipmentModelDO>();;
        for(ShipmentModel shipment : shipments)
        {
            String imageURL = "/shipment/"+shipment.getShipmentID()+"/image/original";
            String compressedImageURL = "/shipment/"+shipment.getShipmentID()+"/image/compressed";
            String thumbnailURL = "/shipment/"+shipment.getShipmentID()+"/image/thumbnail";

            ShipmentModelDO newShipmentDO = new ShipmentModelDO(
                    shipment.getShipmentID(),
                    shipment.getRecipientName(),
                    shipment.getCurrentLocation().getCoordinates()[0],
                    shipment.getCurrentLocation().getCoordinates()[1],
                    shipment.getSourceAddress(),
                    shipment.getSourceLocation().getCoordinates()[0],
                    shipment.getSourceLocation().getCoordinates()[1],
                    shipment.getDestinationAddress(),
                    shipment.getDestinationLocation().getCoordinates()[0],
                    shipment.getDestinationLocation().getCoordinates()[1],
                    shipment.getPackageDescription(),
                    shipment.getPackageWeight(),
                    shipment.getPickupTime(),
                    shipment.getPickupDate(),
                    imageURL,
                    shipment.getCustomerID(),
                    shipment.getStatus(),
                    shipment.getDriverID(),
                    Float.toString(shipment.getAmount()),
                    compressedImageURL,
                    thumbnailURL
            );
            newShipmentDOs.add(newShipmentDO);
        }
        return newShipmentDOs;
    }


    public List<ShipmentModelDO> getAllActiveShipmentsInDriverVicinity(double latitude , double longitude , int radius)
    {
        double[] coordinates = new double[] { longitude, latitude };

        DBObject query = BasicDBObjectBuilder.start()
                .push("sourceLocation")
                .push("$near")
                .add("$maxDistance", radius)
                .push("$geometry")
                .add("type", "Point")
                .add("coordinates", coordinates)
                .get();

        BasicDBObject andQuery = new BasicDBObject();
        List<DBObject> obj = new ArrayList<DBObject>();
        obj.add(query);
        obj.add(new BasicDBObject("status", "created"));
        andQuery.put("$and", obj);

        DBCursor cursor = collection.find(andQuery);
        List<BasicDBList> shipments = new ArrayList<BasicDBList>();
        List<ShipmentModelDO> newShipmentDOs = new ArrayList<ShipmentModelDO>();
        String shipmentURL;
        while (cursor.hasNext())
        {
            DBObject result = cursor.next();
            String imageURL = "/shipment/"+result.get("shipmentID").toString()+"/image/original";
            String compressedImageURL = "/shipment/"+result.get("shipmentID").toString()+"/image/compressed";
            String thumbnailURL = "/shipment/"+result.get("shipmentID").toString()+"/image/thumbnail";


            ShipmentModelDO newShipmentDO = new ShipmentModelDO(
                    result.get("shipmentID").toString(),
                    result.get("recipientName").toString(),
                    getCoordinates(result.get("currentLocation"))[0],//latitude
                    getCoordinates(result.get("currentLocation"))[1],//longitude
                    result.get("sourceAddress").toString(),
                    getCoordinates(result.get("sourceLocation"))[0],
                    getCoordinates(result.get("sourceLocation"))[1],
                    result.get("destinationAddress").toString(),
                    getCoordinates(result.get("destinationLocation"))[0],
                    getCoordinates(result.get("destinationLocation"))[1],
                    result.get("packageDescription").toString(),
                    result.get("packageWeight").toString(),
                    result.get("pickupTime").toString(),
                    result.get("pickupDate").toString(),
                    imageURL,
                    result.get("customerID").toString(),
                    result.get("status").toString(),
                    result.get("driverID").toString(),
                    result.get("amount").toString(),
                    compressedImageURL,
                    thumbnailURL
            );
            newShipmentDOs.add(newShipmentDO);

        }
        return newShipmentDOs;
    }


    private String[] getCoordinates(Object location) {
        String reqData = location.toString();
        String res = reqData.substring(reqData.indexOf("[") + 1, reqData.indexOf("]"));
        String[] result = res.split(",");
        return result;
    }



    public void deleteShipment(String shipmentId)
    {
        Query searchUserQuery = new Query(Criteria.where("shipmentID").is(shipmentId));
        mongoTemplate.remove(searchUserQuery, ShipmentModel.class, COLLECTION_NAME);
    }


    public void changeStatustoAccept(String shipmentId, String driverID)
    {
        Update update = new Update();
        update.set("driverID",driverID);
        update.set("status","accepted");

        Query searchUserQuery1 = new Query(Criteria.where("shipmentID").is(shipmentId));
        ShipmentModel shipment = mongoTemplate.findAndModify(searchUserQuery1, update, ShipmentModel.class, COLLECTION_NAME);
        if(shipment == null)
            throw new BadRequestException("Shipment does not exists.");

    }


    public void shipmentComplete(String shipmentId)
    {
        Update update = new Update();
        update.set("status","completed");
        Query searchUserQuery = new Query(Criteria.where("shipmentID").is(shipmentId));
        ShipmentModel shipment = mongoTemplate.findAndModify(searchUserQuery, update,ShipmentModel.class, COLLECTION_NAME);
        if(shipment == null)
            throw new BadRequestException("Shipment does not exists.");

    }


    public void shipmentUpdateCurrentPosition(String shipmentID, double newlatitude, double newlongitude) {
        double[] coordinates = new double[] { newlongitude, newlatitude };
        Update update = new Update();
        update.set("currentLocation.coordinates",coordinates);
        Query searchUserQuery = new Query(Criteria.where("shipmentID").is(shipmentID));
        ShipmentModel shipment = mongoTemplate.findAndModify(searchUserQuery, update,ShipmentModel.class, COLLECTION_NAME);
        if(shipment == null)
            throw new BadRequestException("Shipment does not exists.");

    }


    public String[] shipmentGetCurrentPosition(String shipmentID) {

        Query searchUserQuery = new Query(Criteria.where("shipmentID").is(shipmentID));
        ShipmentModel shipment = mongoTemplate.findOne(searchUserQuery, ShipmentModel.class, COLLECTION_NAME);
        if(shipment == null)
            throw new BadRequestException("Shipment does not exists.");

        String[] coordinates = new String[]{ shipment.getCurrentLocation().getCoordinates()[0] ,shipment.getCurrentLocation().getCoordinates()[1] };
        return coordinates;

    }
}
