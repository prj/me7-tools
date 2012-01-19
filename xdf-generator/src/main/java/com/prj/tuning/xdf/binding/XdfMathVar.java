package com.prj.tuning.xdf.binding;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "VAR")
public abstract class XdfMathVar {
  @XmlElement(name = "id")
  public abstract String getId();
}
