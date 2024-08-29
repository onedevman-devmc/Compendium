package mc.compendium.database;

import mc.compendium.types.Trio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseManager {

    public static class ConnectionCloser {

        private final Connection connection;

        public ConnectionCloser(Connection connection) {
            this.connection = connection;
        }

        public void close() throws SQLException { this.connection.close(); }

    }

    //

    public enum System {
        MYSQL,
        SQLITE;

        public static final System DEFAULT = SQLITE;
    }

    public enum RequestType {
        UPDATE,
        QUERY;
    }

    //

    public static class Result {

        private final Trio<Integer, ResultSet, ConnectionCloser> trio;

        //

        private Result(Integer rows, ResultSet resultSet, ConnectionCloser closer) {
            trio = Trio.of(rows, resultSet, closer);
        }

        //

        public int rows() { return this.trio.one(); }

        public ResultSet set() { return this.trio.two(); }

        public ConnectionCloser closer() { return this.trio.three(); }

        //

        public static Result of(Integer rows, ResultSet resultSet, ConnectionCloser closer) {
            return new Result(rows, resultSet, closer);
        }

    }

    //

    public static String MYSQL_URL(String hostname, int port, String dbname) {
        return "jdbc:mysql://" + hostname + ":" + port + "/" + dbname;
    }

    public static String SQLITE_URL(String dbpath) {
        return "jdbc:sqlite:" + dbpath;
    }

    //

    public final System dbtype;

    private final String hostname;
    private final int port;

    private final String username;
    private final String password;

    private final String dbname;

    private final Path dbpath;

    //

    private DatabaseManager(System dbtype, String hostname, int port, String dbname, String username, String password, Path dbpath) {
        this.dbtype = dbtype;

        this.hostname = hostname;
        this.port = port;
        this.dbname = dbname;
        this.username = username;
        this.password = password;

        this.dbpath = dbpath;
    }

    //

    public String hostname() { return this.hostname; }

    public int port() { return this.port; }

    public String username() { return this.username; }

    public String password() { return this.password; }

    public String dbname() {return this.dbname; }

    public Path dbpath() { return this.dbpath; }

    //

    private synchronized Connection connect() throws SQLException {
        String url = "";

        if(this.dbtype.equals(System.MYSQL))
            url = DatabaseManager.MYSQL_URL(this.hostname(), this.port(), this.dbname());
        else if (this.dbtype.equals(System.SQLITE))
            url = DatabaseManager.SQLITE_URL(this.dbpath().toString());

        return DriverManager.getConnection(url);
    }

    //

    public synchronized Result execute(RequestType requestType, String request) throws SQLException {
        Connection connection = this.connect();
        Statement statement = connection.createStatement();

        int rows = -1;
        ResultSet result = null;
        try {
            if (requestType.equals(RequestType.UPDATE))
                rows = statement.executeUpdate(request);
            else if (requestType.equals(RequestType.QUERY))
                result = statement.executeQuery(request);
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return Result.of(rows, result, new ConnectionCloser(connection));
    }

    //

    public synchronized boolean tableExists(String table) throws SQLException {
        Connection connection = this.connect();

        ResultSet result = connection.getMetaData().getTables(null, null, table, null);

        connection.close();

        return result.next();
    }

    //
    public synchronized List<String> tables() throws SQLException {
        Connection connection = this.connect();

        ResultSet result = connection.getMetaData().getTables(null, null, "%", null);

        List<String> tables = new ArrayList<>();

        while(result.next())
            tables.add(result.getString("TABLE_NAME"));

        connection.close();

        return Collections.unmodifiableList(tables);
    }

    //

    public synchronized void createTable(String table, String inits) throws SQLException {
        createTable(table, inits, false);
    }

    public synchronized void createTable(String table, String inits, boolean erase) throws SQLException {
        if(this.tableExists(table) && erase)
            this.deleteTable(table);

        if(!this.tableExists(table) || erase)
            this.execute(RequestType.UPDATE, "CREATE TABLE " + table + " (" + inits + ")");
    }

    //

    public synchronized boolean deleteTable(String table) throws SQLException {
        if(!this.tableExists(table))
            return false;

        this.execute(RequestType.UPDATE, "DROP TABLE " + table + "");

        return true;
    }

    @Deprecated
    public synchronized boolean dropTable(String table) throws SQLException {
        return this.deleteTable(table);
    }

    //

    public synchronized List<Trio<String, String, Integer>> columns(String table) throws SQLException {
        Connection connection = this.connect();

        ResultSet result = connection.getMetaData().getColumns(null, null, table, null);

        List<Trio<String, String, Integer>> columns = new ArrayList<>();

        while(result.next())
            columns.add(Trio.of(result.getString("COLUMN_NAME"), result.getString("TYPE_NAME"), result.getInt("COLUMN_SIZE")));

        connection.close();

        return columns;
    }

    //

    public synchronized boolean columnExists(String table, String column) throws SQLException {
        List<Trio<String, String, Integer>> columns = this.columns(table);

        boolean columnFound = false;

        int i = 0;
        while(i < columns.size() && !columnFound) {
            Trio<String, String, Integer> columnIndex = columns.get(i);
            columnFound = columnIndex.one().equalsIgnoreCase(column);
            ++i;
        }

        return columnFound;
    }

    //

    public synchronized void addColumnIfNotExists(String table, String column, String type) throws SQLException {
        if(this.columnExists(table, column)) return;

        this.execute(
            RequestType.UPDATE,
            "ALTER TABLE " + table + " ADD " + column + " " + type
        ).closer().close();
    }

    //

    public static DatabaseManager forMySQL(String hostname, int port, String dbname, String username, String password) {
        return new DatabaseManager(System.MYSQL, hostname, port, dbname, username, password, null);
    }

    public static DatabaseManager forSQLite(String dbpath) throws IOException {
        return forSQLite(Path.of(dbpath));
    }

    public static DatabaseManager forSQLite(Path dbpath) throws IOException {
        if(!Files.exists(dbpath))
            Files.createFile(dbpath);

        return new DatabaseManager(System.SQLITE, null, 0, null, null, null, dbpath);
    }

}
