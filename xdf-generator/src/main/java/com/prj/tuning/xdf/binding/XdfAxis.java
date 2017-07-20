package com.prj.tuning.xdf.binding;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "XDFAXIS")
@XmlType(propOrder = {"dimension", "decimalpl", "xdfIndexcount", "embedded", "xdfMath", "xdfEmbedInfo", "labels"})
public abstract class XdfAxis {

  @XmlAttribute(name = "id")
  public abstract String getDimension();
  
  @XmlElement(name = "decimalpl")
  public Integer getDecimalpl() {
    return null;
  }

  @XmlElement(name = "indexcount")
  public Integer getXdfIndexcount() {
    return null;
  }

  @XmlElementRef
  public XdfEmbedded getEmbedded() {
    return null;
  }

  @XmlElementRef
  public List<XdfMath> getXdfMath() {
    return null;
  }

  @XmlElementRef
  public XdfEmbedInfo getXdfEmbedInfo() {
    return null;
  }
  
  @XmlElementRef
  public List<XdfLabel> getLabels() {
	  return null;
  }
}
