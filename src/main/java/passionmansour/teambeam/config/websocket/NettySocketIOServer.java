package passionmansour.teambeam.config.websocket;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import passionmansour.teambeam.controller.message.MessageHandler;

@org.springframework.context.annotation.Configuration
public class NettySocketIOServer {

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname("0.0.0.0");
        config.setPort(9092);

        return new SocketIOServer(config);
    }

    @Bean
    public CommandLineRunner runner(SocketIOServer server) {
        return args -> {
            server.start();
            Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        };
    }
}
