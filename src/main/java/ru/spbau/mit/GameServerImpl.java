package ru.spbau.mit;

import java.lang.reflect.InvocationTargetException;
import java.util.*;


public class GameServerImpl implements GameServer {

    private Game game;
    private HashMap<String, Connection> connectionMap;
    private int connectionsNumber;

    public GameServerImpl(String gameClassName, Properties properties) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        connectionsNumber = 0;
        connectionMap = new HashMap<>();
        Class <?> gameClass = Class.forName(gameClassName);
        game = (Game)gameClass.getConstructor(GameServer.class).newInstance(this);
        for (String propertyName : properties.stringPropertyNames()) {
            String methodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
            try {
                Integer number = Integer.parseInt(properties.getProperty(propertyName));
                gameClass.getMethod(methodName, Integer.class).invoke(game, number);
            } catch (NumberFormatException e) {
                gameClass.getMethod(methodName, String.class).invoke(game, properties.getProperty(propertyName));
            }
        }
    }

    @Override
    public void accept(final Connection connection) {
        final String id = Integer.toString(connectionsNumber++);
        synchronized (connectionMap) {
            connectionMap.put(id, connection);
        }
        connection.send(id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                game.onPlayerConnected(id);
                while (!connection.isClosed()) {
                    try {
                        synchronized (connection) {
                            if (!connection.isClosed()) {
                                String message = connection.receive(100);
                                game.onPlayerSentMsg(id, message);
                            }
                        }
                    } catch (InterruptedException e) {
                        //...
                    }
                }
            }
        }).start();
    }

    @Override
    public void broadcast(String message) {
        synchronized (connectionMap) {
            for (Connection connection : connectionMap.values()) {
                if (!connection.isClosed()) {
                    connection.send(message);
                }
            }
        }
    }

    @Override
    public void sendTo(String id, String message) {
        synchronized (connectionMap) {
            Connection connection = connectionMap.get(id);
            if (!connection.isClosed()) {
                connection.send(message);
            }
        }
    }
}
