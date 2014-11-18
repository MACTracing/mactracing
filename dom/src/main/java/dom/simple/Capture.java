package dom.simple;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.util.ObjectContracts;
import org.apache.isis.applib.value.Date;
@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER, 
        column="version")
@ObjectType("Captura")
@Bookmarkable
public class Capture implements Comparable<Capture> {

	private Date datetime;
	private String mac;
	private String BSSId;
    
	
	@javax.jdo.annotations.Column(allowsNull="false")
    private Date getDatetime() {
		return datetime;
	}
	private void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	@javax.jdo.annotations.Column(allowsNull="true")
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
    @javax.jdo.annotations.Column(allowsNull="true")
	public String getBSSId() {
		return BSSId;
	}
	public void setBSSId(String bSSId) {
		BSSId = bSSId;
	}
	
    @javax.inject.Inject
    @SuppressWarnings("unused")
    private DomainObjectContainer container;

    @Override
    public int compareTo(Capture capture) {
        return ObjectContracts.compare(this, capture, "fechaHora");
    }
	
}
