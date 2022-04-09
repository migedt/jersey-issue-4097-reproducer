package reproducer;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;


public class Main {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        startServer(port);
        System.out.println("Started server at port " + port);

        WebTarget webTarget = ClientBuilder.newClient().target("http://localhost:" + port + "/helloWorld");

        System.out.println("Received 1st response (410): " + webTarget.request().get().toString());
        System.out.println("Received 2nd response (200): " + webTarget.request().get().toString());
        System.out.println("Next request will hang and not return a response.");
        System.out.println("Received 3rd response (hangs): " + webTarget.request().get().toString());
    }

    private static void startServer(int port) throws Exception {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.setConnectors(new Connector[]{connector});

        ServletContainer servlet = new ServletContainer();
        ServletHolder holder = new ServletHolder(servlet);
        holder.setInitParameter(ServerProperties.PROVIDER_PACKAGES, "reproducer");
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.addServlet(holder, "/*");

        server.setHandler(servletContextHandler);
        server.start();
    }
}