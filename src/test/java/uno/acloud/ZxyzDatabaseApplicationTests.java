package uno.acloud;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@SpringBootTest
class ZxyzDatabaseApplicationTests {

    @Test
    public void testJWT() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", "zxyz");
        claims.put("password", "123456");
        String zxyz = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, "zxyz")
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .compact();
        System.out.println(zxyz);
    }
    @Test
    public void testParseJWT() {
        Map<String, Object> claims = Jwts.parser()
                .setSigningKey("zxyz")
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IjEyMzQ1NiIsImV4cCI6MTc2MjU0Nzk4MCwidXNlcm5hbWUiOiJ6eHl6In0.vuy7qCaoSsrFB7FpYuGWYDooQ7jDvGl7RplpbT82kgg")
                .getBody();
        System.out.println(claims);
    }
}
