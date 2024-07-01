package hust.server.infrastructure.utilies;

import hust.server.app.exception.ApiException;
import hust.server.domain.authen.entities.CustomUserDetails;
import hust.server.infrastructure.enums.MessageCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    private String SECRET_KEY = "linh1234";
    private final int MUNINUS = 1000;
    // Tạo một JWT token dựa trên userDetails
    public String generateToken(CustomUserDetails customUserDetails, long expiration){
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", customUserDetails.getId());
        claims.put("role", customUserDetails.getRole());
        return createToken(claims, customUserDetails.getUsername(), expiration);
    }

    public String generateToken(CustomUserDetails customUserDetails, long expiration, Map<String, Object> payload) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", customUserDetails.getId());
        claims.put("role", customUserDetails.getRole());
        claims.putAll(payload);
        return createToken(claims, customUserDetails.getUsername(), expiration);
    }

    private String createToken(Map<String, Object> claims, String subject, long expiration) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis())) // set ngày phát hành
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * expiration)) // set thời gian sống là 10 giờ
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaim(token);
        return claimsResolver.apply(claims);
    }

    public String getClaimByKey(String token) {
        final Claims claims = extractAllClaim(token);
        return claims.get("id", String.class);
    }

    public String getClaimByRole(String token){
        final Claims claims = extractAllClaim(token);
        return claims.get("role", String.class);
    }

    private Claims extractAllClaim(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    // Lấy token và trả về người dùng.
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }
    // Kiểm tra thời gian sống của token
    public Boolean isTokenExpired(String token) throws ApiException {
        Date date =  extractExpiration(token);
        if (date == null)throw new ApiException(MessageCode.TOKEN_ERROR);
        return date.before(new Date());
    }
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    // check tên người dùng trong userDetails có trùng với tên trong token hay
    public Boolean validateToken(String token, @NotNull CustomUserDetails customUserDetails){
        if (customUserDetails.getRole().equals("GUEST"))return !isTokenExpired(token);
        final String username = extractUsername(token);
        return (username.equals(customUserDetails.getUsername()) && !isTokenExpired(token));
    }
}
