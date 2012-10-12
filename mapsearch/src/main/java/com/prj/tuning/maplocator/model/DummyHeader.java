package com.prj.tuning.maplocator.model;

import com.prj.tuning.mappack.Header;

public class DummyHeader extends Header {
  private String name;

  public DummyHeader(String name) {
    super();
    this.name = name;
  }

  @Override
  public String getFileName() {
    return name;
  }

  @Override
  public String getName() {
    return name;
  }
}
