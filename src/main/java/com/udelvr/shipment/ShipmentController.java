package com.udelvr.shipment;

import com.udelvr.compression.CompressImage;
import com.udelvr.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;



@RestController
@Component("ShipmentController")
public class ShipmentController {

    @Autowired
    ShipmentService shipmentService = new ShipmentService();

    CompressImage compressUtility = new CompressImage();

    //create new shipment
    @ResponseBody
    @RequestMapping(value="/user/{user_id}/shipment", method= RequestMethod.POST)
    public ResponseEntity<ShipmentModelDO> createShipment(@PathVariable(value = "user_id") String user_id , MultipartHttpServletRequest request) throws IOException
    {
        String shipmentID           = generateID();
        String recipientName        = request.getParameter("recipientName");
        String sourceAddress        = request.getParameter("sourceAddress");
        String sourceLat            = request.getParameter("sourceLat");
        String sourceLong           = request.getParameter("sourceLong");
        String destinationAddress   = request.getParameter("destinationAddress");
        String destinationLat       = request.getParameter("destinationLat");
        String destinationLong      = request.getParameter("destinationLong");
        String packageDescription   = request.getParameter("packageDescription");
        String packageWeight        = request.getParameter("packageWeight");
        String pickupTime           = request.getParameter("pickupTime");
        String pickupDate           = request.getParameter("pickupDate");

        double sLatitude            = Double.parseDouble(sourceLat);
        double sLongitude           = Double.parseDouble(sourceLong);
        double dLatitude            = Double.parseDouble(destinationLat);
        double dLongitude           = Double.parseDouble(destinationLong);

        //status is created when the package is first posted
        String status               = "created";

        // no driver assigned when package is created ;
        String driverID             = "";

        String customerID           = user_id;

        Iterator<String> itr        = request.getFileNames();
        MultipartFile imageFile = request.getFile(itr.next());

        //compression
        File originalFile           = compressUtility.convertToFile(imageFile);
        File compressedFile         = compressUtility.compress(originalFile);
        byte[] compressedImage      = compressUtility.convertFileToByteArray(compressedFile);
        File thumbnailFile          = compressUtility.saveScaledImage(compressedFile);
        byte[] thumbnailImage       = compressUtility.convertFileToByteArray(thumbnailFile);
        //end of compression

        byte[] shipmentImage = imageFile.getBytes();

        //validation
        if(recipientName == null || recipientName.trim().equals(""))
            throw new BadRequestException("Please enter recipient name.");

        if(sourceAddress == null || sourceAddress.trim().equals(""))
            throw new BadRequestException("Please enter source address.");

        if(destinationAddress == null || destinationAddress.trim().equals(""))
            throw new BadRequestException("Please enter destination address.");

        if(packageWeight == null || packageWeight.trim().equals(""))
            throw new BadRequestException("Please enter package weight.");

        if(pickupTime == null || pickupTime.trim().equals(""))
            throw new BadRequestException("Please enter package pickup time.");

        if(pickupDate == null || pickupDate.trim().equals(""))
            throw new BadRequestException("Please enter package pickup date.");

        float amount = Computation.calculateAmount(sLatitude, sLongitude, dLatitude, dLongitude, packageWeight);
        
        ShipmentModel newShipment = new ShipmentModel(shipmentID ,recipientName, sourceAddress, sLatitude, sLongitude, destinationAddress,
                dLatitude, dLongitude ,packageDescription, packageWeight, pickupTime, pickupDate, driverID , customerID , status , amount,
                shipmentImage, compressedImage, thumbnailImage);

        return new ResponseEntity<ShipmentModelDO>(shipmentService.addShipment(newShipment), HttpStatus.CREATED);

    }


    //get shipment image
    @RequestMapping(value="/shipment/{shipmentID}/image/{type}", method=RequestMethod.GET , produces = "image/jpeg")
    public ResponseEntity<byte[]> getOneShipmentImage(@PathVariable(value = "shipmentID") String shipment_id, @PathVariable(value = "type") String type) throws Exception
    {
        return new ResponseEntity<byte[]>(shipmentService.getShipmentImage(shipment_id, type), HttpStatus.OK);
    }

