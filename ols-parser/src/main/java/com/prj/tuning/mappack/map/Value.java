package com.prj.tuning.mappack.map;

import java.nio.ByteBuffer;

import com.prj.tuning.mappack.util.BinaryUtil;

public class Value {
  private String description;
  private String units;
  private double factor;
  private double offset;

  private Value() {
  };

  public static Value fromBuffer(ByteBuffer b) {
    Value value = new Value();

    value.description = BinaryUtil.readString(b);
    value.units = BinaryUtil.readString(b);
    value.factor = b.getDouble();
    value.offset = b.getDouble();

    return value;
  }

  public String getDescription() {
    return description;
  }

  public String getUnits() {
    return units;
  }

  public double getFactor() {
    return factor;
  }

  public double getOffset() {
    return offset;
  }
}
