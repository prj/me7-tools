package com.prj.tuning.maplocator.locator;

import java.util.Collection;
import java.util.Properties;

import com.prj.tuning.maplocator.model.LocatedMap;
import com.prj.tuning.mappack.Project;

public abstract class AbstractLocator implements Locator {
  
  protected Properties props;
  protected Project p;
  protected byte[] source;
  protected Collection<LocatedMap> locatedMaps;

  public void construct(Properties props, Project p, byte[] source, Collection<LocatedMap> locatedMaps) {
    this.props = props;
    this.p = p;
    this.source = source;
    this.locatedMaps = locatedMaps;
  }
  
  protected void status() {
    System.err.print("\r" + locatedMaps.size() + "/" + p.getMaps().size());
  }

}
