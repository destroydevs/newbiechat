package ru.spigotmc.destroy.newbiechat.database;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.spigotmc.destroy.newbiechat.NewbieChat;
import ru.spigotmc.destroy.newbiechat.util.ClassLoader;

import java.sql.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Database {
    private static Connection connection;
    private static final Map<String, Data> playerData = new HashMap<>();

    public Database() {
        if (!isConnected())
            initialize(DataType.valueOf(NewbieChat.config().getString("connection.type").toUpperCase(Locale.ENGLISH)));
    }

    public void initialize(DataType type) {
        try {
            new ClassLoader().loadDriver(type);
            if (type == DataType.MYSQL) {
                String url = "jdbc:mysql://" + NewbieChat.config().getString("connection.mysql.url") + "/" + NewbieChat.config().getString("connection.mysql.name");
                String username = NewbieChat.config().getString("connection.mysql.username");
                String password = NewbieChat.config().getString("connection.mysql.password");
                connection = DriverManager.getConnection(url, username, password);
            } else if (type == DataType.H2) {
                connection = DriverManager.getConnection("jdbc:h2:./plugins/NewbieChat/database/data");
            } else {
                Bukkit.getLogger().severe("Database not loaded, check configuration!");
                connection = null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (isConnected()) {
            connect();
            loadCooldowns();
            startSaveTask();
            startChecks();
        }
    }

    public boolean isConnected() {
        try {
            return (connection != null && !connection.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void loadCooldowns() {
        try {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM cooldowns")) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        String name = rs.getString("name");
                        String data = rs.getString("data");
                        playerData.put(name, new Gson().fromJson(data, Data.class));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private static void startSaveTask() {
        Executors.
                newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(
                        Database::saveCooldowns,
                        1,
                        1,
                        TimeUnit.MINUTES
                );
    }

    public static void saveCooldowns() {
        String selectSql = "SELECT data FROM cooldowns WHERE name = ?";
        String updateSql = "UPDATE cooldowns SET data = ? WHERE name = ?";
        String insertSql = "INSERT INTO cooldowns (name, data) VALUES (?, ?)";

        try {
            for (Map.Entry<String, Data> entry : playerData.entrySet()) {
                String name = entry.getKey();
                Data data = entry.getValue();
                String jsonData = new Gson().toJson(data);

                try (PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {
                    selectStmt.setString(1, name);
                    ResultSet rs = selectStmt.executeQuery();
                    if (rs.next()) {
                        try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                            updateStmt.setString(1, jsonData);
                            updateStmt.setString(2, name);
                            updateStmt.executeUpdate();
                        }
                    } else {
                        try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                            insertStmt.setString(1, name);
                            insertStmt.setString(2, jsonData);
                            insertStmt.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            Bukkit.getLogger().warning(e.getMessage());
        }
    }

    public void connect() {
        String sql = "CREATE TABLE IF NOT EXISTS cooldowns" +
                "(id NUMBER PRIMARY KEY AUTO_INCREMENT," +
                "name TEXT," +
                "data TEXT);";
        executeUpdate(sql);
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setBlock(String p, int time) {
        playerData.put(p, new Data(time, isBlocked(p)));
    }

    public boolean isBlocked(String p) {
        return playerData.getOrDefault(p, new Data(0, false)).isBlocked();
    }

    public int getTime(String p) {
        return playerData.getOrDefault(p, new Data(0, false)).getTime();
    }

    public boolean firstJoin(String p) {
        return !playerData.containsKey(p);
    }

    public void startChecks() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            Bukkit.getScheduler().runTask(NewbieChat.getPlugin(NewbieChat.class), () -> {
                for (Map.Entry<String, Data> ps : playerData.entrySet()) {
                    Player p = Bukkit.getPlayer(ps.getKey());
                    if (p == null) continue;
                    if (isBlocked(ps.getKey())) {
                        int time = getTime(ps.getKey());
                        if (time > 0) {
                            playerData.put(ps.getKey(), new Data(ps.getValue().getTime() - 1, ps.getValue().isBlocked()));
                            continue;
                        }
                        playerData.put(ps.getKey(), new Data(0, false));
                    }
                }
            });
        }, 1, 1, TimeUnit.SECONDS);
    }

    private static void executeUpdate(String url) {
        try {
            try (PreparedStatement statement = connection.prepareStatement(url)) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
