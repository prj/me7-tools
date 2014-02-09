package com.prj.tuning.xdf.binding;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "EMBEDDEDDATA")
public abstract class XdfEmbedded {

  @XmlAttribute(name = "mmedaddress")
  public abstract String getXdfAddress();

  @XmlAttribute(name = "mmedelementsizebits")
  public abstract int getXdfWidth();
  
  @XmlAttribute(name = "mmedtypeflags")
  public abstract String getTypeFlags();

}