package com.prj.tuning.xdf.olsimpl;

import com.prj.tuning.mappack.map.Axis;
import com.prj.tuning.xdf.binding.XdfAxis;
import com.prj.tuning.xdf.binding.XdfEmbedInfo;
import com.prj.tuning.xdf.olsimpl.addressmap.AddressMap;

public abstract class OlsAxis extends XdfAxis {
  private AddressMap sub;
  
  public OlsAxis(AddressMap sub) {
    this.sub = sub;
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
        return String.format(OlsProject.ADDRESS_FORMAT, sub.subAddr(getAxis().getAddress()));
      }
    };
  }

  @Override
  public Integer getXdfIndexcount() {
    return getAxis().getLength();
  }

  public abstract Axis getAxis();

}
