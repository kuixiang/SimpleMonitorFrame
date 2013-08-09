package com.jd.monitor.server.filemetrics;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class JFileWatcherCache {

  protected final Set<String> cache = new HashSet<String>();

  public JFileWatcherCache() {
  }

  public void CacheAdd(File theFile) {
    synchronized (cache) {
      cache.add(theFile.getAbsolutePath());
    }
  }

  public void CacheRemove(File theFile) {
    synchronized (cache) {
      cache.remove(theFile.getAbsolutePath());
    }
  }

  public boolean CacheContains(File theFile) {
    synchronized (cache) {
      return cache.contains(theFile.getAbsolutePath());
    }
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();

    String newLine = System.getProperty("line.separator");

    Iterator<String> iterator;
    iterator = cache.iterator();

    while (iterator.hasNext()) {
      result.append(" Element {");
      result.append(iterator.next());
      result.append(newLine);
      result.append("}");
    }
    
    result.append(newLine);
    result.append("Size { ");
    result.append(cache.size());
    result.append(" }");
    result.append(newLine);
    
    return result.toString();
  }
}
