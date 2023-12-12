package servers;

import java.io.IOException;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.webserver.WebServer;
import serviceImpl.GameOperationsImplRpc;

public class RpcServer {
    public static void main(String[] args) throws XmlRpcException, IOException {

        WebServer webServer = new WebServer(5007);
        XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();

        PropertyHandlerMapping phm = new PropertyHandlerMapping();

        phm.addHandler("Game", GameOperationsImplRpc.class);

        xmlRpcServer.setHandlerMapping(phm);
        webServer.start();
        System.out.println("Server is running...");
    }
}