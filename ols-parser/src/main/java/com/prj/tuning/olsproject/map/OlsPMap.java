package com.prj.tuning.olsproject.map;

import java.nio.ByteBuffer;

import com.prj.tuning.mappack.map.PMap;
import com.prj.tuning.mappack.map.Value;
import com.prj.tuning.mappack.util.BinaryUtil;

public class OlsPMap extends PMap {

  @Override
  public PMap parse(ByteBuffer b) {
    BinaryUtil.skip(b, 1);
    name = BinaryUtil.readString(b);
    organization = Organization.get(b.getInt());
    BinaryUtil.skip(b, 4);
    valueType = ValueType.get(b.getInt());
    BinaryUtil.skip(b, 8);
    folderId = b.getInt();
    id = BinaryUtil.readString(b);
    BinaryUtil.skip(b, 58);
    signed = b.get() == 1;
    BinaryUtil.skip(b, 2);
    int xLen = b.getInt();
    int yLen = b.getInt();
    BinaryUtil.skip(b, 8);
    precision = b.getInt();
    value = Value.fromBuffer(b);
    address = b.getInt();
    BinaryUtil.skip(b, 36);
    xAxis = new OlsAxis(xLen).parse(b);
    yAxis = new OlsAxis(yLen).parse(b);
    BinaryUtil.skip(b, 89);
    
    return this;
  }
}
