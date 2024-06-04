package passionmansour.teambeam.service.message.redis;

/*@Service
@Slf4j
public class RedisMessageListener implements MessageListener {

    private final SimpMessagingTemplate template;

    public RedisMessageListener(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("String Message received: {}", message.toString());
        String channel = new String(pattern);
        String messageBody = new String(message.getBody());
        // 메시지를 클라이언트에게 전달
        template.convertAndSend("/topic/" + channel, messageBody);
    }
}*/
