package com.prj.tuning.xdf.olsimpl;

import com.prj.tuning.mappack.map.Axis;
import com.prj.tuning.xdf.binding.XdfAxis;
import com.prj.tuning.xdf.binding.XdfEmbedInfo;

public abstract class OlsAxis extends XdfAxis {

  @Override
  public XdfEmbedInfo getXdfEmbedInfo() {
    return new XdfEmbedInfo() {

      @Override
      public Integer getType() {
        return 3;
      }

      @Override
      public String getLinkObjId() {
        return String.format(OlsProject.ADDRESS_FORMAT, getAxis().getAddress());
      }
    };
  }

  @Override
  public Integer getXdfIndexcount() {
    return getAxis().getLength();
  }

  public abstract Axis getAxis();

}
