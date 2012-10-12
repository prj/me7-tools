package com.prj.tuning.maplocator.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtil {
  public static byte[] readFile(String file) throws IOException {
    BufferedInputStream is = null;
    byte[] buf = null;
    try {
      is = new BufferedInputStream(new FileInputStream(new File(file)));
      buf = new byte[is.available()];
      is.read(buf);
    }
    finally {
      if (is != null)
        is.close();
    }
    return buf;
  }
}
