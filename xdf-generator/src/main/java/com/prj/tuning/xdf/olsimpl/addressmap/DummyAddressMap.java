package com.prj.tuning.xdf.olsimpl.addressmap;

public class DummyAddressMap implements AddressMap {

  @Override
  public int subAddr(int addr) {
    return addr;
  }

}
