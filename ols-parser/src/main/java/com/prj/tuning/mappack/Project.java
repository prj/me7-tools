package com.prj.tuning.mappack;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import com.prj.tuning.mappack.map.PMap;
import com.prj.tuning.mappack.util.BinaryUtil;

public class Project {
  protected Header header;
  protected Collection<PMap> maps;
  protected Collection<Folder> folders;
  protected URL projectUrl;
  protected ByteBuffer projectData;
  protected boolean parsed;

  public Project(URL projectUrl) {
    this.projectUrl = projectUrl;
    this.parsed = false;
  }

  public Project parse() throws IOException {
    if (!parsed) {
      projectData = BinaryUtil.readFile(projectUrl);
      header = Header.fromBuffer(projectData);

      maps = new HashSet<PMap>();

      for (int i = 0; i < header.getMapCount(); i++) {
        maps.add(new PMap().parse(projectData));
      }

      BinaryUtil.skip(projectData, 12);

      int folderCount = projectData.getInt();
      folders = new HashSet<Folder>();

      for (int i = 0; i < folderCount; i++) {
        folders.add(Folder.fromBuffer(projectData));
      }
    }
    parsed = true;
    return this;
  }

  public Header getHeader() {
    return header;
  }

  public Collection<PMap> getMaps() {
    return Collections.unmodifiableCollection(maps);
  }

  public Collection<Folder> getFolders() {
    return Collections.unmodifiableCollection(folders);
  }
}
