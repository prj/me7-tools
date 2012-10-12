package com.prj.tuning.maplocator.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.prj.tuning.mappack.map.PMap;

public class LocatorUtil {

  public static boolean allSame(PMap map, byte[] source) {
    return allSame(map.getAddress(), source, map.getRawLength());
  }

  public static boolean allSame(int addr, byte[] source, int len) {
    int mapEnd = addr + len - 1;
    Set<Integer> values = new HashSet<Integer>();
    for (int i = addr; i < mapEnd; i++) {
      values.add(i);
    }
    values.add(mapEnd);
    return values.size() <= 3;
  }

  public static boolean checkMap(PMap map, byte[] source, byte[] target, int offset) {
    return checkMap(map.getAddress(), source, target, offset, map.getRawLength());
  }

  public static boolean checkMap(int addr, byte[] source, byte[] target, int offset, int length) {
    int mapEnd = addr + length;
    for (int i = addr; i < mapEnd; i++) {
      int loc = i + offset;
      if (loc < 0)
        continue;
      if (!(loc < target.length))
        break;

      if (source[i] != target[loc]) {
        return false;
      }
    }

    return true;
  }

  public static Integer findMap(int addr, byte[] source, byte[] target, int minSize, int maxOffset) {
    Set<Integer> foundAddresses = new LinkedHashSet<Integer>();

    for (int i = 0; i <= maxOffset; i++) {
      if (LocatorUtil.checkMap(addr, source, target, i, minSize)) {
        foundAddresses.add(addr + i);
      }
      else if (LocatorUtil.checkMap(addr, source, target, -i, minSize)) {
        foundAddresses.add(addr - i);
      }
    }

    switch (foundAddresses.size()) {
    case 0:
      return null;
    case 1:
      return foundAddresses.iterator().next();
    }
    
    Set<Integer> tmpSet = new HashSet<Integer>();
    tmpSet.addAll(foundAddresses);
    
    // If there is more than one match, extend ahead, until just one is left.
    // TODO: Handle out of bounds.
    int offset = 1;
    while (tmpSet.size() > 1) {
      for (Iterator<Integer> i = tmpSet.iterator(); i.hasNext();) {
        int targetAddr = i.next();
        if (addr + offset + minSize < 0 || targetAddr + offset + minSize < 0 || source[addr + offset + minSize] != target[targetAddr + offset + minSize]) {
          i.remove();
        }
      }
      offset++;
    }
    
    if (tmpSet.size() == 1) {
      return tmpSet.iterator().next();
    }
    
    // If this logic is reached, it means that the extend ahead ran out exactly at the same time.
    // In this case extend behind.
    tmpSet.addAll(foundAddresses);
    
    offset = 1;
    while (tmpSet.size() > 1) {
      for (Iterator<Integer> i = tmpSet.iterator(); i.hasNext();) {
        int targetAddr = i.next();
        if (addr - offset < 0 || targetAddr - offset < 0 || source[addr - offset] != target[targetAddr - offset]) {
          i.remove();
        }
      }
      offset++;
    }
    
    if (tmpSet.size() == 1) {
      return tmpSet.iterator().next();
    }
    
    // If this is reached, it means the regions are equal, so pick the one with the smallest offset.
    
    return foundAddresses.iterator().next();
  }
}
