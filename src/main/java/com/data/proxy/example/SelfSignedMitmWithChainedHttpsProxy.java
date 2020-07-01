//package com.data.proxy.example;
//
//import io.netty.handler.codec.http.HttpRequest;
//import org.littleshoot.proxy.ChainedProxy;
//import org.littleshoot.proxy.ChainedProxyAdapter;
//import org.littleshoot.proxy.ChainedProxyManager;
//import org.littleshoot.proxy.extras.SelfSignedMitmManager;
//import org.littleshoot.proxy.extras.SelfSignedSslEngineSource;
//import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
//
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLEngine;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//import java.net.InetSocketAddress;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//import java.util.Queue;
//
//public class SelfSignedMitmWithChainedHttpsProxy {
//
//    private static final String UPSTREAM_PROXY_HOST = "192.168.137.142";
//    private static final int UPSTREAM_PROXY_PORT = 8101;
//
//    private static final int PORT = 8100;
//
//    public static void main(String[] args) {
//
//        setupUpstreamHttpsProxy();
//
//        ChainedProxyManager cpm = getChainedProxyManager();
//
//        DefaultHttpProxyServer.bootstrap()
//        .withPort(PORT)
//        .withManInTheMiddle(new SelfSignedMitmManager())
//        .withChainProxyManager(cpm)
//        .withAllowLocalOnly(false)
//        .withName("Mitm")
//        .start();
//    }
//
//    private static ChainedProxyManager getChainedProxyManager() {
//        return new ChainedProxyManager() {
//
//            public void lookupChainedProxies(HttpRequest httpRequest, Queue<ChainedProxy> chainedProxies) {
//
//                ChainedProxyAdapter chainedHttpsProxy = getChainedHttpsProxy();
//                chainedProxies.add(chainedHttpsProxy);
//            }
//
//            private ChainedProxyAdapter getChainedHttpsProxy() {
//
//                return new ChainedProxyAdapter(){
//
//                    @Override
//                    public InetSocketAddress getChainedProxyAddress() {
//                        return new InetSocketAddress(UPSTREAM_PROXY_HOST, UPSTREAM_PROXY_PORT);
//                    }
//
//                    @Override
//                    public boolean requiresEncryption() {
//                        return true;
//                    }
//
//                    @Override
//                    public SSLEngine newSslEngine() {
//                        return getSslMitmEngine();
//                    }
//
//                    @Override
//                    public SSLEngine newSslEngine(String peerHost, int peerPort) {
//                        return newSslEngine();
//                    }
//
//                    private SSLEngine getSslMitmEngine() {
//                        try {
//                            SSLContext sslContext = SSLContext.getInstance("TLS");
//
//                            TrustManager tm = new X509TrustManager() {
//
//                                public X509Certificate[] getAcceptedIssuers() {
//                                    return null;
//                                }
//
//                                public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
//
//                                }
//
//                                public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
//
//                                }
//                            };
//
//                            TrustManager[] tms = new TrustManager[] { tm };
//
//                            sslContext.init(null, tms, null);
//
//                            return sslContext.createSSLEngine();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        return null;
//                    }
//                };
//            }
//
//        };
//    }
//
//    private static void setupUpstreamHttpsProxy() {
//        DefaultHttpProxyServer.bootstrap()
//        .withAddress(new InetSocketAddress(UPSTREAM_PROXY_HOST, UPSTREAM_PROXY_PORT))
//        .withName("Upstream")
//        .withSslEngineSource(new SelfSignedSslEngineSource())
//        .withAuthenticateSslClients(false)
//        .start();
//    }
//}
