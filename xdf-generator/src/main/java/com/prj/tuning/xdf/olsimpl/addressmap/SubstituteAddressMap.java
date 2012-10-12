package com.prj.tuning.xdf.olsimpl.addressmap;

import java.util.Map;

public class SubstituteAddressMap implements AddressMap {
  private Map<Integer, Integer> addressMap;

  public SubstituteAddressMap(Map<Integer, Integer> addressMap) {
    this.addressMap = addressMap;
  }

  @Override
  public int subAddr(int addr) {
    Integer newAddr = addressMap.get(addr);
    if (newAddr == null) {
      return 0;
    }
    return newAddr;
  }
}
