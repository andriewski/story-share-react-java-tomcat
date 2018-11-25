package websocket;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import dto.MessageDTO;
import entites.Message;
import org.apache.log4j.Logger;
import services.MessageService;
import services.impl.MessageServiceImpl;

public class WebSocketManager implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(8181);
        final SocketIOServer server = new SocketIOServer(config);
        final Logger logger = Logger.getLogger(WebSocketManager.class);
        MessageService messageService = MessageServiceImpl.getInstance();

        try {
            server.addEventListener("join", String.class, new DataListener<String>() {
                @Override
                public void onData(SocketIOClient socketIOClient, String roomNumber, AckRequest ackRequest) {
                    System.out.println(socketIOClient.getSessionId() + " <join room number> " + roomNumber);

                    socketIOClient.joinRoom(roomNumber);
                    System.out.println(socketIOClient.getAllRooms());
                }
            });

            server.addEventListener("leave", String.class, new DataListener<String>() {
                @Override
                public void onData(SocketIOClient socketIOClient, String roomNumber, AckRequest ackRequest) {

                    System.out.println(socketIOClient.getSessionId() + " <leave room number>" + roomNumber);
                    socketIOClient.leaveRoom(roomNumber);
                }
            });

            server.addEventListener("sendMessage", MessageDTO.class, new DataListener<MessageDTO>() {
                public void onData(SocketIOClient client, MessageDTO data, AckRequest ackRequest) {
                    System.out.println(data);

                    server.getRoomOperations(getChatRoomNumber(data)).sendEvent("sendMessage", data);

                    server.getRoomOperations(String.valueOf(data.getReceiverID())).sendEvent("sendMessage", data);
                    server.getRoomOperations(String.valueOf(data.getSenderID())).sendEvent("sendMessage", data);

                    messageService.save(new Message(data.getText(), data.getDate(), data.getSenderID(),
                            data.getReceiverID()));
                }
            });

            server.start();
        } catch (Exception e) {
            logger.fatal(e);
        }
    }


    public void contextDestroyed(ServletContextEvent sce) {
        //Nothing there
    }

    private String getChatRoomNumber(MessageDTO data) {
        return data.getSenderID() < data.getReceiverID()
                ? data.getSenderID() + "@" + data.getReceiverID()
                : data.getReceiverID() + "@" + data.getSenderID();
    }
}
