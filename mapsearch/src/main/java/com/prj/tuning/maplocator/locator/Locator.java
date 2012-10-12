package com.prj.tuning.maplocator.locator;

import java.util.Collection;
import java.util.Properties;

import com.prj.tuning.maplocator.model.LocatedMap;
import com.prj.tuning.mappack.Project;

public interface Locator {
  void construct(Properties props, Project p, byte[] source, Collection<LocatedMap> locatedMaps);
  Collection<LocatedMap> locate(byte[] target);
}
