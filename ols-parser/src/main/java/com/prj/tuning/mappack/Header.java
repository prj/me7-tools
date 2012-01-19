package com.prj.tuning.mappack;

import java.nio.ByteBuffer;

import com.prj.tuning.mappack.util.BinaryUtil;

public class Header {
  private String name;
  private String fileName;
  private String program;
  private int mapCount;

  private Header() {
  }

  public static Header fromBuffer(ByteBuffer b) {
    Header header = new Header();
    header.name = BinaryUtil.readString(b);
    BinaryUtil.skip(b, 76);
    header.fileName = BinaryUtil.readString(b);
    header.program = BinaryUtil.readString(b);
    BinaryUtil.skip(b, 461);
    header.mapCount = b.getInt();
    return header;
  }

  public String getName() {
    return name;
  }

  public String getFileName() {
    return fileName;
  }

  public String getProgram() {
    return program;
  }

  public int getMapCount() {
    return mapCount;
  }

}
