package com.prj.tuning.xdf.binding;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "REGION")
public abstract class XdfRegion {
  @XmlAttribute
  public abstract String getSize();
}
