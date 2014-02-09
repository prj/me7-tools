package com.prj.tuning.xdf.olsimpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.prj.tuning.mappack.Folder;
import com.prj.tuning.mappack.Project;
import com.prj.tuning.xdf.binding.XdfCategory;
import com.prj.tuning.xdf.binding.XdfHeader;

public class OlsHeader extends XdfHeader {
  private Project project;

  public OlsHeader(Project project) {
    this.project = project;
  }

  @Override
  public String getDeftitle() {
    return project.getHeader().getFileName();
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
    
    // TunerPro only supports labels for the first 100 categories and a max. of 255 categories...
    if (project.getFolders().size() < 255) {
      List<Folder> folders = new ArrayList<Folder>(project.getFolders());
      Collections.sort(folders, new Comparator<Folder>() {
        @Override
        public int compare(Folder o1, Folder o2) {
          return o1.getName().compareToIgnoreCase(o2.getName());
        }
      });
      
      for (final Folder folder : folders) {
        categories.add(new XdfCategory() {
          
          @Override
          public String getName() {
            return folder.getName();
          }
          
          @Override
          public int getIndex() {
            return folder.getId();
          }
        });
      }
    } else {
      System.err.println("WARNING: Project contains more than 254 categories, omitting.");
    }
      
    return categories;
  }
}
