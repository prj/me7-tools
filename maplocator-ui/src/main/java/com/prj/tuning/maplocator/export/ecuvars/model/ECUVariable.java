package com.prj.tuning.maplocator.export.ecuvars.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ECUVariable")
public class ECUVariable implements Comparable<ECUVariable> {
	@XmlElement(name = "name")
    public String name;
	@XmlElement(name = "desc")
    public String desc;
	@XmlElement(name = "address")
    public String address;
	@XmlElement(name = "units")
    public String units;
	@XmlElement(name = "word")
    public boolean word;
	@XmlElement(name = "signed")
    public boolean signed;
	@XmlElement(name = "factor")
    public double factor;
	@XmlElement(name = "offset")
    public double offset;
	@XmlElement(name = "precision")
    public int precision;

	public ECUVariable()
	{
	
	}
	
    public ECUVariable(String name, String desc, String address, String units, Boolean word, Boolean signed, double factor, double offset, int precision)
    {
        this.name = name;
        this.desc = desc;
        this.address = address;
        this.units = units;
        this.word = word;
        this.signed = signed;
        this.factor = factor;
        this.offset = offset;
        this.precision = precision;
    }
    
    /**
     * Compares an external instance of an object to the current instance.
     * @param t The object that is being compared to the current instance.
     * @return Returns 0 if the objects are equal, greater than 0 if the current
     * object is greater, and less than 0 if the current object if less.
     */
    @Override
    public int compareTo(ECUVariable t) {
        return name.compareTo(t.name);
    }
    
}
