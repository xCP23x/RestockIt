//Copyright (C) 2011-2014 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/RestockIt/blob/master/README and http://github.com/xCP23x/RestockIt/blob/master/LICENSE for details

package org.cp23.restockit;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

@XmlRootElement
public class RXml {
    private final List<RCont> contList = new ArrayList<>();
    private final RestockIt plugin = RestockIt.plugin;
    
    public RXml(){
        //Load stuff
    }
    
    public boolean isRCont(RCont cont){
        return false;
    }
    
    @XmlAnyElement(lax=true)
    public List<RCont> getContList(){
        return contList;
    }
}
