package com.prj.tuning.mappack.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class BinaryUtil {
  private static final int BLOCK_SIZE = 65536;

  public static String readString(ByteBuffer b) {
    int len = b.getInt();
    if (b.position() + len > b.capacity() - 1 || len < 0) {
      throw new IllegalStateException("Tried to read string of length " + len + " at " + b.position());
    }

    if (len > 0) {
      byte[] buf = new byte[len];
      b.get(buf);
      b.get();
      try {
        return new String(buf, "ISO-8859-15");
      }
      catch (UnsupportedEncodingException e) {
        throw new IllegalStateException(e.getMessage());
      }
    }
    else {
      return "";
    }
  }

  public static void skip(ByteBuffer b, int bytes) {
    b.position(b.position() + bytes);
  }

  public static void transferToBuf(ByteBuffer buf, InputStream is) throws IOException {
    byte b[] = new byte[BLOCK_SIZE];
    while (is.available() > 0) {
      if (is.available() >= BLOCK_SIZE) {
        is.read(b);
        buf.put(b);
      }
      else {
        int len = is.available();
        is.read(b);
        buf.put(b, 0, len);
      }
    }
    buf.rewind();
  }

  public static boolean compareBinary(ByteBuffer buf1, ByteBuffer buf2, int addr1, int addr2, int len) {
    byte[] b1 = new byte[len];
    byte[] b2 = new byte[len];

    buf1.position(addr1);
    buf2.position(addr2);

    buf1.get(b1);
    buf2.get(b2);

    return Arrays.equals(b1, b2);
  }
}
