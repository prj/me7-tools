package com.prj.tuning.maplocator;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import com.prj.tuning.maplocator.ui.Loader;


public class CrossPlatformRunner {
  public static void main(String[] args) throws Throwable {
    String osName = System.getProperty("os.name").toLowerCase();
    
    // For mac we have to add -XstartOnFirstThread and re-run the VM.
    if (osName.contains("mac")) {
      ProcessBuilder builder = new ProcessBuilder(new String[] {
          "/usr/bin/java", "-XstartOnFirstThread", "-cp",
          "maplocator-ui.jar",
          "com.prj.tuning.maplocator.ui.Loader" });
      builder.directory(new File("."));
      builder.redirectErrorStream(true);
      Process process = builder.start();

      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }
    }
    else {
      Loader.main(args);
    }
  }
}
