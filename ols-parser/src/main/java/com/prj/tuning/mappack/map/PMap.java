package com.prj.tuning.mappack.map;

import java.nio.ByteBuffer;
import java.util.HashMap;

import com.prj.tuning.mappack.util.BinaryUtil;

public class PMap {
  protected String id;
  protected String name;
  protected int address;
  protected Organization organization;
  protected ValueType valueType;
  protected Value value;
  protected Axis xAxis;
  protected Axis yAxis;
  protected int folderId;
  protected boolean signed;
  protected int precision;

  public PMap() {
  }

  public int getRawLength() {
    return xAxis.getLength() * yAxis.getLength() * valueType.width;
  }

  public enum Organization {
    SINGLE_VALUE, ONEDIMENSIONAL, TWODIMENSIONAL, TWO_D_INVERSE;

    private static final java.util.Map<Integer, Organization> lookup = new HashMap<Integer, PMap.Organization>();

    static {
      lookup.put(2, SINGLE_VALUE);
      lookup.put(3, ONEDIMENSIONAL);
      lookup.put(4, TWODIMENSIONAL);
      lookup.put(5, TWO_D_INVERSE);
    }

    public static Organization get(int value) {
      return lookup.get(value);
    }
  }

  public enum ValueType {
    _8(1), _16_BIG(2), _16_LITTLE(2), _32_BIG(4), _32_LITTLE(4), _32_FLOAT_BIG(4), _32_FLOAT_LITTLE(4);

    private static final java.util.Map<Integer, ValueType> lookup = new HashMap<Integer, PMap.ValueType>();
    public final int width;

    private ValueType(int width) {
      this.width = width;
    }

    static {
      lookup.put(1, _8);
      lookup.put(2, _16_BIG);
      lookup.put(3, _16_LITTLE);
      lookup.put(4, _32_BIG);
      lookup.put(5, _32_LITTLE);
      lookup.put(6, _32_FLOAT_BIG);
      lookup.put(7, _32_FLOAT_LITTLE);
    }

    public static ValueType get(int value) {
      return lookup.get(value);
    }
    
    public boolean isLsbFirst() {
      return this == _16_LITTLE || this == _32_LITTLE || this == _32_FLOAT_LITTLE;
    }
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getAddress() {
    return address;
  }

  public Organization getOrganization() {
    return organization;
  }

  public ValueType getValueType() {
    return valueType;
  }

  public Value getValue() {
    return value;
  }

  public Axis getxAxis() {
    return xAxis;
  }

  public Axis getyAxis() {
    return yAxis;
  }

  public int getFolderId() {
    return folderId;
  }

  public boolean isSigned() {
    return signed;
  }

  public int getPrecision() {
    return precision;
  }
  
  public PMap parse(ByteBuffer b) {
    BinaryUtil.skip(b, 1);
    name = BinaryUtil.readString(b);
    organization = Organization.get(b.getInt());
    BinaryUtil.skip(b, 4);
    valueType = ValueType.get(b.getInt());
    BinaryUtil.skip(b, 8);
    folderId = b.getInt();
    id = BinaryUtil.readString(b);
    BinaryUtil.skip(b, 54);
    signed = b.get() == 1;
    BinaryUtil.skip(b, 2);
    int xLen = b.getInt();
    int yLen = b.getInt();
    BinaryUtil.skip(b, 8);
    precision = b.getInt();
    value = Value.fromBuffer(b);
    address = b.getInt();
    BinaryUtil.skip(b, 28);
    xAxis = new Axis(xLen).parse(b);
    yAxis = new Axis(yLen).parse(b);
    BinaryUtil.skip(b, 80);
    
    return this;
  }

}
