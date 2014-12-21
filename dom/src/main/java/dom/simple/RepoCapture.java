package dom.simple;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.value.Blob;
import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;
import org.isisaddons.wicket.gmap3.cpt.applib.Location;



@DomainService(menuOrder = "40", repositoryFor = Capture.class)
@Named("Capture")
public class RepoCapture {
    
	public String getId() {
        return "Capture";
    }
	
	@Bookmarkable
    @ActionSemantics(Of.SAFE)
    @MemberOrder(sequence = "1")
    public List<Capture> listAll() {
        return container.allInstances(Capture.class);
    }

    public String captureUpload(@Named("captureMacFile") Blob captureMacFile,@Named("captureGPSFile")Blob captureGPSFile) throws IOException {
        
    	File file = new File("fis");
    	FileOutputStream fileOutputStream = new FileOutputStream(file);
    	captureGPSFile.writeBytesTo(fileOutputStream);	
    	fileOutputStream.close();
    	BufferedReader buf = new BufferedReader(new FileReader(file));
    	String linea="";
    	while ((linea=buf.readLine())!=null)
    	{
    		//$GPGLL,3866.63489,S,06812.71590,W,220554.00,A,A*6C
    		if (linea.contains("$GPGLL"))
    		{
    			String[] cord= linea.split(",");
    			if (cord[1].isEmpty() || cord[2].isEmpty() || cord[3].isEmpty() || cord[4].isEmpty())
    			{
    			}
    			else
    			{
    			Capture cap =container.newTransientInstance(Capture.class);
    			cap.setBSSId(cord[1]);
    			//format example 51,520283;0,082858
    			
    			Location location = new Location(NMEA.Latitude2Decimal(cord[1], cord[2]),NMEA.Longitude2Decimal(cord[3], cord[4]));
    			cap.setLocation(location);
    			container.persistIfNotAlready(cap);
    			}
    		}
    	}
    	buf.close();
		return "ok";
    	
    	
    }

    
    

    public String captureUpload2(@Named("captureMacFile") Blob captureMacFile,@Named("captureGPSFile")Blob captureGPSFile) throws IOException, ParseException {
        
    	File fileMAC = new File("mac");
    	FileOutputStream fileOutputStream = new FileOutputStream(fileMAC);
    	captureMacFile.writeBytesTo(fileOutputStream);	
    	fileOutputStream.close();
    	BufferedReader buf = new BufferedReader(new FileReader(fileMAC));
    	String linea="";
    	String texto="";
    	File fileGPS = new File("gps");
    	FileOutputStream fileOutputStreamGPS = new FileOutputStream(fileGPS);
    	captureGPSFile.writeBytesTo(fileOutputStreamGPS);	
    	fileOutputStreamGPS.close();
    	BufferedReader bufGPS = new BufferedReader(new FileReader(fileGPS));
    	
    	Location location = new Location(); 
    	String lineGPS="";
    	String lineFecha="";
    	do
    	{
    		lineGPS=bufGPS.readLine();
    		
    		if (lineGPS.contains("$GPGLL"))
    		{
    			String[] cord= lineGPS.split(",");
    			if (cord[1].isEmpty() || cord[2].isEmpty() || cord[3].isEmpty() || cord[4].isEmpty())
    			{
    			}
    			else
    			{
    			location = new Location(NMEA.Latitude2Decimal(cord[1], cord[2]),NMEA.Longitude2Decimal(cord[3], cord[4]));
    			}	
    		}
    		
    		
    	}while(lineGPS.contains("$GPGLL")!=true);
    	lineFecha = bufGPS.readLine();
    	Date dateTime = NMEA.stringNMEAToDate(lineFecha);
    	
    	while ((linea=buf.readLine())!=null && lineGPS!=null)
    	{
    	
    	     //"Time","Receiver address","Destination address","Transmitter address","Source address","BSS Id","SSID"
    	     //"2014-12-17 02:45:00.641906000","ff:ff:ff:ff:ff:ff","ff:ff:ff:ff:ff:ff","8c:3a:e3:10:60:45","8c:3a:e3:10:60:45","ff:ff:ff:ff:ff:ff",""
    	     String[] splitter = linea.split(",");
    	     
    	     
    	     
    	     
    	    if (splitter[0].contains("Time")==false)
    	    {
    	    Date dateMAC =NMEA.stringMACtoDate(splitter[0].replace("\"", ""));
    	        if (dateTime.before(dateMAC))
    	        {
    	        	lineGPS=bufGPS.readLine();
    	    		
    	    		if (lineGPS.contains("$GPGLL"))
    	    		{
    	    			String[] cord= lineGPS.split(",");
    	    			if (cord[1].isEmpty() || cord[2].isEmpty() || cord[3].isEmpty() || cord[4].isEmpty())
    	    			{
    	    			}
    	    			else
    	    			{
    	    			location = new Location(NMEA.Latitude2Decimal(cord[1], cord[2]),NMEA.Longitude2Decimal(cord[3], cord[4]));
    	    			}	
    	    		}  
    	    		lineFecha = bufGPS.readLine();
    	    		dateTime = NMEA.stringNMEAToDate(lineFecha);
    	        }
    	    
    	        
    	    texto = dateMAC.toString();
    	    Capture cap =container.newTransientInstance(Capture.class);
 			cap.setBSSId(splitter[5].replace("\"", ""));
 			cap.setReceiverAddress(splitter[1].replace("\"", ""));
 			cap.setDestinationAddress(splitter[2].replace("\"", ""));
 			cap.setTransmitterAddress(splitter[3].replace("\"", ""));
 			cap.setSourceAddress(splitter[4].replace("\"", ""));
 			cap.setBSSId(splitter[5].replace("\"", ""));
 			cap.setSSID(splitter[6].replace("\"", ""));
 			cap.setLocation(location);
 			container.persistIfNotAlready(cap);
    	    }
    	}
    	
    	buf.close();
    	bufGPS.close();
		return texto;    	
    	
    }

    
    
    
    
    
    @javax.inject.Inject 
    DomainObjectContainer container;
	
}
