package com.prj.tuning.maplocator.export.xdf.model;

import java.util.Arrays;
import java.util.List;

import com.prj.tuning.maplocator.model.LocatedMap;
import com.prj.tuning.maplocator.model.LocatedMap.Endianness;
import com.prj.tuning.xdf.binding.XdfAxis;
import com.prj.tuning.xdf.binding.XdfCategoryMem;
import com.prj.tuning.xdf.binding.XdfEmbedded;
import com.prj.tuning.xdf.binding.XdfMath;
import com.prj.tuning.xdf.binding.XdfTable;
import com.prj.tuning.xdf.olsimpl.OlsAxisMap;
import com.prj.tuning.xdf.olsimpl.OlsProject;

public class Table extends XdfTable {
  private LocatedMap locatedMap;
  
  public Table(LocatedMap locatedMap) {
    this.locatedMap = locatedMap;
  }

  @Override
  public String getUniqueId() {
    return String.format(OlsProject.ADDRESS_FORMAT, locatedMap.getAddress());
  }

  @Override
  public String getTitle() {
    return locatedMap.getId();
  }

  @Override
  public XdfAxis getRowAxis() {
    if (locatedMap.getxAxis() == null) {
      return new DummyAxis("y", locatedMap.getLength() == 0 ? 1 : locatedMap.getLength());
    } else {
      return new Axis("y", locatedMap.getyAxis());
    }
  }

  @Override
  public XdfAxis getColAxis() {
    if (locatedMap.getxAxis() == null) {
      return new DummyAxis("x", 1);
    } else {
      return new Axis("x", locatedMap.getxAxis());
    }
  }

  @Override
  public XdfAxis getValueAxis() {
    return new XdfAxis() {
      
      @Override
      public String getDimension() {
        return "z";
      }

      @Override
      public Integer getDecimalpl() {
        return 2;
      }

      @Override
      public XdfEmbedded getEmbedded() {
        return new XdfEmbedded() {
          
          @Override
          public int getXdfWidth() {
            return locatedMap.getWidth()*8;
          }
          
          @Override
          public String getXdfAddress() {
            return getUniqueId();
          }
          
          @Override
          public String getTypeFlags() {
            return String.format("0x%X", (locatedMap.isSigned() ? 1 : 0) + (locatedMap.getEndianness() == Endianness.BIGENDIAN ? 2 : 0));
          }
        };
      }

      @Override
      public List<XdfMath> getXdfMath() {
        return Arrays.asList(new XdfMath[] {new TableMath(locatedMap.getFactor(), locatedMap.getOffset())});
      }
    };
  }

  @Override
  public XdfCategoryMem getCategory() {
    if (locatedMap.isAxis()) {
      return new XdfCategoryMem() {
        
        @Override
        public int getIndex() {
          return 0;
        }
        
        @Override
        public int getCategory() {
          return OlsAxisMap.AXIS_CATEGORY;
        }
      };
    }
    return null;
  }

}
