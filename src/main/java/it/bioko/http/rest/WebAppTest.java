package it.bioko.http.rest;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.google.inject.servlet.GuiceServletContextListener;

public abstract class WebAppTest {
	
	private Server _server;
	private int _port;

	public void init() throws Exception {
		init(0);
	}
	
	public void init(int port) throws Exception {
		_server = new Server(port);
		
		ServletContextHandler handler = new ServletContextHandler(_server, "/", ServletContextHandler.SESSIONS);
		handler.addEventListener(getServletConfig());
	}

	public int getPort() {
		return _port;
	}
	
	public void start() throws Exception {
		_server.start();
		_port = _server.getConnectors()[0].getLocalPort();
	}
	
	public void stop() throws Exception {
		_server.stop();
	}

	/**
	 * Java version of the web.xml snippet:
	 * <pre>
	 * {@code
	 * <listener>
	 *   <listener-class>it.bioko.mysystem.injection.MySystemServletConfig</listener-class>
	 * </listener>
	 * }
	 * <pre>
	 * @return An instance of a subclass of {@link GuiceServletContextListener}
	 */
	protected abstract GuiceServletContextListener getServletConfig();
	
}
