package com.prj.tuning.xdf.binding;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "LABEL")
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL) 
public abstract class XdfLabel {
	@XmlAttribute(name = "index")
	public abstract int getIndex();

	@XmlAttribute(name = "value")
	public abstract String getValue();
}
