package com.prj.tuning.xdf.olsimpl;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.prj.tuning.mappack.Project;
import com.prj.tuning.mappack.map.PMap;
import com.prj.tuning.xdf.binding.XdfHeader;
import com.prj.tuning.xdf.binding.XdfProject;
import com.prj.tuning.xdf.binding.XdfTable;

public class OlsProject extends XdfProject {
  private Project project;
  private OlsHeader header;

  public OlsProject(Project project) {
    this.project = project;
  }

  @Override
  public final XdfHeader getXdfHeader() {
    if (header == null) this.header = new OlsHeader(project);
    return header;
  }

  @Override
  public Collection<XdfTable> getTables() {
    Set<XdfTable> tables = new TreeSet<XdfTable>(new Comparator<XdfTable>() {
      @Override
      public int compare(XdfTable o1, XdfTable o2) {
        return o1.getTitle().compareToIgnoreCase(o2.getTitle());
      }
    });

    Set<Integer> axes = new HashSet<Integer>();
    Set<Integer> addresses = new HashSet<Integer>();
    for (PMap map : project.getMaps()) {
      addresses.add(map.getAddress());
    }
    
    for (PMap map : project.getMaps()) {
      // For every map check if the axis comes from EPROM, and if it does and there is no map for it in .kp, create it.
      if (map.getxAxis().getDataSource().isEprom() && !axes.contains(map.getxAxis().getAddress()) && !addresses.contains(map.getxAxis().getAddress())) {
        tables.add(new OlsAxisMap(map.getxAxis()));
        axes.add(map.getxAxis().getAddress());
      }

      if (map.getyAxis().getDataSource().isEprom() && !axes.contains(map.getyAxis().getAddress()) && !addresses.contains(map.getxAxis().getAddress())) {
        tables.add(new OlsAxisMap(map.getyAxis()));
        axes.add(map.getyAxis().getAddress());
      }

      tables.add(new OlsMap(map, project.getFolders().size() > 254));
    }
    return tables;
  }

}
