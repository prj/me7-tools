package com.prj.tuning.xdf.olsimpl;

import java.util.Arrays;
import java.util.List;

import com.prj.tuning.mappack.map.Axis;
import com.prj.tuning.mappack.map.PMap;
import com.prj.tuning.xdf.binding.XdfAxis;
import com.prj.tuning.xdf.binding.XdfCategoryMem;
import com.prj.tuning.xdf.binding.XdfEmbedded;
import com.prj.tuning.xdf.binding.XdfMath;
import com.prj.tuning.xdf.binding.XdfProject;
import com.prj.tuning.xdf.binding.XdfTable;
import com.prj.tuning.xdf.olsimpl.addressmap.AddressMap;

public class OlsMap extends XdfTable {
  private PMap map;
  private boolean omitCategories;
  private AddressMap sub;

  public OlsMap(PMap map, boolean omitCategories, AddressMap sub) {
    this.map = map;
    this.omitCategories = omitCategories;
    this.sub = sub;
  }

  @Override
  public String getUniqueId() {
    return String.format(XdfProject.ADDRESS_FORMAT, sub.subAddr(map.getAddress()));
  }

  @Override
  public String getTitle() {
    return String.format(XdfProject.ADDRESS_FORMAT, map.getAddress()) + " - " + map.getId() + " - " + map.getName();
  }

  @Override
  public XdfAxis getRowAxis() {
    if (getyAxis().getDataSource().isEprom()) {
      return new OlsAxis(sub) {

        @Override
        public String getDimension() {
          return "y";
        }

        @Override
        public Axis getAxis() {
          return getyAxis();
        }
      };
    }
    else {
      return new XdfAxis() {
        @Override
        public String getDimension() {
          return "y";
        }

        @Override
        public Integer getXdfIndexcount() {
          return getyAxis().getLength();
        }
      };
    }
  }

  @Override
  public XdfAxis getColAxis() {
    if (getxAxis().getDataSource().isEprom()) {
      return new OlsAxis(sub) {

        @Override
        public String getDimension() {
          return "x";
        }

        @Override
        public Axis getAxis() {
          return getxAxis();
        }
      };
    }
    else {
      return new XdfAxis() {
        @Override
        public String getDimension() {
          return "x";
        }

        @Override
        public Integer getXdfIndexcount() {
          return getxAxis().getLength();
        }
      };
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
      public XdfEmbedded getEmbedded() {
        return new XdfEmbedded() {

          @Override
          public int getXdfWidth() {
            return map.getValueType().width * 8;
          }

          @Override
          public String getXdfAddress() {
            return getUniqueId();
          }

          @Override
          public String getTypeFlags() {
            return String.format("0x%X", (map.isSigned() ? 1 : 0) + (map.getValueType().isLsbFirst() ? 2 : 0));
          }
        };
      }

      @Override
      public List<XdfMath> getXdfMath() {
        return Arrays.asList(new XdfMath[] {new OlsMath(map.getValue())});
      }

      @Override
      public Integer getDecimalpl() {
        return map.getPrecision() > 2 ? 2 : map.getPrecision();
      }
    };
  }

  private Axis getyAxis() {
    return shouldFlip() ? map.getxAxis() : map.getyAxis();
  }

  private Axis getxAxis() {
    return shouldFlip() ? map.getyAxis() : map.getxAxis();
  }

  private boolean shouldFlip() {
    return map.getyAxis().getLength() < 2 && map.getxAxis().getLength() > 1;
  }

  @Override
  public XdfCategoryMem getCategory() {
    return omitCategories ? null : new OlsCategoryMem(map.getFolderId());
  }
}
