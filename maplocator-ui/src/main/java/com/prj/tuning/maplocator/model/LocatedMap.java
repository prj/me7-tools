package com.prj.tuning.maplocator.model;

public class LocatedMap implements Comparable<LocatedMap> {
  private int address;
  private String desc;
  private String id;
  private String units;
  private int precision;
  private Endianness endianness;
  private double offset;
  private double factor;
  private int length;
  private Integer width;
  private LocatedMap xAxis;
  private LocatedMap yAxis;
  private boolean signed;
  private boolean axis;
  private boolean ecuvar;
  
  public enum Endianness {
    BIGENDIAN, LITTLEENDIAN;
  }

  public int getAddress() {
    return address;
  }

  public void setAddress(int address) {
    this.address = address;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Endianness getEndianness() {
    return endianness;
  }

  public void setEndianness(Endianness endianness) {
    this.endianness = endianness;
  }

  public double getOffset() {
    return offset;
  }

  public void setOffset(double offset) {
    this.offset = offset;
  }

  public double getFactor() {
    return factor;
  }

  public void setFactor(double factor) {
    this.factor = factor;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public LocatedMap getxAxis() {
    return xAxis;
  }

  public void setxAxis(LocatedMap xAxis) {
    this.xAxis = xAxis;
  }

  public LocatedMap getyAxis() {
    return yAxis;
  }

  public void setyAxis(LocatedMap yAxis) {
    this.yAxis = yAxis;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(Integer width) {
    this.width = width;
  }

  public boolean isSigned() {
    return signed;
  }

  public void setSigned(boolean signed) {
    this.signed = signed;
  }

  public boolean isAxis() {
    return axis;
  }

  public void setAxis(boolean axis) {
    this.axis = axis;
  }
  public String getDesc()
  {
	  return desc;
  }
  public void setDesc(String desc)
  {
	this.desc = desc;  
  }
  public String getUnits()
  {
	  return units;
  }
  public void setUnits(String units)
  {
	  this.units = units;
  }
  public int getPrecision()
  {
	  return precision;
  }
  public void setPrecision(int precision)
  {
	  this.precision = precision;
  }
  public Boolean isEcuvar()
  {
	  return ecuvar;
  }
  public void setEcuvar(Boolean ecuvar)
  {
	  this.ecuvar = ecuvar;
  }
  
  /**
   * Compares an external instance of an object to the current instance.
   * @param t The object that is being compared to the current instance.
   * @return Returns 0 if the objects are equal, greater than 0 if the current
   * object is greater, and less than 0 if the current object if less.
   */
  @Override
  public int compareTo(LocatedMap t) {
      return id.compareTo(t.id);
  }
}
