package com.prj.tuning.xdf.binding;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CATEGORYMEM")
public abstract class XdfCategoryMem {
  
  @XmlAttribute(name = "index")
  public abstract int getIndex();
  
  @XmlAttribute(name = "category")
  public abstract int getCategory();
}
