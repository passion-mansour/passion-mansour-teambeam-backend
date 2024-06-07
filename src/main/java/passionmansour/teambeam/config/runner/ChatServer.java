package passionmansour.teambeam.config.runner;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

/*@Component
public class ChatServer {

    private final SocketIOServer server;

    public ChatServer(SocketIOServer server) {
        this.server = server;
    }

    @PostConstruct
    public void startServer() {
        server.start();
    }

    @PreDestroy
    public void stopServer() {
        server.stop();
    }
}*/
