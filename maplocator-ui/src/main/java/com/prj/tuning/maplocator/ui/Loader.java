package com.prj.tuning.maplocator.ui;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import com.prj.tuning.maplocator.plugin.PluginManager;

public class Loader {
  public static void main(String[] args) throws Exception {
    if (args.length == 0 || !args[0].equals("DEVEL")) {
      // Get addUrl method for classloader
      URLClassLoader classLoader = (URLClassLoader) Loader.class.getClassLoader();
      Method addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
      addUrlMethod.setAccessible(true);

      // Add all libraries in /lib to classpath
      final List<URL> libUrls = new ArrayList<URL>();
      
      File libs = new File("lib");
      for (File f : libs.listFiles()) {
        if (f.getName().endsWith(".jar")) {
          addUrlMethod.invoke(classLoader, f.toURI().toURL());
          libUrls.add(f.toURI().toURL());
        }
      }

      // Load appropriate SWT library
      String osName = System.getProperty("os.name").toLowerCase();
      String osArch = System.getProperty("os.arch").toLowerCase();

      String osPart = osName.contains("win") ? "win32" : osName.contains("mac") ? "macosx" : osName.contains("linux")
          || osName.contains("nix") ? "linux" : "";
      if (osName.length() == 0)
        throw new RuntimeException("Unknown OS name: " + osName);
      String archPart = osArch.contains("64") ? "64" : "32";

      addUrlMethod.invoke(classLoader, new File("lib/swt/swt-" + osPart + "-" + archPart + ".jar").toURI().toURL());

      // Initialize the Plugin manager asynchronously to speed up startup.
      new Thread(new Runnable() {
        public void run() {
          PluginManager.initialize(libUrls);
        }
      }).start();
    }

    // Start the UI
    new Ui().run();
  }
}
