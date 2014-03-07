package com.prj.tuning.xdf.binding;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MATH")
public abstract class XdfMath {

  @XmlAttribute(name = "equation")
  public abstract String getXdfEquation();
  
  @XmlAttribute(name = "row")
  public String getXdfRow() {
    return null;
  }

  @XmlElementRef
  public Collection<XdfMathVar> getXdfMathVars() {
    return null;
  }
}
