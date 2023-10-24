package sistema;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Cliente {

    private static Cliente instancia; // Instância única do Cliente
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    // Construtor privado para evitar criação direta de instâncias
    private Cliente(String servidorIP, int servidorPorta) {
        try {
            socket = new Socket(servidorIP, servidorPorta);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método estático para obter a instância única do Cliente
    public static Cliente getInstance(String servidorIP, int servidorPorta) {
        if (instancia == null) {
            instancia = new Cliente(servidorIP, servidorPorta);
        }
        return instancia;
    }

    public ObjectNode enviarSolicitacao(ObjectNode solicitacao) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString=objectMapper.writeValueAsString(solicitacao);
            System.out.println("Para o Servidor ->"+jsonString);
            out.println(jsonString);        
           ObjectNode jsonResponse = objectMapper.readValue(in.readLine(), ObjectNode.class);
            System.out.println("Resposta do Servidor ->"+jsonResponse);
            return jsonResponse;
        } catch (IOException e) {
            e.printStackTrace();   
        }
        return null;
    }

    public void fecharConexao() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Remova o método conectar() e qualquer uso direto de Cliente na sua aplicação

}



