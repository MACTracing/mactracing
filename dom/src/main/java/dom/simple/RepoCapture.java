package dom.simple;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
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
    			if (cord[1].isEmpty() || cord[2].isEmpty())
    			{
    			}
    			else
    			{
    			Capture cap =container.newTransientInstance(Capture.class);
    			cap.setBSSId(cord[1]);
    			cap.setMac(cord[3]);
    			//format example 51,520283;0,082858
    			Location location = new Location(51.520283, 0.082858);
    			cap.setLocation(location);
    			container.persistIfNotAlready(cap);
    			}
    		}
    	}
    	buf.close();
		return "ok";
    	
    	
    }
    
    public Capture create (String bssid,String mac) {
        final Capture capture = container.newTransientInstance(Capture.class);
        capture.setBSSId(bssid);
        capture.setMac(mac);
        container.persist(capture);
    	return capture;
    }
    
    
    @javax.inject.Inject 
    DomainObjectContainer container;
	
}
