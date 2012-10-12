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
import com.prj.tuning.xdf.olsimpl.addressmap.AddressMap;
import com.prj.tuning.xdf.olsimpl.addressmap.DummyAddressMap;

public class OlsProject extends XdfProject {
  private Project project;
  private OlsHeader header;
  private AddressMap sub;

  public OlsProject(Project project) {
    this(project, new DummyAddressMap());
  }
  
  public OlsProject(Project project, AddressMap sub) {
    this.project = project;
    this.sub = sub;
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
      addresses.add(sub.subAddr(map.getAddress()));
    }
    
    for (PMap map : project.getMaps()) {
      // For every map check if the axis comes from EPROM, and if it does and there is no map for it in .kp, create it.
      if (map.getxAxis().getDataSource().isEprom() && !axes.contains(sub.subAddr(map.getxAxis().getAddress())) && !addresses.contains(sub.subAddr(map.getxAxis().getAddress()))) {
        tables.add(new OlsAxisMap(map.getxAxis(), sub));
        axes.add(sub.subAddr(map.getxAxis().getAddress()));
      }

      if (map.getyAxis().getDataSource().isEprom() && !axes.contains(sub.subAddr(map.getyAxis().getAddress())) && !addresses.contains(sub.subAddr(map.getyAxis().getAddress()))) {
        tables.add(new OlsAxisMap(map.getyAxis(), sub));
        axes.add(sub.subAddr(map.getyAxis().getAddress()));
      }

      tables.add(new OlsMap(map, project.getFolders().size() > 254, sub));
    }
    return tables;
  }

}
