package com.jd.monitor.server.filemetrics;

import java.io.File;
import org.apache.log4j.Logger;


class JFileWatcher implements Runnable {

  private static Logger logger = Logger.getLogger(JFileWatcher.class);

  private enum State {

    NEW, MODIFIED, DELETED
  }
  private File file;
  private JFileWatcherService fileWatcherService;
  private JFileWatcherCache cache;
  private State state = State.NEW;
  private long lastModified = 0;
  private boolean dnotify = false;

  protected JFileWatcher(JFileWatcherContext ctx) {
    this.file = ctx.getFile();
    this.cache = ctx.getJFileWatcherCache();
    this.fileWatcherService = ctx.getFileWatcherService();
  }

  @Override
  public void run() {

    if (file == null) {
      logger.fatal("file is null!");
      return;
    }

    logger.debug("Watching " + file.getAbsolutePath());

    switch (state) {
      case NEW:
        state = State.DELETED;
        fileWatcherService.offer(new JFileWatcherEvent(JFileWatcherEvent.Code.NEW, file));
        if (file.exists()) {
          state = State.MODIFIED;
          lastModified = file.lastModified();
        }
        break;

      case MODIFIED:
        state = State.DELETED;
        if (file.exists()) {
          state = State.MODIFIED;
          if (lastModified < file.lastModified()) {
            fileWatcherService.offer(new JFileWatcherEvent(JFileWatcherEvent.Code.MODIFIED, file));
          }
          lastModified = file.lastModified();
        }
        break;

      case DELETED:
        state = State.DELETED;
        if (!dnotify) {
          fileWatcherService.offer(new JFileWatcherEvent(JFileWatcherEvent.Code.DELETE, file));
          dnotify = true;

          this.fileWatcherService.removeTask(this);	// Seems to work - remove this thread now were done with it
          // JFileWatcherCache.CacheRemove(file);
          cache.CacheRemove(file);
        }
        if (file.exists()) {
          state = State.NEW;
          dnotify = false;
        }
        break;
    }
  }
}
