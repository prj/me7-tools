package com.prj.tuning.xdf.binding;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "embedinfo")
public abstract class XdfEmbedInfo {

  @XmlAttribute(name = "type")
  public abstract Integer getType();

  @XmlAttribute(name = "linkobjid")
  public abstract String getLinkObjId();
}
