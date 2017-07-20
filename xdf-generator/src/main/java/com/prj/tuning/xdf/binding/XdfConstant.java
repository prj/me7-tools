package com.prj.tuning.xdf.binding;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "XDFCONSTANT")
public abstract class XdfConstant {
  @XmlAttribute(name = "uniqueid")
  public abstract String getUniqueId();
  
  @XmlAttribute(name = "flags")
  public String getFlags() {
	  return null;
  }

  @XmlElement(name = "title")
  public abstract String getTitle();
  
  @XmlElement(name = "description")
  public String getDescription() {
	  return null;
  }
		  
  @XmlElement(name = "units")
  public String getUnits() {
	  return null;
  }
  
  @XmlElement(name = "rangehigh")
  public String getRangeHigh() {
	  return null;
  }
  
  @XmlElement(name = "rangelow")
  public String getRangeLow() {
	  return null;
  }

  @XmlElementRef
  public List<XdfMath> getXdfMath() {
    return null;
  }
  
  @XmlElementRef
  public XdfEmbedded getEmbedded() {
    return null;
  }
  
  @XmlElementRef
  public XdfCategoryMem getCategory() {
	  return null;
  }
}
