package com.prj.tuning.maplocator.locator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import com.prj.tuning.maplocator.model.LocatedMap;
import com.prj.tuning.mappack.Project;

public class LocatorChain {
  private static final List<Class<? extends Locator>> locatorChain =  new ArrayList<Class<? extends Locator>>();;
  
  static {
    register(ExactLocator.class);
    register(ExtendingLocator.class);
    register(OffsetLocator.class);
  }
  
  private static void register(Class<? extends Locator> klazz) {
    locatorChain.add(klazz);
  }
  
  public static Collection<LocatedMap> getMaps(Properties props, Project p, byte[] source, byte[] target) throws InstantiationException, IllegalAccessException {
    
    Collection<LocatedMap> locatedMaps = new HashSet<LocatedMap>();
    
    for (Class<? extends Locator> klazz : locatorChain) {
      System.err.println("Running: " + klazz.getSimpleName());
      Locator locator = klazz.newInstance();
      locator.construct(props, p, source, locatedMaps);
      locatedMaps = locator.locate(target);
      System.err.println();
    }
    
    return locatedMaps;
  }
}
