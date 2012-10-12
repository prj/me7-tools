package com.prj.tuning.olsproject;

import com.prj.tuning.mappack.Header;

public class OlsHeader extends Header {
  private int mapCount;
  
  public OlsHeader(int mapCount) {
    this.mapCount = mapCount;
  }

  @Override
  public String getName() {
    return "not supported";
  }

  @Override
  public String getFileName() {
    return "not supported";
  }

  @Override
  public String getProgram() {
    return "not supported";
  }

  @Override
  public int getMapCount() {
    return mapCount;
  }

  
}
