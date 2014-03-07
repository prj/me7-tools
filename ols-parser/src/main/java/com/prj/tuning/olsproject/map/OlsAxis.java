package com.prj.tuning.olsproject.map;

import java.nio.ByteBuffer;

import com.prj.tuning.mappack.map.Axis;
import com.prj.tuning.mappack.map.PMap.ValueType;
import com.prj.tuning.mappack.map.Value;
import com.prj.tuning.mappack.util.BinaryUtil;

public class OlsAxis extends Axis {

  public OlsAxis(int length) {
    super(length);
  }

  @Override
  public Axis parse(ByteBuffer b) {
    value = Value.fromBuffer(b);
    dataSource = DataSource.get(b.getInt());
    address = b.getInt();
    valueType = ValueType.get(b.getInt());
    /*System.out.println(b.position());
    BinaryUtil.skip(b, 10);
    precision = b.get();
    BinaryUtil.skip(b, 3);
    signed = b.get() == 1;
    System.out.println(b.position());
    BinaryUtil.skip(b, b.getInt() + 4);
    signature = b.getInt();*/
    BinaryUtil.skip(b, 39);
    return this;
  }

}
