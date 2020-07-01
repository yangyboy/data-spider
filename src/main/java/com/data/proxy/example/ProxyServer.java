//package com.data.proxy;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.handler.codec.DecoderResult;
//import io.netty.handler.codec.http.*;
//import io.netty.util.AttributeKey;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.io.IOUtils;
//import org.littleshoot.proxy.*;
//import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
//import org.littleshoot.proxy.impl.ThreadPoolConfiguration;
//import org.littleshoot.proxy.mitm.CertificateSniffingMitmManager;
//import org.littleshoot.proxy.mitm.RootCertificateException;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.io.ByteArrayInputStream;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.UnknownHostException;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//import java.util.Map;
//import java.util.zip.GZIPInputStream;
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
//    @PostConstruct
//    public void initProxyServer() {
//        try {
//            this.startProxyServer();
//            log.info("proxyServer is running at {}", proxyServer.getListenAddress());
//        } catch (RootCertificateException | UnknownHostException e) {
//            log.error("proxyServer is start error!", e);
//        }
//    }
//
//
//    public void stop() {
//        if (proxyServer != null) {
//            proxyServer.stop();
//        }
//
//    }
//
//    private static final AttributeKey<String> CONNECTED_URL = AttributeKey.valueOf("connected_url");
//
//    private void startProxyServer() throws RootCertificateException, UnknownHostException {
//        proxyServer = DefaultHttpProxyServer.bootstrap()
//                .withAddress(new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), proxyServerPort))
////                .withPort(proxyServerPort)
//                .withManInTheMiddle(new CertificateSniffingMitmManager())
//                .withFiltersSource(new HttpFiltersSourceAdapter() {
//                    public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
//                        return new HttpFiltersAdapter(originalRequest) {
//
//                            @Override
//                            public HttpResponse clientToProxyRequest(HttpObject httpObject) {
//
//                                return null;
//                            }
//
//                            @Override
//                            public HttpObject serverToProxyResponse(HttpObject httpObject) {
//                                try {
//                                    MyResponse myResponse = new MyResponse();
//                                    if (httpObject instanceof HttpResponse) {
//                                        myResponse.setHttpHeaders(((HttpResponse) httpObject).headers());
//                                    } else if (httpObject instanceof HttpContent) {
//                                        HttpHeaders httpHeaders = myResponse.getHttpHeaders();
//                                        if (httpHeaders != null) {
//                                            String contentType = httpHeaders.get("Content-Type");
//                                            if (contentType != null && !contentType.contains("image") && !contentType.contains("audio")
//                                                    && !contentType.contains("zip") && !contentType.contains("application/octet-stream")
//                                            ) {
//                                                ByteBuf buf = ((HttpContent) httpObject).content();
//                                                buf.markReaderIndex();
//                                                byte[] array = new byte[buf.readableBytes()];
//                                                buf.readBytes(array);
//                                                buf.resetReaderIndex();
//                                                myResponse.appendByte(array);
//                                            }
//                                        }
//                                    }
//                                    if (httpObject instanceof LastHttpContent) {
//                                        if (myResponse.getContent() != null) {
//                                            log.info(originalRequest.uri());
//                                            HttpHeaders httpHeaders = myResponse.getHttpHeaders();
//                                            myResponse.printHeader();
//                                            String ce = httpHeaders.get("Content-Encoding");
//                                            if (ce != null && ce.contains("gzip")) {
//                                                if (myResponse.getContent() != null) {
//                                                    ByteArrayInputStream bais = new ByteArrayInputStream(myResponse.getContent());
//                                                    GZIPInputStream gzis = new GZIPInputStream(bais);
//                                                    byte[] decompressedData = IOUtils.toByteArray(gzis);
//                                                    log.info(new String(decompressedData, StandardCharsets.UTF_8));
//                                                }
//                                            } else {
//                                                if (myResponse.getContent() != null) {
//                                                    log.info(new String(myResponse.getContent(), StandardCharsets.UTF_8));
//                                                }
//                                            }
//                                        }
//                                    }
//                                } catch (Exception e) {
//                                    log.error("代理服务器解析返回异常",e);
//                                }
//                                return httpObject;
//                            }
//
//
//                        };
//                    }
//                })
//
////                .withFiltersSource(new HttpFiltersSourceAdapter() {
////
////                    public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
////                        String uri = originalRequest.uri();
////                        if (originalRequest.method() == HttpMethod.CONNECT) {
////                            if (ctx != null) {
////                                String prefix = "https://" + uri.replaceFirst(":443$", "");
////                                ctx.channel().attr(CONNECTED_URL).set(prefix);
////                            }
////                            return new HttpFiltersAdapter(originalRequest, ctx);
////                        }
////                        String connectedUrl = ctx.channel().attr(CONNECTED_URL).get();
////                        if (connectedUrl == null) {
////                            return new MyHttpFilters(uri);
////                        }
////                        return new MyHttpFilters(connectedUrl + uri);
//
////                        return new HttpFiltersAdapter(originalRequest) {
////                            @Override
////                            public HttpObject serverToProxyResponse(HttpObject httpObject) {
////                                DecoderResult decoderResult = httpObject.decoderResult();
////                                log.info(decoderResult.toString());
////                                return httpObject;
////                            }
////                        };
////                    }
////                })
////                .withProxyAuthenticator(new ProxyAuthenticator() {
////                    @Override
////                    public boolean authenticate(String userName, String password) {
////                        return proxyUserName.equals(userName) && proxyPassword.equals(password);
////                    }
////
////                    @Override
////                    public String getRealm() {
////                        return "NiFi Unit Test";
////                    }
////                })
////                .withAllowLocalOnly(false)
//                // Use less threads to mitigate Gateway Timeout (504) with proxy test
//                .withThreadPoolConfiguration(new ThreadPoolConfiguration()
//                        .withAcceptorThreads(2)
//                        .withClientToProxyWorkerThreads(4)
//                        .withProxyToServerWorkerThreads(4))
//                .start();
//    }
//
////    private static void printHeaders(DefaultHttpRequest httpObject) {
////        final HttpHeaders headers = httpObject.headers();
////        final List<Map.Entry<String, String>> entries = headers.entries();
////
////        for (Map.Entry<String, String> entry : entries) {
////            final String value = entry.getValue();
////            log.info(entry.getKey() + " -> " + value);
////        }
////    }
//
////    public static class MyHttpFilters implements HttpFilters {
////        private final String uri;
////
////        MyHttpFilters(String uri) {
////            this.uri = uri;
////        }
////
////        @Override
////        public HttpResponse clientToProxyRequest(HttpObject httpObject) {
//////            ((DefaultHttpRequest) httpObject).setUri(uri);
//////            printHeaders((DefaultHttpRequest) httpObject);
////            return null;
////        }
////
////        //
////        @Override
////        public HttpResponse proxyToServerRequest(HttpObject httpObject) {
//////            ((DefaultHttpRequest) httpObject).setUri(uri);
//////            printHeaders((DefaultHttpRequest) httpObject);
////            return null;
////        }
////
////        @Override
////        public void proxyToServerRequestSending() {
////
////        }
////
////        @Override
////        public void proxyToServerRequestSent() {
////
////        }
////
////        @Override
////        public HttpObject serverToProxyResponse(HttpObject httpObject) {
////            return httpObject;
////        }
////
////        @Override
////        public void serverToProxyResponseTimedOut() {
////
////        }
////
////        @Override
////        public void serverToProxyResponseReceiving() {
////
////        }
////
////        @Override
////        public void serverToProxyResponseReceived() {
////
////        }
////
////        @Override
////        public HttpObject proxyToClientResponse(HttpObject httpObject) {
////            return httpObject;
////        }
////
////        @Override
////        public void proxyToServerConnectionQueued() {
////
////        }
////
////        @Override
////        public InetSocketAddress proxyToServerResolutionStarted(String resolvingServerHostAndPort) {
////            return null;
////        }
////
////        @Override
////        public void proxyToServerResolutionFailed(String hostAndPort) {
////
////        }
////
////        @Override
////        public void proxyToServerResolutionSucceeded(String serverHostAndPort, InetSocketAddress resolvedRemoteAddress) {
////
////        }
////
////        @Override
////        public void proxyToServerConnectionStarted() {
////
////        }
////
////        @Override
////        public void proxyToServerConnectionSSLHandshakeStarted() {
////
////        }
////
////        @Override
////        public void proxyToServerConnectionFailed() {
////
////        }
////
////        @Override
////        public void proxyToServerConnectionSucceeded(ChannelHandlerContext serverCtx) {
////
////        }
////    }
//
//
//}
