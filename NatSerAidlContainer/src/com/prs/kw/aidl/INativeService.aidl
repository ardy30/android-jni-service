// INativeService.aidl
package com.prs.kw.aidl;

interface INativeService {
    boolean startServer(int port);
    boolean stopServer();
}