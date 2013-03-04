import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;


public class DevelopWebApp {
    private static final int HTTP_SERVER_PORT = 8080;
    private static final String WEBAPP_DIR = "src/main/webapp";
    private static final String CONTEXT_PATH = "/";

    public static void main(final String argv[]) throws Exception {
        final Server server = new Server(HTTP_SERVER_PORT);
        WebAppContext webAppContext = new WebAppContext(WEBAPP_DIR, CONTEXT_PATH);
        server.setHandler(webAppContext);
        server.start();
        System.err.println("Started!");
        server.join();
    }
}
