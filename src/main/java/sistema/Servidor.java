/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema;

/**
 *
 * @author henri
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;

import java.net.*; 
import java.io.*; 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor extends Thread
{ 
 protected static boolean serverContinue = true;
 protected Socket clientSocket;
 private static List<Socket> clientesConectados = new ArrayList<>();
 BufferedReader in;
 PrintWriter out;
// public createJWT jwt;


 public static void main(String[] args) throws IOException 
   { 
    ServerSocket serverSocket = null; 

    try { 
         serverSocket = new ServerSocket(10008); 
         System.out.println ("Connection Socket Created");
         try { 
              while (serverContinue)
                 {
                  serverSocket.setSoTimeout(10000);
                  System.out.println ("Waiting for Connection");
                  try {
                       new Servidor (serverSocket.accept()); 
                      }
                  catch (SocketTimeoutException ste)
                      {
                       System.out.println ("Timeout Occurred");
                      }
                 }
             } 
         catch (IOException e) 
             { 
              System.err.println("Accept failed."); 
              System.exit(1); 
             } 
        } 
    catch (IOException e) 
        { 
         System.err.println("Could not listen on port: 10008."); 
         System.exit(1); 
        } 
    finally
        {
         try {
              System.out.println ("Closing Server Connection Socket");
              serverSocket.close(); 
             }
         catch (IOException e)
             { 
              System.err.println("Could not close port: 10008."); 
              System.exit(1); 
             } 
        }
   }

 private Servidor (Socket clientSoc)
   {
    clientSocket = clientSoc;
    start();
   }

 @Override
 public void run()
   {
    System.out.println ("New Communication Thread Started");
    
    SocketAddress clientAddress = clientSocket.getRemoteSocketAddress();
                String clientIP = "Desconhecido";
                int clientPort = -1;

                if (clientAddress instanceof InetSocketAddress inetSocketAddress) {
                    clientIP = inetSocketAddress.getAddress().getHostAddress();
                    clientPort = inetSocketAddress.getPort();
                }

                System.out.println("Cliente conectado do IP: " + clientIP + ", Porta: " + clientPort);

                // Adicione o socket do cliente à lista de clientes conectados
                clientesConectados.add(clientSocket);
                
    try { 
         out = new PrintWriter(clientSocket.getOutputStream(),true); 
         in = new BufferedReader(new InputStreamReader( clientSocket.getInputStream())); 

         String inputLine; 

         Connection conexao = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexao = DriverManager.getConnection("jdbc:mysql://localhost/sistemas", "root", "");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
         
         while ((inputLine = in.readLine())!=null) 
            { 
                //inputLine = in.readLine(); 
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode receivedJson = objectMapper.readValue(inputLine, ObjectNode.class);
                String action = receivedJson.get("action").asText();
                JsonNode dataNode = receivedJson.get("data");
                ObjectNode responseJson = objectMapper.createObjectNode();

//                String[] valores = inputLine.split(",");
//                String response ="";
              
             if(action.equals("register")){
                try {
                    String consultaInsercao;
                    String email = dataNode.get("email").asText();
                    String password = dataNode.get("password").asText();
                    String name = dataNode.get("name").asText();
                    System.out.println(email);
                    System.out.println(name);
                    if (password != null && !password.isEmpty() && email != null && !email.isEmpty() && name != null && !name.isEmpty()) {
//                    String token = "";
                        consultaInsercao = "INSERT INTO `usuarios`(`nome`, `senha`, `email`) VALUES ('"+ name +"','"+password+"','"+email+"')";

                       // Crie um PreparedStatement
                         PreparedStatement preparedStatement;
                         preparedStatement = conexao.prepareStatement(consultaInsercao);

                       // Execute a instrução de inserção
                       int linhasAfetadas = preparedStatement.executeUpdate();
                       if(linhasAfetadas == 1){
                                responseJson = objectMapper.createObjectNode();
                                responseJson.put("action", "register");
                                responseJson.put("error", false);
                                responseJson.put("message", "cadastrado com sucesso");
                                ObjectNode dataResponse = objectMapper.createObjectNode();
//                              dataResponse.put("token", "dka3i92fjsi4j9f29jf92j");
                                responseJson.set("data", dataResponse);
    //                            objectMapper.writeValue(out, responseJson);
    //                         response = "sucess";
                       }else{
                                responseJson = objectMapper.createObjectNode();
                                responseJson.put("action", "register");
                                responseJson.put("error",true);
                                responseJson.put("message", "erro ao cadastrar");
                                ObjectNode dataResponse = objectMapper.createObjectNode();
//                              dataResponse.put("token", "dka3i92fjsi4j9f29jf92j");
                                responseJson.set("data", dataResponse);
    //                            objectMapper.writeValue(out, responseJson);
                       }
                        //ResultSet respClient = conexao.createStatement().executeUpdate();
                    } else {
                        responseJson = objectMapper.createObjectNode();
                        responseJson.put("action", "register");
                        responseJson.put("error",true);
                        responseJson.put("message", "todos os dados devem ser preenchidos");
                        ObjectNode dataResponse = objectMapper.createObjectNode();
//                       dataResponse.put("token", "dka3i92fjsi4j9f29jf92j");
                        responseJson.set("data", dataResponse);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
             } else if(action.equals("login")){
                 
                String email = dataNode.get("email").asText();
                String password = dataNode.get("password").asText();
                String consultaSelecao = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";

                if(password != null && !password.isEmpty() && email != null && !email.isEmpty()){
                    try {
                        PreparedStatement preparedStatement;
                        preparedStatement = conexao.prepareStatement(consultaSelecao);

                        preparedStatement.setString(1, email);
                        preparedStatement.setString(2, password);
                        ResultSet resultSet = preparedStatement.executeQuery();

                        if (resultSet.next()) {
                            // O usuário foi encontrado
                            responseJson = objectMapper.createObjectNode();
                            responseJson.put("action", "login");
                            responseJson.put("error", false);
                            responseJson.put("message", "logado com sucesso");
                            ObjectNode dataResponse = objectMapper.createObjectNode();
                            
                            int userId = resultSet.getInt("id");
                            String userIdStr = Integer.toString(userId);
                            int userAdmin = resultSet.getInt("isAdmin");
                            boolean isAdmin = false;
                            isAdmin = userAdmin == 1;
                            
                            String token = createJWT.generateJwtToken(userIdStr,isAdmin);
                            
                            dataResponse.put("token", token);
                            responseJson.set("data", dataResponse);
//                            objectMapper.writeValue(out, responseJson);

//                            if(resultSet.getInt("tipo") == 1){
//                                
////                                response = "admin";
//                            } else {
////                                response = "sucess";
//                            }
                        } else {
                            responseJson = objectMapper.createObjectNode();
                            responseJson.put("action", "login");
                            responseJson.put("error", true);
                            responseJson.put("message", "Usuario não encontrado");
                            ObjectNode dataResponse = objectMapper.createObjectNode();
                            dataResponse.put("token", "dka3i92fjsi4j9f29jf92j");
                            responseJson.set("data", dataResponse);
//                            objectMapper.writeValue(out, responseJson);

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        // Lide com a exceção apropriadamente
                    }
                }
             }else if(action.equals("logout")){
                responseJson = objectMapper.createObjectNode();
                responseJson.put("action", "logout");
                responseJson.put("error", false);
                responseJson.put("message", "logout efetuado com sucesso");
             }
                
//                objectMapper.writeValue(out, responseJson);
                String jsonString=objectMapper.writeValueAsString(responseJson);
                out.println(jsonString);
                System.out.println("morte 12");
//              out.println(  response); 

              if (inputLine.equals("logout")) 
                  break; 
//
//              if (inputLine.equals("End Server.")) 
//                  serverContinue = false; 

             } 
        Date dataAtual = new Date();

        // Define o formato desejado para a hora
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");

        // Formata a data atual no formato desejado
        String horaFormatada = formato.format(dataAtual);

        // Exibe a hora formatada
        System.out.println("Hora atual: " + horaFormatada);
         System.out.println("Cliente conectado saiu");
         out.close(); 
         in.close(); 
         if (conexao != null){
            try {
                conexao.close();
            } catch (SQLException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
//         clientSocket.close(); 
        } 
    catch (IOException e) 
        { 
         System.err.println("Problem with Communication Server");
         System.exit(1); 
        } 
    }
 public static List<Socket> getClientesConectados() {
    return clientesConectados;
}

} 
