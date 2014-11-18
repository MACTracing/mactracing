package dom.simple;


import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.Bookmarkable;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.value.Blob;


@Named("Capture")
@DomainService(menuOrder = "40", repositoryFor = Capture.class)
public class RepoCapture extends AbstractFactoryAndRepository {
    
	public String getId() {
        return "Capture";
    }
	
	@Bookmarkable
    @ActionSemantics(Of.SAFE)
    @MemberOrder(sequence = "1")
    public List<Capture> listAll() {
        return container.allInstances(Capture.class);
    }

    public String captureUpload(Blob captureMacFile,Blob captureGPSFile) {
        return "Upload sucessfull";
    }
    
    @javax.inject.Inject 
    DomainObjectContainer container;
	
}
