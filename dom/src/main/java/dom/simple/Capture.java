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
@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER, 
        column="version")
@ObjectType("Captura")
@Bookmarkable
public class Capture implements Comparable<Capture> , Locatable  {

	//private Date datetime;
	private String mac;
	private String BSSId;
    
	
	/*@javax.jdo.annotations.Column(allowsNull="false")
	@MemberOrder(sequence = "1")
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}*/
	@Title
	@javax.jdo.annotations.Column(allowsNull="true")
	@MemberOrder(sequence = "2")
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	@MemberOrder(sequence = "3")
    @javax.jdo.annotations.Column(allowsNull="true")
	public String getBSSId() {
		return BSSId;
	}
	public void setBSSId(String bSSId) {
		BSSId = bSSId;
	}
	@javax.jdo.annotations.Persistent
    private Location location;
	
	@javax.jdo.annotations.Column(allowsNull="true")
    @MemberOrder(sequence = "10")
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
	
}
