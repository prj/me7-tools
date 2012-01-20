package com.prj.tuning.mappack;

import java.nio.ByteBuffer;

import com.prj.tuning.mappack.util.BinaryUtil;

public class Folder {
  private int id;
  private String name;
  
  private Folder() {};
  
  public static Folder fromBuffer(ByteBuffer b) {
    Folder folder = new Folder();
    
    folder.id = b.getInt();
    BinaryUtil.skip(b, 4);
    folder.name = BinaryUtil.readString(b);
    BinaryUtil.skip(b, 6);
    
    return folder;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
