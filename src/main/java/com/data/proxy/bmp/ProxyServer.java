//package com.data.proxy.bmp;
//
//import lombok.extern.slf4j.Slf4j;
//import net.lightbody.bmp.BrowserMobProxy;
//import net.lightbody.bmp.BrowserMobProxyServer;
//import org.littleshoot.proxy.HttpProxyServer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//
//@Slf4j
//@Component
//public class ProxyServer {
//    private HttpProxyServer proxyServer;
//
//    @Value("${proxy.server.port}")
//    private int proxyServerPort;
////    @Value("${proxy.server.username}")
////    private String proxyUserName;
////    @Value("${proxy.server.password}")
////    private String proxyPassword;
//
//    //    @PostConstruct
//    public void initProxyServer() throws UnknownHostException {
//        BrowserMobProxy proxy =
//                new BrowserMobProxyServer();
//        proxy.start(8180, InetAddress.getByName("localhost"));
//    }
//
//    public static void main(String[] args) throws UnknownHostException {
//        BrowserMobProxy proxy =
//                new BrowserMobProxyServer();
//        proxy.start(8180, InetAddress.getByName("localhost"));
//        proxy.addResponseFilter((response, contents, messageInfo) -> {
//                    System.out.println("======================================="+messageInfo.getUrl() + " >>>>>> " + response.getStatus() + " : " + response.headers().get("cookie") + " | " + contents.getTextContents());
//                }
//        );
//
//    }
//
//
//}
