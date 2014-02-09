package com.prj.tuning.maplocator.export.xdf.model;

import com.prj.tuning.xdf.binding.XdfAxis;

public class DummyAxis extends XdfAxis {
  private String dimension;
  private int length;
  
  public DummyAxis(String dimension, int length) {
    this.dimension = dimension;
    this.length = length;
  }

  @Override
  public String getDimension() {
    return dimension;
  }

  @Override
  public Integer getXdfIndexcount() {
    return length;
  }
}
