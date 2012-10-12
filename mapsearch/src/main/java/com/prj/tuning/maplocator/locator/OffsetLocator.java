package com.prj.tuning.maplocator.locator;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.prj.tuning.maplocator.model.LocatedMap;
import com.prj.tuning.maplocator.util.LocatorUtil;
import com.prj.tuning.mappack.map.PMap;

public class OffsetLocator extends AbstractLocator {
  private int minSize;
  private int maxOffset;
  private int axisCount = 0;

  public Collection<LocatedMap> locate(byte[] target) {
    minSize = Integer.parseInt(props.getProperty("min.exact.size", "8"));
    maxOffset = Integer.parseInt(props.getProperty("max.offset", "10000"));

    Map<Integer, Integer> segmentMap = new HashMap<Integer, Integer>();

    Set<PMap> unkMaps = new HashSet<PMap>();
    unkMaps.addAll(p.getMaps());

    Map<Integer, PMap> addressToMap = new HashMap<Integer, PMap>();

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
      tgtAddr = map.getLocatedAddress();
      srcAddr = map.getMap().getAddress();
      while (tgtAddr < target.length && srcAddr < target.length && target[tgtAddr] == source[srcAddr]) {
        segmentMap.put(srcAddr, tgtAddr);
        tgtAddr++;
        srcAddr++;
      }

      addressToMap.put(map.getMap().getAddress(), map.getMap());
      unkMaps.remove(map.getMap());
    }
    
    // Go through maps that are not found and look for the closest match, after
    // that use it's offset.
    for (PMap map : unkMaps) {
      int i = 0;
      while (true) {
        Integer targetAddr = segmentMap.get(map.getAddress() + i);

        if (targetAddr != null) {
          locatedMaps.add(new LocatedMap(map, targetAddr - i, source, p, target));
          status();
          break;
        }

        targetAddr = segmentMap.get(map.getAddress() - i);

        if (targetAddr != null) {
          locatedMaps.add(new LocatedMap(map, targetAddr + i, source, p, target));
          status();
          break;
        }

        i++;
      }
      addressToMap.put(map.getAddress(), map);
    }

    // Match up all the axes
    System.err.println();
    System.err.println("Finding axes:");
    
    for (LocatedMap map : locatedMaps) {
      if (map.getMap().getxAxis().getDataSource().isEprom()) {
        map.setxAxisAddr(locateAxis(map.getMap().getxAxis().getAddress(), addressToMap, segmentMap, target));
        axisStatus();
      }

      if (map.getMap().getyAxis().getDataSource().isEprom()) {
        map.setyAxisAddr(locateAxis(map.getMap().getyAxis().getAddress(), addressToMap, segmentMap, target));
        axisStatus();
      }
    }

    return locatedMaps;
  }

  private int locateAxis(int addr, Map<Integer, PMap> addressToMap, Map<Integer, Integer> segmentMap, byte[] target) {
    // First see if the axis is actually a map that is already defined, and
    // return it.
    if (addressToMap.containsKey(addr)) {
      return addressToMap.get(addr).getAddress();
    }

    // If it is not, then check the segment map to see if there is an exact
    // match at the starting location
    if (segmentMap.containsKey(addr)) {
      return segmentMap.get(addr);
    }

    // Now try to do pattern matching around the approximate area
    if (!LocatorUtil.allSame(addr, source, minSize)) {
      Integer targetAddr = LocatorUtil.findMap(addr, source, target, minSize, maxOffset);
      if (targetAddr != null) {
        return targetAddr;
      }
    }

    // If all else fails, find the closest register in the segment map and use
    // the calculated offset.
    int i = 0;
    while (true) {
      Integer targetAddr = segmentMap.get(addr + i);

      if (targetAddr != null) {
        return targetAddr - i;
      }

      targetAddr = segmentMap.get(addr - i);

      if (targetAddr != null) {
        return targetAddr + i;
      }

      i++;
    }
  }
  
  private void axisStatus() {
    System.err.print("\r" + axisCount++);
  }
}
