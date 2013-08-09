
package com.jd.monitor.server.filemetrics;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jregex.util.io.PathPattern;
import org.apache.log4j.Logger;

class JFolderWatcher implements Runnable {

  private Logger logger = Logger.getLogger(JFolderWatcher.class);
  protected JFileWatcherContext ctx;
  protected final Set<String> cache = new HashSet<String>();
  protected List<PathPattern> patterns = new ArrayList<PathPattern>();
  private JFileWatcherService fileWatcherService;

  protected JFolderWatcher(JFileWatcherContext ctx) {
    this.ctx = ctx;
    this.fileWatcherService = ctx.getFileWatcherService();
    
    for (int i = 0; i < ctx.getPatterns().length; i++) {
      patterns.add(new PathPattern(ctx.getPatterns()[i]));
    }
  }

  private boolean match(File file) {
    boolean flag = false;

    for (PathPattern pattern : patterns) {
      if (pattern.matches(file.getName())) {
        flag = true;
        break;
      }
    }
    
    return flag;
  }

  @Override
  public void run() {
    if (ctx == null) {
      return;
    }

    if (ctx.getFile() == null) {
      return;
    }

    File root = ctx.getFile();
    if (!root.isDirectory()) {
      logger.debug("root " + root.getAbsolutePath() + " is not directory!");
      return;
    }

    if (!root.exists()) {
      logger.debug("file " + root.getAbsolutePath() + " does not exist!");
      return;
    }

    File[] files = root.listFiles();
    if (files == null) {
      logger.debug("no files for " + root.getAbsolutePath());
      return;
    }

    /**
     * 1/14 | Ejaz Fixed a bug here. If user gives high level folder and file
     * patterns, file watcher did not recurse inside. Added the logic to recurse
     * the folders and find the files, matching the files, to watch!
     *
     */
    for (File file : files) {
      logger.debug("Check file " + file.getAbsolutePath());
      if (!ctx.getJFileWatcherCache().CacheContains(file)) {

        if (file.isDirectory()) {
          logger.debug("Watch folder : " + file.getAbsolutePath());

          JFileWatcherContext ctx2 = ctx.getCopy();
          ctx2.setFile(file);
          ctx2.setPatterns(ctx.getPatterns());

          fileWatcherService.addTask(new JFolderWatcher(ctx2));
        } else {
          // if (regex.matcher(file.getName()).matches()) {
          if (match(file)) {
            JFileWatcherContext ctx2 = ctx.getCopy();
            ctx2.setFile(file);

            logger.debug("Watch file : " + file.getAbsolutePath());

            fileWatcherService.addTask(new JFileWatcher(ctx2));
          }
        }
        ctx.getJFileWatcherCache().CacheAdd(file); // Add to global cache
      }
    }
  }
}
