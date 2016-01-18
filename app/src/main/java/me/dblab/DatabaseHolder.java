package me.dblab;

import me.dblab.common.Database;

public class DatabaseHolder {
    private static Database database;

    public static void setDatabase(Database database) {
        DatabaseHolder.database = database;
    }

    public static Database getDatabase() {
        return database;
    }
}