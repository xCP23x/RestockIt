//Copyright (C) 2011-2014 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/RestockIt/blob/master/README and http://github.com/xCP23x/RestockIt/blob/master/LICENSE for details

package org.cp23.restockit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlSeeAlso({RCont.class, RItemStack.class})
public class RXml {
    //contList is a list of ALL registered containers
    private final List<RCont> contList = new ArrayList<>();
    private final RestockIt plugin = RestockIt.plugin;
    private static final int version = 1;
    
    public RXml(){
        //Load stuff
    }
    
    public void addToList(RCont cont){
        //Test code
        contList.add(cont);
    }
    
    public boolean isRCont(RCont cont){
        return false;
    }
    
    public void save(){
        try{
            JAXBContext context = JAXBContext.newInstance(this.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            File file = new File("plugins/RestockIt/containers.xml");
            marshaller.marshal(this, file);
        } catch(JAXBException e){
            e.printStackTrace();
        }
    }
    
    
    @XmlElement
    public int getVersion(){
        return version;
    }
    
    @XmlAnyElement(lax=true)
    public List<RCont> getContList(){
        return contList;
    }
}
