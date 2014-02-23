/*
 * Copyright (c) 2014																 
 *	Mikol Faro			<mikol.faro@gmail.com>
 *	Simone Mangano		<simone.mangano@ieee.org>
 *	Mattia Tortorelli	<mattia.tortorelli@gmail.com>
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 */

package org.biokoframework.http.rest;

import java.net.URI;
import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;

public abstract class WebAppTest {
	
	private Server fServer;
	private URI fUri;

	public void init() throws Exception {
		init(0);
	}
	
	public void init(int port) throws Exception {
		fServer = new Server(port);
		ServletContextHandler handler = new ServletContextHandler(fServer, "/", ServletContextHandler.SESSIONS);
		handler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
		handler.addEventListener(getServletConfig());
		handler.addServlet(DefaultServlet.class, "/");
		
		ConsoleAppender console = new ConsoleAppender(new PatternLayout("[%d] [%t] [%p] [%F:%L] : %m%n"));
		console.setThreshold(Level.INFO);
		Logger.getRootLogger().addAppender(console);
	}
	
	public URI getURI() {
		return fUri;
	}
	
	public void start() throws Exception {
		fServer.start();
		fUri = fServer.getURI();
	}
	
	public void stop() throws Exception {
		fServer.stop();
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
