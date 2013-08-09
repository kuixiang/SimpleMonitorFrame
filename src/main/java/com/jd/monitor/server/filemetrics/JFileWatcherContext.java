
package com.jd.monitor.server.filemetrics;

import java.io.File;

public class JFileWatcherContext {

  private File file;
  private String[] patterns;
  private JFileWatcherCache cache;
  private JFileWatcherService instance;

  @Override
  public String toString() {
    StringBuilder buff = new StringBuilder();
    buff.append("folder=").append(file.getAbsolutePath()).append(";");

    buff.append("patterns=");
    for (int i = 0; i < patterns.length; i++) {
      String pattern = patterns[i];
      buff.append(pattern);
      if (i < patterns.length - 1) {
        buff.append(",");
      }
    }

    return buff.toString();
  }
  
  public void setFileWatcherService(JFileWatcherService instance) {
  	this.instance = instance;
  }
  
  public JFileWatcherService getFileWatcherService() {
  	return instance;
  }

  public void setJFileWatcherCache(JFileWatcherCache cache) {
    this.cache = cache;
  }
  
  public JFileWatcherCache getJFileWatcherCache() {
    return this.cache;
  }

  public File getFile() {
    return file;
  }
  public void setFile(File root) {
    this.file = root;
  }
  public String[] getPatterns() {
    return patterns;
  }

  /**
   * @param patterns the patterns to set
   */
  public void setPatterns(String[] patterns) {
    this.patterns = new String[patterns.length];
    System.arraycopy(patterns, 0, this.patterns, 0, patterns.length);
  }
  
  public JFileWatcherContext getCopy() {
  	JFileWatcherContext ctx = new JFileWatcherContext();
  	
  	ctx.setFile(file);
  	ctx.setPatterns(patterns);
  	ctx.setFileWatcherService(instance);
    ctx.setJFileWatcherCache(cache);
  	
  	return ctx;
  }
}
