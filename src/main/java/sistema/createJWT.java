/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema;

/**
 *
 * @author henri
 */
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class createJWT {

    // Chave secreta para assinar e verificar o token
    private static final String SECRET_KEY = "AoT3QFTTEkj16rCby/TPVBWvfSQHL3GeEz3zVwEd6LDrQDT97sgDY8HJyxgnH79jupBWFOQ1+7fRPBLZfpuA2lwwHqTgk+NJcWQnDpHn31CVm63Or5c5gb4H7/eSIdd+7hf3v+0a5qVsnyxkHbcxXquqk9ezxrUe93cFppxH4/kF/kGBBamm3kuUVbdBUY39c4U3NRkzSO+XdGs69ssK5SPzshn01axCJoNXqqj+ytebuMwF8oI9+ZDqj/XsQ1CLnChbsL+HCl68ioTeoYU9PLrO4on+rNHGPI0Cx6HrVse7M3WQBPGzOd1TvRh9eWJrvQrP/hm6kOR7KrWKuyJzrQh7OoDxrweXFH8toXeQRD8=";

    // Método para gerar um token JWT
        public static String generateJwtToken(String subject, boolean isAdmin) {
          System.out.println(isAdmin);
           return Jwts.builder()
                   .claim("user_id", subject)
                   .claim("isAdmin", isAdmin)
                   .setSubject(subject)
                   .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                   .compact();
       }
     
      public static Jws<Claims> parseToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
    }

    public static boolean isAdmin(String token) {
        Jws<Claims> parsedToken = parseToken(token);
        return (boolean) parsedToken.getBody().get("isAdmin", Boolean.class);
    }


    // Método para verificar e obter as informações do token JWT
    public static Claims verifyJwtToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            return claims;
        } catch (Exception e) {
            // O token é inválido ou expirou
            return null;
        }
    }

//    public static void main(String[] args) {
//        // Exemplo de geração e verificação de um token JWT
//        String username = "usuario123";
//        String token = generateJwtToken(username);
//        System.out.println("Token JWT gerado: " + token);
//
//        // Verifique o token
//        Claims claims = verifyJwtToken(token);
//        if (claims != null) {
//            System.out.println("Token JWT válido");
//            System.out.println("Nome de usuário: " + claims.get("username"));
//        } else {
//            System.out.println("Token JWT inválido ou expirado");
//        }
//    }
}
