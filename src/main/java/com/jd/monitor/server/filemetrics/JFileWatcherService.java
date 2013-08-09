/**
 * Copyright 2011 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.jd.monitor.server.filemetrics;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component("fservice")
public class JFileWatcherService extends Thread {

  private static Logger logger = Logger.getLogger(JFileWatcherService.class);
  private boolean abort = false;
  private final List<Runnable> tasks = new CopyOnWriteArrayList<Runnable>();
  private final BlockingQueue<JFileWatcherEvent> queue = new LinkedBlockingQueue<JFileWatcherEvent>();
  private final JFileWatcherCache cache = new JFileWatcherCache();
  private final ExecutorService pool;
  protected final Properties props;

  public JFileWatcherService() {
    this(Runtime.getRuntime().availableProcessors() * 2);
  }

  public JFileWatcherService(int numThreads) {
    logger.info("# File Watcher Threads : " + numThreads);
    pool = Executors.newFixedThreadPool(numThreads);
    props = new Properties();
    try {
      props.load(getClass().getClassLoader().getResourceAsStream("jfilewatcher.properties"));
    } catch (Exception e) {
      logger.warn("WARN", e);
    }
  }

  /**
   * Add new file watcher task Watches a folder/file
   *
   * @param task
   */
  protected void addTask(Runnable task) {
    tasks.add(task);
  }

  /**
   * Remove file watcher task if the file is deleted.
   *
   * @param task
   */
  protected void removeTask(Runnable task) {
    tasks.remove(task);
  }

  /**
   * Adds an event in the notification queue This will notify the listener
   *
   * @param event
   */
  protected void offer(JFileWatcherEvent event) {
    queue.offer(event);
  }

  /**
   * Obtains a file lock. This maybe useful for those applications who do not
   * want to run multiple file watchers at same time. You could be running two
   * or more file watchers in active-passive mode on same/different machines.
   * Whoever gets lock, becomes primary file watcher If primary goes down, one
   * of the others will become master automatically.
   *
   * @param file Lock file name
   * @param block Return if lock not available
   */
  public boolean lock(File file, boolean block) {
    boolean flag = false;
    RandomAccessFile raf = null;

    logger.info("Getting lock on " + file.getAbsolutePath());

    try {
      raf = new RandomAccessFile(file, "rw");
      if (block) {
        raf.getChannel().lock();
        flag = true;
      } else {
        if (null != raf.getChannel().tryLock()) {
          flag = true;
        }
      }
    } catch (Exception e) {
      System.err.println(e);
    }

    if (flag) {
      logger.info("Got lock!");
    }

    return flag;
  }

  /**
   * Specify the folder/file patterns you need to be watched This can include
   * wild card characters
   *
   * @param file Watch this file
   * @param patterns File name patterns
   * @param frequency Polling frequency
   */
  public void watch(File file) {
    watch(file, null);
  }

  /**
   * Specify the folder/file patterns you need to be watched This can include
   * wild card characters
   *
   * @param file Watch this file
   * @param patterns File name patterns
   * @param frequency Polling frequency
   */
  public void watch(File file, String[] patterns) {
    JFileWatcherContext ctx = new JFileWatcherContext();

    if (file.isDirectory()) {
      ctx.setFile(file);
      ctx.setPatterns(patterns != null ? patterns : new String[]{"*.*"});
    } else {
      ctx.setFile(file.getParentFile());
      logger.info("Watch folder: " + file.getParentFile().getAbsolutePath() + " pattern: " + file.getName());
      ctx.setPatterns(new String[]{file.getName()});
    }

    ctx.setJFileWatcherCache(cache);
    ctx.setFileWatcherService(this);
    addTask(new JFolderWatcher(ctx));
  }

  /**
   * Returns next available file. Blocks if not available
   *
   * @return JFileWatcherEvent instance
   * @throws InterruptedException
   */
  public JFileWatcherEvent next() throws InterruptedException {
    return queue.take();
  }

  /**
   * Returns next available file. Blocks if not available
   *
   * @return JFileWatcherEvent instance
   * @throws InterruptedException
   */
  public JFileWatcherEvent next(long timeout, TimeUnit tu) throws InterruptedException {
    return queue.poll(timeout, tu);
  }

  /**
   * Stop the file watcher service
   */
  public void terminate() {
    abort = true;
  }

  /**
   * Main loop ...
   */
  @Override
  public void run() {
    long sleepPeriod = 500;

    if (props.containsKey("sleep.period")) {
      sleepPeriod = Long.parseLong(props.getProperty("sleep.period"));
    }

    while (!abort) {
      logger.debug("# tasks : " + tasks.size());
      final CountDownLatch cd = new CountDownLatch(tasks.size());
      for (final Runnable task : tasks) {
        pool.submit(new Runnable() {

          @Override
          public void run() {
            try {
              task.run();
            } catch (Exception e) {
              logger.fatal("ERR", e);
            } finally {
              cd.countDown();
            }
          }
        });
      }

      try {
        cd.await();
      } catch (Exception e) {
      }

      try {
        Thread.sleep(sleepPeriod);
      } catch (Exception e) {
      }
    }

    pool.shutdown();
    logger.info("File watcher is shutdown");
  }

  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      System.err.println("Usage: java JFileWatcher <file>|<folder> [<pattern> <pattern> ...]");
      System.exit(1);
    }

    // System.out.println("# args " + args.length);

    JFileWatcherService fservice = new JFileWatcherService();
    File file = new File(args[0]);
    String[] patterns = null;
    if (args.length > 1) {
      patterns = new String[args.length - 1];
      System.arraycopy(args, 1, patterns, 0, args.length - 1);
    }

    fservice.watch(file, patterns);

    // Start file watcher service (it is a thread!)
    fservice.start();

    // Wait for the file event (NEW, MODIFIED, DELETE)
    SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
    while (true) {
      JFileWatcherEvent e = fservice.next();
      System.out.println(
              fmt.format(e.getFile().lastModified())
              + " : "
              + e.getType()
              + " : "
              + e.getFile().getAbsolutePath()
      );
    }
  }
}
