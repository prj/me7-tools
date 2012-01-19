package com.prj.tuning.xdf.binding;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "XDFHEADER")
public abstract class XdfHeader {

  @XmlElement(name = "deftitle")
  public abstract String getDeftitle();

}
