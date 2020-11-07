package doom;

import doom.server.DoomServer;

class rough {
    public static void main(String[] args) {
        DoomServer server = new DoomServer();
        server.addGlobalMiddleWare(new LogMiddleware());
        server.start();
    }
}
