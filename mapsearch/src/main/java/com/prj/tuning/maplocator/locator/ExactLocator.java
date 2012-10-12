package com.prj.tuning.maplocator.locator;

import java.util.Collection;

import com.prj.tuning.maplocator.model.LocatedMap;
import com.prj.tuning.maplocator.util.LocatorUtil;
import com.prj.tuning.mappack.map.PMap;

public class ExactLocator extends AbstractLocator {

  public Collection<LocatedMap> locate(byte[] target) {
    int maxOffset = Integer.parseInt(props.getProperty("max.offset", "10000"));
    int minSize = Integer.parseInt(props.getProperty("min.exact.size", "8"));

    for (PMap map : p.getMaps()) {
      if (map.getRawLength() > minSize && !LocatorUtil.allSame(map, source)) {
        for (int i = 0; i <= maxOffset; i++) {
          if (LocatorUtil.checkMap(map, source, target, i)) {
            locatedMaps.add(new LocatedMap(map, map.getAddress() + i, source, p, target));
            break;
          }
          else if (LocatorUtil.checkMap(map, source, target, -i)) {
            locatedMaps.add(new LocatedMap(map, map.getAddress() - i, source, p, target));
            break;
          }
        }
      }
      status();
    }

    return locatedMaps;
  }

}
