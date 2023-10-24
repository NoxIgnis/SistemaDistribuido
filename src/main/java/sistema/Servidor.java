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
import io.jsonwebtoken.Claims;

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
 int setPorta;

    public Servidor(int porta) {
         // Inicializa o objeto servidorView
         this.setPorta = porta;
    }
 public void iniciarServidor() throws IOException 
   { 
    ServerSocket serverSocket = null; 

    try { 
         serverSocket = new ServerSocket(setPorta); 
         System.out.println ("Connection Socket Created");
         try { 
              while (serverContinue)
                 {
                  serverSocket.setSoTimeout(10000);
                  try {
                       new Servidor (serverSocket.accept()); 
                      }
                  catch (SocketTimeoutException ste)
                      {
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
         System.err.println("Could not listen on port: "+setPorta); 
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
              System.err.println("Could not close port: "+setPorta); 
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
                System.out.println("Recebido do Cliente ->"+inputLine);
         
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode receivedJson = objectMapper.readValue(inputLine, ObjectNode.class);
                String action = receivedJson.get("action").asText();
                JsonNode dataNode = receivedJson.get("data");
                ObjectNode responseJson = objectMapper.createObjectNode();


             if(action.equals("cadastro-usuario")){
                try {
                    String token = dataNode.get("token").asText();
                    Claims claims = createJWT.verifyJwtToken(token);
                    System.out.println(claims);

                    String consultaInsercao;
                    String email = dataNode.get("email").asText();
                    String password = dataNode.get("password").asText();
                    String name = dataNode.get("name").asText();
                    int type = dataNode.get("type").asInt();

                    if (password != null && !password.isEmpty() && email != null && !email.isEmpty() && name != null && !name.isEmpty()) {
                        consultaInsercao = "INSERT INTO `usuarios`(`nome`, `senha`, `email`,`isAdmin`) VALUES ('"+ name +"','"+password+"','"+email+"','"+type+"')";

                         PreparedStatement preparedStatement;
                         preparedStatement = conexao.prepareStatement(consultaInsercao);

                       int linhasAfetadas = preparedStatement.executeUpdate();
                       if(linhasAfetadas == 1){
                                responseJson = objectMapper.createObjectNode();
                                responseJson.put("action", "cadastro-usuario");
                                responseJson.put("error", false);
                                responseJson.put("message", "Usuário cadastrado com sucesso!");


                       }else{
                                responseJson = objectMapper.createObjectNode();
                                responseJson.put("action", "cadastro-usuario");
                                responseJson.put("error",true);
                                responseJson.put("message", "erro ao cadastrar");

                       }
                    } else {
                        responseJson = objectMapper.createObjectNode();
                        responseJson.put("action", "cadastro-usuario");
                        responseJson.put("error",true);
                        responseJson.put("message", "todos os dados devem ser preenchidos");
                        
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
             } else if(action.equals("autocadastro-usuario")){
                try {

                    String consultaInsercao;
                    String email = dataNode.get("email").asText();
                    String password = dataNode.get("password").asText();
                    String name = dataNode.get("name").asText();

                    if (password != null && !password.isEmpty() && email != null && !email.isEmpty() && name != null && !name.isEmpty()) {
                        consultaInsercao = "INSERT INTO `usuarios`(`nome`, `senha`, `email`) VALUES ('"+ name +"','"+password+"','"+email+"')";

                         PreparedStatement preparedStatement;
                         preparedStatement = conexao.prepareStatement(consultaInsercao);

                       int linhasAfetadas = preparedStatement.executeUpdate();
                       if(linhasAfetadas == 1){
                                responseJson = objectMapper.createObjectNode();
                                responseJson.put("action", "autocadastro-usuario");
                                responseJson.put("error", false);
                                responseJson.put("message", "Usuário cadastrado com sucesso!");
                       }else{
                                responseJson = objectMapper.createObjectNode();
                                responseJson.put("action", "autocadastro-usuario");
                                responseJson.put("error",true);
                                responseJson.put("message", "erro ao cadastrar");
                       }
                    } else {
                        responseJson = objectMapper.createObjectNode();
                        responseJson.put("action", "autocadastro-usuario");
                        responseJson.put("error",true);
                        responseJson.put("message", "todos os dados devem ser preenchidos");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
             } else if(action.equals("login")){

                String email = dataNode.get("email").asText();
                String password = dataNode.get("password").asText();
                String token;

                if(password != null && !password.isEmpty() && email != null && !email.isEmpty()){
                    try {
                        String consultaSelecao = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";
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
                            
                            String userId = resultSet.getString("id");
                            boolean userAdmin = resultSet.getBoolean("isAdmin");
                            
                            token = createJWT.generateJwtToken(userId,userAdmin);

                            dataResponse.put("token", token);
                            responseJson.set("data", dataResponse);
                           
                        } else {
                            responseJson = objectMapper.createObjectNode();
                            responseJson.put("action", "login");
                            responseJson.put("error", true);
                            responseJson.put("message", "Usuario não encontrado");

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
             }else if(action.equals("logout")){
                responseJson = objectMapper.createObjectNode();
                responseJson.put("action", "logout");
                responseJson.put("error", false);
                responseJson.put("message", "logout efetuado com sucesso");
             }
                
                String jsonString=objectMapper.writeValueAsString(responseJson);
                System.out.println("Resposta para Cliente ->"+jsonString);
                out.println(jsonString);

              if (inputLine.equals("logout")) 
                  break; 

             } 
        Date dataAtual = new Date();

        SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
        String horaFormatada = formato.format(dataAtual);
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
