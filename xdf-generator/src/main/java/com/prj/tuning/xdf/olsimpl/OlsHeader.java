package com.prj.tuning.xdf.olsimpl;

import com.prj.tuning.mappack.Header;
import com.prj.tuning.xdf.binding.XdfHeader;

public class OlsHeader extends XdfHeader {
  private Header header;

  public OlsHeader(Header header) {
    this.header = header;
  }

  @Override
  public String getDeftitle() {
    return header.getFileName();
  }

}
