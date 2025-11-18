package security.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // HARD CODE - ESSE PROJETO É APENAS PARA FINS DE APRENDIZADO. EM UM AMBIENTE REAL, NUNCA EXPONHA CHAVES SECRETAS
    private static final String SECRET_KEY = "oBC9P8xqR7bC2XgB8kCsJp6V3zT7FfA9LdZ3wN+WgKs=";
    private long tokenExp = 9000000;

    public String extrairEmail(String token) {
        return extrairClaim(token, Claims::getSubject);
    }

    public <T> T extrairClaim(String token, Function<Claims, T> claimsTFunction){
        final Claims claims = extrairTodasClaims(token);
        return claimsTFunction.apply(claims);
    }

    public String gerarToken(Map<String, Object> claimsExtra, UserDetails userDetails){
        return Jwts.builder()
                .claims(claimsExtra)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenExp))
                .signWith(chaveAssinatura())
                .compact();
    }

    public String gerarToken(UserDetails userDetails){
        return gerarToken(new HashMap<>(), userDetails);
    }

    public boolean validarToken(String token, UserDetails userDetails){
        final String email = extrairEmail(token);
        return (email.equals(userDetails.getUsername()) && !tokenExpirado(token));
    }

    private boolean tokenExpirado(String token) {
        return extrairExpiracao(token).before(new Date());
    }

    private Date extrairExpiracao(String token) {
        return extrairClaim(token, Claims::getExpiration);
    }

    private Claims extrairTodasClaims(String token){
        return Jwts.parser()
                .verifyWith(chaveAssinatura())
                .clockSkewSeconds(60)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey chaveAssinatura(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
