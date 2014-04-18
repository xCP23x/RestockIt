//Copyright (C) 2011-2014 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/RestockIt/blob/master/README and http://github.com/xCP23x/RestockIt/blob/master/LICENSE for details

package org.cp23.restockit;

import java.io.File;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;

@XmlRootElement(name="containerList")
@XmlType(propOrder={"version", "contList"})
@XmlSeeAlso(RCont.class)
public class RXml {
    
    public class RXmlException extends Exception{
        //Thrown if a file version newer than current is loaded
        public RXmlException(String msg){
            super(msg);
        }
    }
    
    //XML serializable variables
    private ArrayList<RCont> contList = new ArrayList<>();
    private static final int version = 1;
    
    //Transient variables
    private transient final String filePath = "plugins/RestockIt/";
    private transient final RestockIt plugin = RestockIt.plugin;
    private transient int fileVersion;
    
    
    public RXml(){
    }
    
    public void addToList(RCont cont){
        contList.add(cont);
    }
    
    public boolean isInXml(RCont cont){
        for(RCont c : contList){
            if(c.getX()==cont.getX() && c.getY()==cont.getY() && c.getZ()==cont.getZ() && c.getWorldUID()==cont.getWorldUID()){
                return true;
            }
        }
        return false;
    }
    
    public void save(String fileName){
        try{
            for(RCont cont: contList){
                //Double check that all XML variables are up to date
                cont.transientToXml();
            }
            
            JAXBContext context = JAXBContext.newInstance(this.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            File file = new File(filePath + fileName);
            marshaller.marshal(this, file);
        } catch(JAXBException e){
            plugin.debug(e.getStackTrace().toString());
            plugin.debug("Couldn't save file "+ fileName); //ADD PROPER MESSAGE
        }
    }
    
    public void load(String fileName) throws RXmlException{
        try{
            JAXBContext context = JAXBContext.newInstance(this.getClass());
            Unmarshaller unmarshaller = context.createUnmarshaller();
            
            File file = new File(filePath + fileName);
            RXml rx = (RXml) unmarshaller.unmarshal(file);
            contList = rx.contList;
            fileVersion = rx.fileVersion;
            if(fileVersion > version){
                throw new RXmlException("XML file version too new: Plugin version " + version + ", file version " +fileVersion);
            }
            
            for(RCont cont: contList){
                //Load XML settings to transient variables
                //This can't be done in constructor, as it's constructed BEFORE XML values are loaded
                cont.xmlToTransient();
            }
        } catch(JAXBException e){
            plugin.debug(e.getStackTrace().toString());
            plugin.debug("Couldn't load file "+ fileName); //ADD PROPER MESSAGE
        }
    }
    
    
    //XML Getters and Setters
    @XmlElement
    public int getVersion(){
        return version;
    }
    public void setVersion(int n){
        fileVersion = n;
    }
    
    @XmlAnyElement(lax=true)
    public ArrayList<RCont> getContList(){
        return contList;
    }
    public void setContList(ArrayList<RCont> cl){
        contList = cl;
    }
}
