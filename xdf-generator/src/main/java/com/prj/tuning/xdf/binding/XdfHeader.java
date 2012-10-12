package com.prj.tuning.xdf.binding;

import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "XDFHEADER")
public abstract class XdfHeader {

  @XmlElement(name = "deftitle")
  public abstract String getDeftitle();
  
  @XmlElementRef
  public XdfRegion getRegion() {
    return null;
  }
  
  @XmlElementRef
  public abstract Collection<XdfCategory> getCategories();

}
