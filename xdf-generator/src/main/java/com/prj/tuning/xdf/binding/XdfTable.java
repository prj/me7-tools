package com.prj.tuning.xdf.binding;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "XDFTABLE")
public abstract class XdfTable {
  @XmlAttribute(name = "uniqueid")
  public abstract String getUniqueId();

  @XmlElement(name = "title")
  public abstract String getTitle();

  @XmlElementRef
  public abstract XdfAxis getRowAxis();

  @XmlElementRef
  public abstract XdfAxis getColAxis();

  @XmlElementRef
  public abstract XdfAxis getValueAxis();
}
