/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author henri
 */
public class conection {
    public static void main(String[] args) throws SQLException {
        Connection conexao = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try {
                 conexao = DriverManager.getConnection("jdbc:mysql://localhost/sistemas","root","");
                ResultSet respClient = conexao.createStatement().executeQuery("SELECT * FROM CLIENTES");
                while (respClient.next()){
                    System.out.println("nome: "+respClient.getString("nome"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(conection.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(conection.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            if (conexao != null){
                conexao.close();
            }
        }
    }
}
