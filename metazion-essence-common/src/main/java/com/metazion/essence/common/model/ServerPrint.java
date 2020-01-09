package com.metazion.essence.common.model;

public class ServerPrint {

    private String requestUrl;
    private String requestUri;
    private String protocol;
    private String scheme;
    private String serverName;
    private int serverPort;
    private String contextPath;
    private String servletPath;
    private String localAddr;
    private String localName;
    private int localPort;
    private String remoteAddr;
    private String remoteHost;
    private int remotePort;
    private String servletRealPath;
    private String sessionServletRealPath;
    private String realIp;
    private String realPort;
    private String forwardedFor;
    private String forwardedProto;

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getServletPath() {
        return servletPath;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public String getLocalAddr() {
        return localAddr;
    }

    public void setLocalAddr(String localAddr) {
        this.localAddr = localAddr;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public String getServletRealPath() {
        return servletRealPath;
    }

    public void setServletRealPath(String servletRealPath) {
        this.servletRealPath = servletRealPath;
    }

    public String getSessionServletRealPath() {
        return sessionServletRealPath;
    }

    public void setSessionServletRealPath(String sessionServletRealPath) {
        this.sessionServletRealPath = sessionServletRealPath;
    }

    public String getRealIp() {
        return realIp;
    }

    public void setRealIp(String realIp) {
        this.realIp = realIp;
    }

    public String getRealPort() {
        return realPort;
    }

    public void setRealPort(String realPort) {
        this.realPort = realPort;
    }

    public String getForwardedFor() {
        return forwardedFor;
    }

    public void setForwardedFor(String forwardedFor) {
        this.forwardedFor = forwardedFor;
    }

    public String getForwardedProto() {
        return forwardedProto;
    }

    public void setForwardedProto(String forwardedProto) {
        this.forwardedProto = forwardedProto;
    }
}
