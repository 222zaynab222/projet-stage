package com.example.servercheck;

import java.sql.*; //Connection,DriverManager,SQLException



public class ConnectionDBB {
    private static String URL = "jdbc:mysql://localhost:8889/stage";
    private static String USER = "root";
    private static String PASSWORD = "root";

    public static  Connection getConnection() throws SQLException {
        try{
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }

    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connexion fermée avec succès");
            } catch (SQLException e) {
                System.out.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }


}

