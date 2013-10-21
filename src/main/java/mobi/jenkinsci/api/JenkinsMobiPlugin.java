package mobi.jenkinsci.api;

import hudson.Plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JenkinsMobiPlugin extends Plugin {
  public static final Logger LOG = LoggerFactory
      .getLogger(JenkinsMobiPlugin.class);
  private JenkinsMobiServer server = new JenkinsMobiServer("localhost", 8888);

  @Override
  public void start() throws Exception {
    LOG.info("Starting JenkinsMobi API ...");
    server.start();
  }

  @Override
  public void stop() throws Exception {
    LOG.info("Stopping JenkinsMobi API ...");
    server.stop();
  }

}
