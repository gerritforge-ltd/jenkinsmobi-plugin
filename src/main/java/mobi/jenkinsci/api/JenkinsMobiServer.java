package mobi.jenkinsci.api;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.security.UserRealm;
import org.mortbay.jetty.webapp.WebAppContext;

import com.lmit.jenkinscloud.realm.jetty.JenkinsCloudJettyRealm;

public class JenkinsMobiServer extends Server {
  private static final int MAX_IDLE_TIME = 30000;

  public JenkinsMobiServer(String hosthttpListenAddress, int httpPort) {
    setConnectors(new Connector[] {getHttpConnector(hosthttpListenAddress,
        httpPort)});
    setHandler(getWebAppContext());
    setUserRealms(new UserRealm[] {new JenkinsCloudJettyRealm("http://"
        + hosthttpListenAddress + ":" + httpPort + "/registry")});
  }

  private WebAppContext getWebAppContext() {
    WebAppContext webapp = new WebAppContext();
    webapp.setContextPath("/");
    File pluginRoot = whoAmI();
    webapp.setResourceBase(pluginRoot.getAbsolutePath());
    webapp.setDescriptor(new File(pluginRoot, "/WEB-INF/web.xml")
        .getAbsolutePath());
    webapp.setContextPath("/");
    webapp.setParentLoaderPriority(true);
    return webapp;
  }

  private SelectChannelConnector getHttpConnector(String hosthttpListenAddress,
      int httpPort) {
    SelectChannelConnector connector = new SelectChannelConnector();
    connector.setPort(httpPort);
    connector.setMaxIdleTime(MAX_IDLE_TIME);
    connector.setHost(hosthttpListenAddress);
    return connector;
  }

  private File whoAmI() {
    URLClassLoader pluginClassLoader =
        (URLClassLoader) JenkinsMobiServer.class.getClassLoader();
    URL[] urls = pluginClassLoader.getURLs();
    for (URL url : urls) {
      if (url.getProtocol().equalsIgnoreCase("file")) {
        String urlPath = url.getPath();
        urlPath = urlPath.substring(0, urlPath.lastIndexOf("/WEB-INF"));
        return new File(urlPath);
      }
    }
    throw new IllegalArgumentException(
        "Cannot find base directory of JenkinsMobi API Plugin");
  }
}
