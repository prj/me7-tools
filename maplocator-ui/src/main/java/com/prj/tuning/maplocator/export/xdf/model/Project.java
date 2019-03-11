package com.prj.tuning.maplocator.export.xdf.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.List;
import java.util.Comparator;

import com.prj.tuning.maplocator.model.LocatedMap;
import com.prj.tuning.xdf.binding.XdfCategory;
import com.prj.tuning.xdf.binding.XdfHeader;
import com.prj.tuning.xdf.binding.XdfProject;
import com.prj.tuning.xdf.binding.XdfRegion;
import com.prj.tuning.xdf.binding.XdfTable;
import com.prj.tuning.xdf.binding.XdfConstant;
import com.prj.tuning.xdf.olsimpl.OlsAxisMap;
import com.prj.tuning.xdf.olsimpl.OlsProject;

public class Project extends XdfProject {
  
  private Collection<XdfTable> tables = new TreeSet<XdfTable>(new Comparator<XdfTable>() {
    public int compare(XdfTable obj1, XdfTable obj2) {
        return obj1.getTitle().compareTo(obj2.getTitle());
     }
  });
  private Collection<XdfConstant> constants = new HashSet<XdfConstant>();
  private int size;
  
  public Project(Collection<LocatedMap> locatedMaps, int size) {
    for (LocatedMap locatedMap : locatedMaps) {
      tables.add(new Table(locatedMap));
    }
    this.size = size;
  }

  @Override
  public XdfHeader getXdfHeader() {
    return new XdfHeader() {
      
      @Override
      public String getDeftitle() {
        return "Maplocator UI auto-generated XDF";
      }
      
      @Override
      public Collection<XdfCategory> getCategories() {
        List<XdfCategory> categories = new ArrayList<XdfCategory>();
        categories.add(new XdfCategory() {
          
          @Override
          public String getName() {
            return "Axes";
          }
          
          @Override
          public int getIndex() {
            return OlsAxisMap.AXIS_CATEGORY;
          }
        });
        return categories;
      }

      @Override
      public XdfRegion getRegion() {
        return new XdfRegion() {
          
          @Override
          public String getSize() {
            return String.format(OlsProject.ADDRESS_FORMAT, size);
          }
        };
      }
    };
  }

  @Override
  public Collection<XdfTable> getTables() {
    return tables;
  }

  @Override
  public Collection<XdfConstant> getConstants() {
    return constants;
  }
}