    //get shipment
    @RequestMapping(value="/shipment/{shipmentID}", method=RequestMethod.GET)
    public ResponseEntity<ShipmentModelDO> getOneShipment(@PathVariable(value = "shipmentID") String shipment_id) throws Exception
    {
        return new ResponseEntity<ShipmentModelDO>(shipmentService.getShipment(shipment_id), HttpStatus.OK);
    }

    //delete shipment
    @RequestMapping(value="/shipment/{shipmentID}", method=RequestMethod.DELETE)
    public ResponseEntity deleteShipment(@PathVariable(value = "shipmentID") String shipment_id) throws Exception
    {
        shipmentService.deleteShipment(shipment_id);
        return new ResponseEntity(HttpStatus.OK);
    }

    //get all customer's shipments
    @RequestMapping(value="/user/{ID}/shipments", method=RequestMethod.GET)
    public ResponseEntity<List<ShipmentModelDO>> getAllCustomerShipments(@PathVariable(value = "ID") String customerID) throws Exception
    {
        System.out.println(customerID);
        return new ResponseEntity<List<ShipmentModelDO>>(shipmentService.getAllCustomerShipment(customerID), HttpStatus.OK);
    }

    //all shipment in created status for drivers
    @RequestMapping(value="/driver/shipments", method=RequestMethod.GET)
    public ResponseEntity<List<ShipmentModelDO>> getAllActiveShipmentsForDriver() throws Exception
    {
        return new ResponseEntity<List<ShipmentModelDO>>(shipmentService.getAllActiveShipmentsForDriver(), HttpStatus.OK);
    }

    //all shipment in drivers vicinity and package status is created
    @RequestMapping(value="/driver/{latitude}/{longitude}/{radius}/shipment", method=RequestMethod.GET)
    public ResponseEntity<List<ShipmentModelDO>> getAllActiveShipmentsInDriversVicinity(@PathVariable(value = "latitude") String latitude, @PathVariable(value = "longitude") String longitude , @PathVariable(value = "radius") String radius) throws Exception
    {
        double sLatitude            = Double.parseDouble(latitude);
        double sLongitude           = Double.parseDouble(longitude);
        int    rad                  = Integer.parseInt(radius);

        return new ResponseEntity<List<ShipmentModelDO>>(shipmentService.getAllActiveShipmentsInDriverVicinity(sLatitude,sLongitude,rad), HttpStatus.OK);
    }

    //update the driver id and the status to accepted once shipment accepted by driver
    @RequestMapping(value="/shipment/accept/{shipmentID}/{driverID}", method=RequestMethod.PUT)
    public ResponseEntity changeStatustoAccept(@PathVariable(value = "shipmentID") String shipmentID , @PathVariable(value = "driverID") String driverID) throws Exception
    {
        shipmentService.changeStatustoAccept(shipmentID,driverID);
        return new ResponseEntity(HttpStatus.OK);
    }


    //update the status to complete once finish
    @RequestMapping(value="/shipment/complete/{shipmentID}", method=RequestMethod.PUT)
    public ResponseEntity shipmentComplete(@PathVariable(value = "shipmentID") String shipmentID) throws Exception
    {
        shipmentService.shipmentComplete(shipmentID);
        return new ResponseEntity(HttpStatus.OK);
    }

    //update shipment current location
    @RequestMapping(value="/shipment/currentLocation/{latitude}/{longitude}/{shipmentID}", method=RequestMethod.PUT)
    public ResponseEntity shipmentUpdateCurrentPosition(@PathVariable(value = "shipmentID") String shipmentID , @PathVariable(value = "latitude") String latitude , @PathVariable(value = "longitude") String longitude) throws Exception
    {
        double newlatitude            = Double.parseDouble(latitude);
        double newlongitude           = Double.parseDouble(longitude);
        shipmentService.shipmentUpdateCurrentPosition(shipmentID, newlatitude, newlongitude);
        return new ResponseEntity(HttpStatus.OK);
    }

    //get shipment current location
    @RequestMapping(value="/shipment/currentLocation/{shipmentID}", method=RequestMethod.GET)
    public ResponseEntity<String[]> shipmentGetCurrentPosition(@PathVariable(value = "shipmentID") String shipmentID) throws Exception
    {
        return new ResponseEntity<String[]>(shipmentService.shipmentGetCurrentPosition(shipmentID),HttpStatus.OK);
    }

    public static String generateID()
    {
        int aNumber = (int)((Math.random() * 900000)+100000);
        return ""+aNumber;
    }
}
