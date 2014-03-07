package com.prj.tuning.maplocator.export.ecuvars.model;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="ECUVariables")
public abstract class ECUVariables {
	@XmlElementRef
	public abstract Collection<ECUVariable> getEcuvars();
}
