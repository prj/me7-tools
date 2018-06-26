package com.prj.tuning.maplocator.model;

public class LocatedMap {
  private int address;
  private String id;
  private String title;
  private Endianness endianness;
  private double offset;
  private double factor;
  private int length;
  private Integer width;
  private LocatedMap xAxis;
  private LocatedMap yAxis;
  private boolean signed;
  private boolean axis;
  
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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
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
}
