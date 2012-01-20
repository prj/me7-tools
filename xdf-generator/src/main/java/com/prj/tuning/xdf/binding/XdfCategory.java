package com.prj.tuning.xdf.binding;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.prj.tuning.xdf.olsimpl.OlsProject;

@XmlRootElement(name = "CATEGORY")
public abstract class XdfCategory {
  
  @XmlAttribute(name = "index")
  public String getHexIndex() {
    return String.format(OlsProject.ADDRESS_FORMAT, getIndex());
  }
  
  @XmlTransient
  public abstract int getIndex();
  
  @XmlAttribute(name = "name")
  public abstract String getName();
}
