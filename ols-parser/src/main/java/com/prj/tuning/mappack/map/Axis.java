package com.prj.tuning.mappack.map;

import java.nio.ByteBuffer;
import java.util.HashMap;

import com.prj.tuning.mappack.map.PMap.ValueType;
import com.prj.tuning.mappack.util.BinaryUtil;

public class Axis {
  private int length;
  private Value value;
  private DataSource dataSource;
  private int address;
  private ValueType valueType;
  private int signature;
  private boolean signed;

  private Axis() {
  };

  public static Axis fromBuffer(ByteBuffer b, int len) {
    Axis axis = new Axis();
    axis.value = Value.fromBuffer(b);
    axis.dataSource = DataSource.get(b.getInt());
    axis.address = b.getInt();
    axis.valueType = ValueType.get(b.getInt());
    BinaryUtil.skip(b, 14);
    axis.signed = b.get() == 1;
    BinaryUtil.skip(b, b.getInt() + 4);
    axis.signature = b.getInt();
    axis.length = len;
    return axis;
  }

  public int getRawLength() {
    return length * valueType.width;
  }

  public enum DataSource {
    ONE_TWO_THREE, EPROM, EPROM_ADD, EPROM_SUBTRACT, EPROM_BACKWARDS, FREE_EDITABLE;

    private static final java.util.Map<Integer, DataSource> lookup = new HashMap<Integer, DataSource>();

    static {
      lookup.put(0, ONE_TWO_THREE);
      lookup.put(1, EPROM);
      lookup.put(2, EPROM_ADD);
      lookup.put(3, EPROM_SUBTRACT);
      lookup.put(4, EPROM_BACKWARDS);
      lookup.put(5, FREE_EDITABLE);
    }

    public static DataSource get(int value) {
      return lookup.get(value);
    }

    public boolean isEprom() {
      return this == EPROM || this == EPROM_ADD || this == EPROM_SUBTRACT || this == EPROM_BACKWARDS;
    }
  }

  public int getLength() {
    return length;
  }

  public Value getValue() {
    return value;
  }

  public DataSource getDataSource() {
    return dataSource;
  }

  public int getAddress() {
    return address;
  }

  public ValueType getValueType() {
    return valueType;
  }

  public int getSignature() {
    return signature;
  }

  public boolean isSigned() {
    return signed;
  }

}
