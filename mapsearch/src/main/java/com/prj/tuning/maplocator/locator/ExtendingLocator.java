package com.prj.tuning.maplocator.locator;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.prj.tuning.maplocator.model.LocatedMap;
import com.prj.tuning.maplocator.util.LocatorUtil;
import com.prj.tuning.mappack.map.PMap;

public class ExtendingLocator extends AbstractLocator {

  public Collection<LocatedMap> locate(byte[] target) {
    Map<Integer, Integer> segmentMap = new HashMap<Integer, Integer>();
    
    Set<PMap> unkMaps = new HashSet<PMap>();
    unkMaps.addAll(p.getMaps());    

    // Find matching areas
    for (LocatedMap map : locatedMaps) {
      // Look behind
      int tgtAddr = map.getLocatedAddress() - 1;
      int srcAddr = map.getMap().getAddress() - 1;
      while (tgtAddr >= 0 && srcAddr >= 0 && target[tgtAddr] == source[srcAddr]) {
        segmentMap.put(srcAddr, tgtAddr);
        tgtAddr--;
        srcAddr--;
      }

      // Look ahead
      tgtAddr = map.getLocatedAddress() + map.getMap().getRawLength();
      srcAddr = map.getMap().getAddress() + map.getMap().getRawLength();
      while (tgtAddr < target.length && srcAddr < target.length && target[tgtAddr] == source[srcAddr]) {
        segmentMap.put(srcAddr, tgtAddr);
        tgtAddr++;
        srcAddr++;
      }
      unkMaps.remove(map.getMap());
    }
    
    // Check if any maps are contained in the matching area as a whole, add them
    for (Iterator<PMap> it = unkMaps.iterator(); it.hasNext();) {
      PMap map = it.next();
      Integer targetAddr = segmentMap.get(map.getAddress());
      if (targetAddr != null) {
        boolean allOk = true;
        for (int i = map.getAddress() + 1; i < map.getAddress() + map.getRawLength(); i++) {
          if (!segmentMap.containsKey(i)) {
            allOk = false;
            break;
          }
        }
        if (allOk) {
          locatedMaps.add(new LocatedMap(map, targetAddr, source, p, target));
          it.remove();
          status();
        }
      }
    }
    
    // Now take the rest of the maps, and do a slow extending search based on min.exact.size.
    int minSize = Integer.parseInt(props.getProperty("min.exact.size", "8"));
    int maxOffset = Integer.parseInt(props.getProperty("max.offset", "10000"));
    
    for (PMap map : unkMaps) {
      if (!LocatorUtil.allSame(map.getAddress(), source, minSize)) {
        Integer address = LocatorUtil.findMap(map.getAddress(), source, target, minSize, maxOffset);
        if (address != null) {
          locatedMaps.add(new LocatedMap(map, address, source, p, target));
          status();
        }
      }
    }

    return locatedMaps;
  }
}
