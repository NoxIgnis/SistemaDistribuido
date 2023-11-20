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
import com.fasterxml.jackson.databind.node.ArrayNode;
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

                    String consultaInsercao;
                    String email = dataNode.get("email").asText();
                    String password = dataNode.get("password").asText();
                    String name = dataNode.get("name").asText();
//                    int type = dataNode.get("type").asInt();
                    int type = dataNode.get("type").asText() == "admin" ? 1: 0;

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
                
             }else if(action.equals("listar-usuarios")){
                 
                    String consultaSelecao = "SELECT DISTINCT * FROM usuarios";
                    PreparedStatement preparedStatement;
                    preparedStatement = conexao.prepareStatement(consultaSelecao);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        responseJson.put("action", "listar-usuarios");
                        responseJson.put("error", false);
                        responseJson.put("message", "Sucesso");

                        ArrayNode usersArray = objectMapper.createArrayNode();

                        do {
                            ObjectNode userNode = objectMapper.createObjectNode();
                            userNode.put("id", resultSet.getInt("id"));
                            userNode.put("name", resultSet.getString("nome"));
                            String type = resultSet.getBoolean("isAdmin") ? "admin": "user";
                            userNode.put("type", type);
                            userNode.put("email", resultSet.getString("email"));
                            usersArray.add(userNode);
                        } while (resultSet.next());

                        ObjectNode dataResponse = objectMapper.createObjectNode();
                        dataResponse.set("users", usersArray);

                        responseJson.set("data", dataResponse);
                    } else {
                        responseJson.put("action", "listar-usuarios");
                        responseJson.put("error", true);
                        responseJson.put("message", "Nenhum usuário encontrado");
                    }
                    // Agora responseJson contém o JSON no formato desejado com os dados dos usuários.
             }else if(action.equals("pedido-edicao-usuario")){
             
                String id = dataNode.get("user_id").asText();
                String token = dataNode.get("token").asText();

                if(id != null && !id.isEmpty()){
                    try {
                        String consultaSelecao = "SELECT * FROM usuarios WHERE id = ?";
                        PreparedStatement preparedStatement;
                        preparedStatement = conexao.prepareStatement(consultaSelecao);

                        preparedStatement.setString(1, id);
                        ResultSet resultSet = preparedStatement.executeQuery();

                        if (resultSet.next()) {
                            // O usuário foi encontrado
                            responseJson = objectMapper.createObjectNode();
                            responseJson.put("action", "pedido-edicao-usuario");
                            responseJson.put("error", false);
                            responseJson.put("message", "Sucesso");
                            ObjectNode dataResponse = objectMapper.createObjectNode();
                            ObjectNode userNode = objectMapper.createObjectNode();
                            userNode.put("id", resultSet.getInt("id"));
                            userNode.put("name", resultSet.getString("nome"));
                            String type = resultSet.getBoolean("isAdmin") ? "admin": "user";
                            userNode.put("type", type);
                            userNode.put("email", resultSet.getString("email"));
                            dataResponse.set("user", userNode); // Definir o nó "user" em "data"

                            responseJson.set("data", dataResponse);
                           
                        } else {
                            responseJson = objectMapper.createObjectNode();
                            responseJson.put("action", "pedido-edicao-usuario");
                            responseJson.put("error", true);
                            responseJson.put("message", "Usuario não encontrado");

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
             }else if (action.equals("edicao-usuario")){
                String consultaAtualizacao;           
                String email = dataNode.get("email").asText();
//                boolean isAdmin = dataNode.get("type").asBoolean();
                boolean isAdmin = dataNode.get("type").asText() == "admin" ? true: false;
                String user_id = dataNode.get("user_id").asText();
                String password = dataNode.get("password").asText();
                String name = dataNode.get("name").asText();

                if (user_id != null && !user_id.isEmpty() && email != null && !email.isEmpty() && name != null && !name.isEmpty()) {
                    consultaAtualizacao = "UPDATE `usuarios` SET `nome` = ?, `email` = ?, isAdmin = ? ,senha = ? WHERE `id` = ?";

                    PreparedStatement preparedStatement;
                    preparedStatement = conexao.prepareStatement(consultaAtualizacao);

                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, email);
                    preparedStatement.setBoolean(3, isAdmin);
                    preparedStatement.setString(4, password);
                    preparedStatement.setString(5, user_id);

                    int linhasAfetadas = preparedStatement.executeUpdate();
                    if (linhasAfetadas == 1) {
                        responseJson = objectMapper.createObjectNode();
                        responseJson.put("action", "edicao-usuario");
                        responseJson.put("error", false);
                        responseJson.put("message", "Usuário atualizado com sucesso!");
                    } else {
                        responseJson = objectMapper.createObjectNode();
                        responseJson.put("action", "edicao-usuario");
                        responseJson.put("error", true);
                        responseJson.put("message", "Erro ao atualizar o usuário. Verifique os dados fornecidos.");
                    }
                } else {
                    responseJson = objectMapper.createObjectNode();
                    responseJson.put("action", "edicao-usuario");
                    responseJson.put("error", true);
                    responseJson.put("message", "Todos os campos devem ser preenchidos para a atualização.");
                }

             }else if (action.equals("excluir-usuario")){
                 String consultaDelete;
                String user_id = dataNode.get("user_id").asText();
                if (user_id != null && !user_id.isEmpty()) {
                    consultaDelete = "DELETE FROM `usuarios` WHERE `id` = ?";

                    PreparedStatement preparedStatement;
                    preparedStatement = conexao.prepareStatement(consultaDelete);

                    preparedStatement.setString(1, user_id);

                    int linhasAfetadas = preparedStatement.executeUpdate();
                    if (linhasAfetadas == 1) {
                        responseJson = objectMapper.createObjectNode();
                        responseJson.put("action", "excluir-usuario");
                        responseJson.put("error", false);
                        responseJson.put("message", "Usuário Excluido com sucesso!");
                    } else {
                        responseJson = objectMapper.createObjectNode();
                        responseJson.put("action", "excluir-usuario");
                        responseJson.put("error", true);
                        responseJson.put("message", "Erro ao Excluir o usuário.");
                    }
                } else {
                    responseJson = objectMapper.createObjectNode();
                    responseJson.put("action", "excluir-usuario");
                    responseJson.put("error", true);
                    responseJson.put("message", "id deve ser preenchido para a EXCLUSÃO.");
                }
             }else if (action.equals("excluir-proprio-usuario")){
                 String consultaDelete;
                String email = dataNode.get("email").asText();
                String password = dataNode.get("password").asText();
                if (email != null && !email.isEmpty() && password != null && !password.isEmpty() ) {
                    consultaDelete = "DELETE FROM `usuarios` WHERE `email` = ? AND `senha` = ?";

                    PreparedStatement preparedStatement;
                    preparedStatement = conexao.prepareStatement(consultaDelete);

                    preparedStatement.setString(1, email);
                    preparedStatement.setString(2, password);

                    int linhasAfetadas = preparedStatement.executeUpdate();
                    if (linhasAfetadas == 1) {
                        responseJson = objectMapper.createObjectNode();
                        responseJson.put("action", "excluir-proprio-usuario");
                        responseJson.put("error", false);
                        responseJson.put("message", "Usuário Excluido com sucesso!");
                    } else {
                        responseJson = objectMapper.createObjectNode();
                        responseJson.put("action", "excluir-proprio-usuario");
                        responseJson.put("error", true);
                        responseJson.put("message", "Erro ao Excluir o usuário.");
                    }
                } else {
                    responseJson = objectMapper.createObjectNode();
                    responseJson.put("action", "excluir-proprio-usuario");
                    responseJson.put("error", true);
                    responseJson.put("message", "Erro ao realizar EXCLUSÃO.");
                }
             }else if(action.equals("pedido-proprio-usuario")){
                 
                String token = dataNode.get("token").asText();
                if(token != null && !token.isEmpty()){
                    Claims claims = createJWT.verifyJwtToken(token);
                    try {
                        String consultaSelecao = "SELECT * FROM usuarios WHERE id = ?";
                        PreparedStatement preparedStatement;
                        preparedStatement = conexao.prepareStatement(consultaSelecao);

                        preparedStatement.setString(1, claims.get("user_id", String.class));
                        ResultSet resultSet = preparedStatement.executeQuery();

                        if (resultSet.next()) {
                            // O usuário foi encontrado
                            responseJson = objectMapper.createObjectNode();
                            responseJson.put("action", "pedido-proprio-usuario");
                            responseJson.put("error", false);
                            responseJson.put("message", "Sucesso");
                            ObjectNode dataResponse = objectMapper.createObjectNode();
                            ObjectNode userNode = objectMapper.createObjectNode();
                            userNode.put("id", resultSet.getInt("id"));
                            userNode.put("name", resultSet.getString("nome"));
//                            userNode.put("type", resultSet.getBoolean("isAdmin"));
                            String isAdmin = resultSet.getBoolean("isAdmin") ? "admin": "user";
                            userNode.put("type", isAdmin);
                            userNode.put("email", resultSet.getString("email"));
                            dataResponse.set("user", userNode); // Definir o nó "user" em "data"
                            responseJson.set("data", dataResponse);
                           
                        } else {
                            responseJson = objectMapper.createObjectNode();
                            responseJson.put("action", "pedido-proprio-usuario");
                            responseJson.put("error", true);
                            responseJson.put("message", "Usuario não encontrado");

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }       
                }
             }else if (action.equals("autoedicao-usuario")){
                String consultaAtualizacao;
                String email = dataNode.get("email").asText();
                String user_id = dataNode.get("id").asText();
                String name = dataNode.get("name").asText();
                String password = dataNode.get("password").asText();

                if (email != null && !email.isEmpty() && name != null && !name.isEmpty()) {
                    consultaAtualizacao = "UPDATE `usuarios` SET `nome` = ?, `email` = ? , senha = ? WHERE `id` = ?";

                    PreparedStatement preparedStatement;
                    preparedStatement = conexao.prepareStatement(consultaAtualizacao);

                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, email);
                    preparedStatement.setString(3, password);

                    preparedStatement.setString(4, user_id);

                    int linhasAfetadas = preparedStatement.executeUpdate();
                    if (linhasAfetadas == 1) {
                        responseJson = objectMapper.createObjectNode();
                        responseJson.put("action", "autoedicao-usuario");
                        responseJson.put("error", false);
                        responseJson.put("message", "Usuário atualizado com sucesso!");
                    } else {
                        responseJson = objectMapper.createObjectNode();
                        responseJson.put("action", "autoedicao-usuario");
                        responseJson.put("error", true);
                        responseJson.put("message", "Erro ao atualizar o usuário. Verifique os dados fornecidos.");
                    }
                } else {
                    responseJson = objectMapper.createObjectNode();
                    responseJson.put("action", "autoedicao-usuario");
                    responseJson.put("error", true);
                    responseJson.put("message", "Todos os campos devem ser preenchidos para a atualização.");
                }

             }else if (action.equals("cadastro-ponto")){
                try {
                    String consultaInsercao;
                    String obs = dataNode.get("obs").asText();
                    String name = dataNode.get("name").asText();

                    if (obs != null && !obs.isEmpty() && name != null && !name.isEmpty()) {
                        consultaInsercao = "INSERT INTO `pontos`(`nome`, `observacao`) VALUES ('"+ name +"','"+obs+"')";
                         PreparedStatement preparedStatement;
                         preparedStatement = conexao.prepareStatement(consultaInsercao);

                       int linhasAfetadas = preparedStatement.executeUpdate();
                       if(linhasAfetadas == 1){
                                responseJson = objectMapper.createObjectNode();
                                responseJson.put("action", "cadastro-ponto");
                                responseJson.put("error", false);
                                responseJson.put("message", "Ponto cadastrado com sucesso!");
                       }else{
                                responseJson = objectMapper.createObjectNode();
                                responseJson.put("action", "cadastro-ponto");
                                responseJson.put("error",true);
                                responseJson.put("message", "erro ao cadastrar");
                       }
                    } else {
                        responseJson = objectMapper.createObjectNode();
                        responseJson.put("action", "cadastro-pontolis");
                        responseJson.put("error",true);
                        responseJson.put("message", "todos os dados devem ser preenchidos");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
             }else if(action.equals("listar-pontos")){
                 
                    String consultaSelecao = "SELECT DISTINCT * FROM pontos";
                    PreparedStatement preparedStatement;
                    preparedStatement = conexao.prepareStatement(consultaSelecao);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        responseJson.put("action", "listar-pontos");
                        responseJson.put("error", false);
                        responseJson.put("message", "Sucesso");

                        ArrayNode usersArray = objectMapper.createArrayNode();

                        do {
                            ObjectNode userNode = objectMapper.createObjectNode();
                            userNode.put("id", resultSet.getInt("id"));
                            userNode.put("name", resultSet.getString("nome"));

                            userNode.put("obs", resultSet.getString("observacao"));
                            usersArray.add(userNode);
                        } while (resultSet.next());

                        ObjectNode dataResponse = objectMapper.createObjectNode();
                        dataResponse.set("pontos", usersArray);

                        responseJson.set("data", dataResponse);
                    } else {
                        responseJson.put("action", "listar-pontos");
                        responseJson.put("error", true);
                        responseJson.put("message", "Nenhum usuário encontrado");
                    }
                    // Agora responseJson contém o JSON no formato desejado com os dados dos usuários.
             }else if (action.equals("excluir-ponto")){
                 String consultaDelete;
                String id = dataNode.get("ponto_id").asText();
                if (id != null && !id.isEmpty()) {
                    consultaDelete = "DELETE FROM `pontos` WHERE `id` = ? ";

                    PreparedStatement preparedStatement;
                    preparedStatement = conexao.prepareStatement(consultaDelete);

                    preparedStatement.setString(1, id);

                    int linhasAfetadas = preparedStatement.executeUpdate();
                    if (linhasAfetadas == 1) {
                        responseJson = objectMapper.createObjectNode();
                        responseJson.put("action", "excluir-ponto");
                        responseJson.put("error", false);
                        responseJson.put("message", "Ponto Excluido com sucesso!");
                    } else {
                        responseJson = objectMapper.createObjectNode();
                        responseJson.put("action", "excluir-ponto");
                        responseJson.put("error", true);
                        responseJson.put("message", "Erro ao Excluir o Ponto.");
                    }
                } else {
                    responseJson = objectMapper.createObjectNode();
                    responseJson.put("action", "excluir-ponto");
                    responseJson.put("error", true);
                    responseJson.put("message", "Erro ao realizar EXCLUSÃO.");
                }
             }else if (action.equals("edicao-ponto")){
                String consultaAtualizacao;           
                String name = dataNode.get("name").asText();
                String ponto_id = dataNode.get("ponto_id").asText();
                String obs = dataNode.get("obs").asText();

                if (obs != null && !obs.isEmpty() && name != null && !name.isEmpty()) {
                    consultaAtualizacao = "UPDATE `pontos` SET `nome` = ?, `observacao` = ? WHERE `id` = ?";

                    PreparedStatement preparedStatement;
                    preparedStatement = conexao.prepareStatement(consultaAtualizacao);

                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, obs);
                    preparedStatement.setString(3, ponto_id);

                    int linhasAfetadas = preparedStatement.executeUpdate();
                    if (linhasAfetadas == 1) {
                        responseJson = objectMapper.createObjectNode();
                        responseJson.put("action", "edicao-ponto");
                        responseJson.put("error", false);
                        responseJson.put("message", "Ponto atualizado com sucesso!");
                    } else {
                        responseJson = objectMapper.createObjectNode();
                        responseJson.put("action", "edicao-ponto");
                        responseJson.put("error", true);
                        responseJson.put("message", "Erro ao atualizar o Ponto. Verifique os dados fornecidos.");
                    }
                } else {
                    responseJson = objectMapper.createObjectNode();
                    responseJson.put("action", "edicao-ponto");
                    responseJson.put("error", true);
                    responseJson.put("message", "Todos os campos devem ser preenchidos para a atualização.");
                }

             }else if(action.equals("pedido-edicao-ponto")){
                 
                String token = dataNode.get("token").asText();
                String id = dataNode.get("ponto_id").asText();

                if(token != null && !token.isEmpty() && id !=null && !id.isEmpty()){
                    Claims claims = createJWT.verifyJwtToken(token);
                    try {
                        String consultaSelecao = "SELECT * FROM pontos WHERE id = ?";
                        PreparedStatement preparedStatement;
                        preparedStatement = conexao.prepareStatement(consultaSelecao);

                        preparedStatement.setString(1, id);
                        ResultSet resultSet = preparedStatement.executeQuery();

                        if (resultSet.next()) {
                            // O usuário foi encontrado
                            responseJson = objectMapper.createObjectNode();
                            responseJson.put("action", "pedido-edicao-ponto");
                            responseJson.put("error", false);
                            responseJson.put("message", "Sucesso");
                            ObjectNode dataResponse = objectMapper.createObjectNode();
                            ObjectNode pontoNode = objectMapper.createObjectNode();
                            pontoNode.put("id", resultSet.getInt("id"));
                            pontoNode.put("name", resultSet.getString("nome"));                          
                            pontoNode.put("obs", resultSet.getString("observacao"));
                            dataResponse.set("ponto", pontoNode); // Definir o nó "user" em "data"
                            responseJson.set("data", dataResponse);
                           
                        } else {
                            responseJson = objectMapper.createObjectNode();
                            responseJson.put("action", "pedido-edicao-ponto");
                            responseJson.put("error", true);
                            responseJson.put("message", "Usuario não encontrado");

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }       
                }
             }else if (action.equals("cadastro-segmento")){
                try {
                    String consultaInsercao;
                    JsonNode segmentoNode = dataNode.get("segmento");
                    String pontoOrigemId = segmentoNode.get("ponto_origem").get("id").asText();
                    String pontoDestinoId = segmentoNode.get("ponto_destino").get("id").asText();

                    String obs = segmentoNode.get("obs").asText();
                    String direcao = segmentoNode.get("direcao").asText();
                    String distancia = segmentoNode.get("distancia").asText();


                    if (obs != null && !obs.isEmpty() && pontoOrigemId != null && !pontoOrigemId.isEmpty()&& pontoDestinoId != null && !pontoDestinoId.isEmpty() && distancia != null && !distancia.isEmpty()&& direcao != null && !direcao.isEmpty() && direcao != null && !direcao.isEmpty()) {
                        consultaInsercao = "INSERT INTO `Segmentos`(`origem`, `destino`,`distancia`,`direcao`,`observacao`) VALUES ('"+ pontoOrigemId +"','"+pontoDestinoId+"','"+distancia+"','"+direcao+"','"+obs+"')";
                         PreparedStatement preparedStatement;
                         preparedStatement = conexao.prepareStatement(consultaInsercao);

                       int linhasAfetadas = preparedStatement.executeUpdate();
                       if(linhasAfetadas == 1){
                                responseJson = objectMapper.createObjectNode();
                                responseJson.put("action", "cadastro-segmento");
                                responseJson.put("error", false);
                                responseJson.put("message", "segmento cadastrado com sucesso!");
                       }else{
                                responseJson = objectMapper.createObjectNode();
                                responseJson.put("action", "cadastro-segmento");
                                responseJson.put("error",true);
                                responseJson.put("message", "erro ao cadastrar");
                       }
                    } else {
                        responseJson = objectMapper.createObjectNode();
                        responseJson.put("action", "cadastro-segmento");
                        responseJson.put("error",true);
                        responseJson.put("message", "todos os dados devem ser preenchidos");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
             }else if(action.equals("listar-segmentos")){
                 
                    String consultaSelecao = """
                        SELECT
                            s.id, 
                            `p_origem`.id AS ponto_origem_id,
                            `p_origem`.nome AS ponto_origem_nome,
                            `p_origem`.observacao AS ponto_origem_obs,
                            `p_destino`.id AS ponto_destino_id,
                            `p_destino`.nome AS ponto_destino_nome,
                            `p_destino`.observacao AS ponto_destino_obs,
                            s.direcao,
                            s.distancia,
                            s.observacao
                        FROM
                            segmentos s
                        LEFT JOIN
                            pontos `p_origem` ON s.origem = p_origem.id
                        LEFT JOIN 
                            pontos `p_destino` ON s.destino = p_destino.id
                                                                          """;
                    PreparedStatement preparedStatement;
                    preparedStatement = conexao.prepareStatement(consultaSelecao);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        responseJson.put("action", "listar-segmentos");  // Updated action to match JSON structure
                        responseJson.put("error", false);
                        responseJson.put("message", "Sucesso");

                        ArrayNode segmentsArray = objectMapper.createArrayNode();  // Updated array name

                        do {
                            ObjectNode segmentNode = objectMapper.createObjectNode();
                            segmentNode.put("id", resultSet.getInt("id"));

                            // Populate ponto_origem
                            ObjectNode pontoOrigemNode = objectMapper.createObjectNode();
                            pontoOrigemNode.put("id", resultSet.getInt("ponto_origem_id"));
                            pontoOrigemNode.put("name", resultSet.getString("ponto_origem_nome"));
                            pontoOrigemNode.put("obs", resultSet.getString("ponto_origem_obs"));
                            segmentNode.set("ponto_origem", pontoOrigemNode);

                            // Populate ponto_destino
                            ObjectNode pontoDestinoNode = objectMapper.createObjectNode();
                            pontoDestinoNode.put("id", resultSet.getInt("ponto_destino_id"));
                            pontoDestinoNode.put("name", resultSet.getString("ponto_destino_nome"));
                            pontoDestinoNode.put("obs", resultSet.getString("ponto_destino_obs"));
                            segmentNode.set("ponto_destino", pontoDestinoNode);

                            segmentNode.put("direcao", resultSet.getString("direcao"));
                            segmentNode.put("distancia", resultSet.getString("distancia"));
                            segmentNode.put("obs", resultSet.getString("observacao"));

                            segmentsArray.add(segmentNode);
                        } while (resultSet.next());

                        ObjectNode dataResponse = objectMapper.createObjectNode();
                        dataResponse.set("segmentos", segmentsArray);  // Updated data key

                        responseJson.set("data", dataResponse);
                    } else {
                        responseJson.put("action", "listar-segmentos");  // Updated action to match JSON structure
                        responseJson.put("error", true);
                        responseJson.put("message", "Nenhum segmento encontrado");  // Updated message
                    }

                    // Agora responseJson contém o JSON no formato desejado com os dados dos usuários.
             }else if (action.equals("excluir-segmento")){
                 String consultaDelete;
                String id = dataNode.get("ponto_id").asText();
                if (id != null && !id.isEmpty()) {
                    consultaDelete = "DELETE FROM `segmentos` WHERE `id` = ? ";

                    PreparedStatement preparedStatement;
                    preparedStatement = conexao.prepareStatement(consultaDelete);

                    preparedStatement.setString(1, id);

                    int linhasAfetadas = preparedStatement.executeUpdate();
                    if (linhasAfetadas == 1) {
                        responseJson = objectMapper.createObjectNode();
                        responseJson.put("action", "excluir-segmento");
                        responseJson.put("error", false);
                        responseJson.put("message", "Ponto Excluido com sucesso!");
                    } else {
                        responseJson = objectMapper.createObjectNode();
                        responseJson.put("action", "excluir-segmento");
                        responseJson.put("error", true);
                        responseJson.put("message", "Erro ao Excluir o Ponto.");
                    }
                } else {
                    responseJson = objectMapper.createObjectNode();
                    responseJson.put("action", "excluir-segmento");
                    responseJson.put("error", true);
                    responseJson.put("message", "Erro ao realizar EXCLUSÃO.");
                }
             }else if(action.equals("pedido-edicao-segmento")){
                
                String id = dataNode.get("segmento_id").asText();
                    String consultaSelecao = """
                        SELECT
                            s.id, 
                            `p_origem`.id AS ponto_origem_id,
                            `p_origem`.nome AS ponto_origem_nome,
                            `p_origem`.observacao AS ponto_origem_obs,
                            `p_destino`.id AS ponto_destino_id,
                            `p_destino`.nome AS ponto_destino_nome,
                            `p_destino`.observacao AS ponto_destino_obs,
                            s.direcao,
                            s.distancia,
                            s.observacao
                        FROM
                            segmentos s
                        LEFT JOIN
                            pontos `p_origem` ON s.origem = p_origem.id
                        LEFT JOIN 
                            pontos `p_destino` ON s.destino = p_destino.id
                        WHERE
                            s.id = ?
                                                                          """;
                        PreparedStatement preparedStatement;
                        preparedStatement = conexao.prepareStatement(consultaSelecao);

                        preparedStatement.setString(1, id);
                        ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        responseJson.put("action", "pedido-edicao-segmento");  // Updated action to match JSON structure
                        responseJson.put("error", false);
                        responseJson.put("message", "Sucesso");

                            ObjectNode segmentNode = objectMapper.createObjectNode();
                            segmentNode.put("id", resultSet.getInt("id"));

                            // Populate ponto_origem
                            ObjectNode pontoOrigemNode = objectMapper.createObjectNode();
                            pontoOrigemNode.put("id", resultSet.getInt("ponto_origem_id"));
                            pontoOrigemNode.put("name", resultSet.getString("ponto_origem_nome"));
                            pontoOrigemNode.put("obs", resultSet.getString("ponto_origem_obs"));
                            segmentNode.set("ponto_origem", pontoOrigemNode);

                            // Populate ponto_destino
                            ObjectNode pontoDestinoNode = objectMapper.createObjectNode();
                            pontoDestinoNode.put("id", resultSet.getInt("ponto_destino_id"));
                            pontoDestinoNode.put("name", resultSet.getString("ponto_destino_nome"));
                            pontoDestinoNode.put("obs", resultSet.getString("ponto_destino_obs"));
                            segmentNode.set("ponto_destino", pontoDestinoNode);

                            segmentNode.put("direcao", resultSet.getString("direcao"));
                            segmentNode.put("distancia", resultSet.getString("distancia"));
                            segmentNode.put("obs", resultSet.getString("observacao"));


                        ObjectNode dataResponse = objectMapper.createObjectNode();
                        dataResponse.set("segmento", segmentNode);  // Updated data key

                        responseJson.set("data", dataResponse);
                    } else {    
                        responseJson.put("action", "pedido-edicao-segmento");  // Updated action to match JSON structure
                        responseJson.put("error", true);
                        responseJson.put("message", "Nenhum segmento encontrado");  // Updated message
                    }

                    // Agora responseJson contém o JSON no formato desejado com os dados dos usuários.
             }else if (action.equals("edicao-segmento")){
                String consultaAtualizacao;           
                JsonNode segmentoNode = dataNode.get("segmento");
                String id =dataNode.get("segmento_id").asText();
                String pontoOrigemId = segmentoNode.get("ponto_origem").get("id").asText();
                String pontoDestinoId = segmentoNode.get("ponto_destino").get("id").asText();

                String obs = segmentoNode.get("obs").asText();
                String direcao = segmentoNode.get("direcao").asText();
                String distancia = segmentoNode.get("distancia").asText();

                if (obs != null && !obs.isEmpty() && pontoOrigemId != null && !pontoOrigemId.isEmpty()&& pontoDestinoId != null && !pontoDestinoId.isEmpty()) {
                    consultaAtualizacao = "UPDATE `segmentos` SET `origem` = ?, `destino` = ?, `distancia` = ?, `direcao` = ? , `observacao` = ? WHERE `id` = ?";

                    PreparedStatement preparedStatement;
                    preparedStatement = conexao.prepareStatement(consultaAtualizacao);

                    preparedStatement.setString(1, pontoOrigemId);
                    preparedStatement.setString(2, pontoDestinoId);
                    preparedStatement.setString(3, distancia);
                    preparedStatement.setString(4, direcao);
                    preparedStatement.setString(5, obs);
                    preparedStatement.setString(6, id);

                    int linhasAfetadas = preparedStatement.executeUpdate();
                    if (linhasAfetadas == 1) {
                        responseJson = objectMapper.createObjectNode();
                        responseJson.put("action", "edicao-segmento");
                        responseJson.put("error", false);
                        responseJson.put("message", "segmento atualizado com sucesso!");
                    } else {
                        responseJson = objectMapper.createObjectNode();
                        responseJson.put("action", "edicao-segmento");
                        responseJson.put("error", true);
                        responseJson.put("message", "Erro ao atualizar o Ponto. Verifique os dados fornecidos.");
                    }
                } else {
                    responseJson = objectMapper.createObjectNode();
                    responseJson.put("action", "edicao-segmento");
                    responseJson.put("error", true);
                    responseJson.put("message", "Todos os campos devem ser preenchidos para a atualização.");
                }

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
        } catch (SQLException ex) { 
         Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
     } 
    }
 public static List<Socket> getClientesConectados() {
    return clientesConectados;
}

} 
