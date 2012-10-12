package com.prj.tuning.maplocator.plugin;

import java.util.Collection;

import com.prj.tuning.maplocator.model.LocatedMap;

/**
 * The main map locator plugin interface.
 * Your plugin should implement this interface and also have a default constructor.
 * @author prj
 */
public interface PluginCallback {
  void handleMaps(Collection<? extends LocatedMap> maps);
}
