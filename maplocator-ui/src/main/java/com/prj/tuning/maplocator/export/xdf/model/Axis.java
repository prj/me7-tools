package com.prj.tuning.maplocator.export.xdf.model;

import com.prj.tuning.maplocator.model.LocatedMap;
import com.prj.tuning.xdf.binding.XdfAxis;
import com.prj.tuning.xdf.binding.XdfEmbedInfo;
import com.prj.tuning.xdf.olsimpl.OlsProject;

public class Axis extends XdfAxis {
  private String dimension;
  private LocatedMap locatedMap;

  public Axis(String dimension, LocatedMap locatedMap) {
    this.locatedMap = locatedMap;
    this.dimension = dimension;
  }
  
  @Override
  public String getDimension() {
    return dimension;
  }

  @Override
  public XdfEmbedInfo getXdfEmbedInfo() {
    return new XdfEmbedInfo() {
      @Override
      public Integer getType() {
        return 3;
      }
      
      @Override
      public String getLinkObjId() {
        return String.format(OlsProject.ADDRESS_FORMAT, locatedMap.getAddress());
      }
    };
  }

  @Override
  public Integer getXdfIndexcount() {
    return locatedMap.getLength();
  }
  
}
