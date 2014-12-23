package dom.simple;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.util.ObjectContracts;
import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;
import org.isisaddons.wicket.gmap3.cpt.applib.Location;
import javax.jdo.annotations.Query;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.Queries({
	@Query(name="traerPorMAC", language="JDOQL", value = "SELECT FROM dom.simple.Capture WHERE BSSId ==:mac || receiverAddress ==:mac ||  destinationAddress ==:mac ||  TransmitterAddress ==:mac || SourceAddress ==:mac ||  SSID ==:mac ") 
	})

@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")



@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER, 
        column="version")

@ObjectType("Captura")
@Bookmarkable
public class Capture implements Comparable<Capture> , Locatable  {


	private String BSSId;
    private String receiverAddress;
    private String destinationAddress;
    private String TransmitterAddress;
    private String SourceAddress;
    private String SSID;
	
	@MemberOrder(sequence = "3")
    @javax.jdo.annotations.Column(allowsNull="true")
	public String getBSSId() {
		return BSSId;
	}
	public void setBSSId(String bSSId) {
		BSSId = bSSId;
	}
	
    private Location location;
	
	@javax.jdo.annotations.Column(allowsNull="true")
    @MemberOrder(sequence = "10")
	@javax.jdo.annotations.Persistent
    public Location getLocation() { 
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }
	
    @javax.inject.Inject
    @SuppressWarnings("unused")
    private DomainObjectContainer container;

    @Override
    public int compareTo(Capture capture) {
        return ObjectContracts.compare(this, capture, "mac");
    }
    @javax.jdo.annotations.Column(allowsNull="true")
	public String getReceiverAddress() {
		return receiverAddress;
	}
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	@javax.jdo.annotations.Column(allowsNull="true")
	public String getDestinationAddress() {
		return destinationAddress;
	}
	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}
	@javax.jdo.annotations.Column(allowsNull="true")
	public String getTransmitterAddress() {
		return TransmitterAddress;
	}
	public void setTransmitterAddress(String transmitterAddress) {
		TransmitterAddress = transmitterAddress;
	}
	@javax.jdo.annotations.Column(allowsNull="true")
	public String getSourceAddress() {
		return SourceAddress;
	}
	public void setSourceAddress(String sourceAddress) {
		SourceAddress = sourceAddress;
	}
	@javax.jdo.annotations.Column(allowsNull="true")
	public String getSSID() {
		return SSID;
	}
	public void setSSID(String sSID) {
		SSID = sSID;
	}
    
	
}
