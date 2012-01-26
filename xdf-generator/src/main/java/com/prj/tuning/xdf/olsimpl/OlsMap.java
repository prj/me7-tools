package com.prj.tuning.xdf.olsimpl;

import com.prj.tuning.mappack.map.Axis;
import com.prj.tuning.mappack.map.PMap;
import com.prj.tuning.xdf.binding.XdfAxis;
import com.prj.tuning.xdf.binding.XdfCategoryMem;
import com.prj.tuning.xdf.binding.XdfEmbedded;
import com.prj.tuning.xdf.binding.XdfMath;
import com.prj.tuning.xdf.binding.XdfProject;
import com.prj.tuning.xdf.binding.XdfTable;

public class OlsMap extends XdfTable {
  private PMap map;
  private boolean omitCategories;

  public OlsMap(PMap map, boolean omitCategories) {
    this.map = map;
    this.omitCategories = omitCategories;
  }

  @Override
  public String getUniqueId() {
    return String.format(XdfProject.ADDRESS_FORMAT, map.getAddress());
  }

  @Override
  public String getTitle() {
    return map.getId() + " - " + map.getName();
  }

  @Override
  public XdfAxis getRowAxis() {
    if (map.getyAxis().getDataSource().isEprom()) {
      return new OlsAxis() {

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
    if (map.getyAxis().getDataSource().isEprom()) {
      return new OlsAxis() {

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
      public XdfMath getXdfMath() {
        return new OlsMath(map.getValue());
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
    return map.getxAxis().getLength() < 2;
  }

  @Override
  public XdfCategoryMem getCategory() {
    return omitCategories ? null : new OlsCategoryMem(map.getFolderId());
  }
}
