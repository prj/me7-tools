package com.prj.tuning.mappack.map;

import java.nio.ByteBuffer;
import java.util.HashMap;

import com.prj.tuning.mappack.util.BinaryUtil;

public class PMap {
  private String id;
  private String name;
  private int address;
  private Organization organization;
  private ValueType valueType;
  private Value value;
  private Axis xAxis;
  private Axis yAxis;

  private PMap() {
  }

  public static PMap fromBuffer(ByteBuffer b) {
    PMap map = new PMap();

    BinaryUtil.skip(b, 1);
    map.name = BinaryUtil.readString(b);
    map.organization = Organization.get(b.getInt());
    BinaryUtil.skip(b, 4);
    map.valueType = ValueType.get(b.getInt());
    BinaryUtil.skip(b, 12);
    map.id = BinaryUtil.readString(b);
    BinaryUtil.skip(b, 57);
    int xLen = b.getInt();
    int yLen = b.getInt();
    BinaryUtil.skip(b, 12);
    map.value = Value.fromBuffer(b);
    map.address = b.getInt();
    BinaryUtil.skip(b, 28);
    map.xAxis = Axis.fromBuffer(b, xLen);
    map.yAxis = Axis.fromBuffer(b, yLen);
    BinaryUtil.skip(b, 80);

    return map;
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

}
