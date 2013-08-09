
package com.jd.monitor.server.filemetrics;

import java.io.File;

public class JFileWatcherEvent {

  /**
   * File type codes
   * 
   */
  public static enum Code {
    DELETE,
    NEW,
    MODIFIED
  }
  private Code code;
  private File file;

  public JFileWatcherEvent() {
  }

  public JFileWatcherEvent(Code code, File file) {
    this.code = code;
    this.file = file;
  }
  
  /**
   * @return the type
   */
  public Code getType() {
    return code;
  }

  /**
   * @return the file
   */
  public File getFile() {
    return file;
  }
}
