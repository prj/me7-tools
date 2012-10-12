package com.prj.tuning.maplocator.plugin;

import java.util.Collection;

import com.prj.tuning.maplocator.model.LocatedMap;

public interface LocatorPlugin {
  Collection<? extends LocatedMap> locateMaps(byte[] binary);
}
