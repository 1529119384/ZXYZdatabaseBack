package uno.acloud.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import uno.acloud.pojo.User;

import java.util.Date;
import java.util.Map;

@Slf4j
public class JwtUtils {
    public static String generateJwt(User u) {
        Map<String, Object> claims = new java.util.HashMap<>();
        claims.put("id", u.getId());
        claims.put("name", u.getName());
        claims.put("username", u.getUsername());

        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, "zxyz")
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .compact();
        log.info("生成JWT成功,token:{}", jwt);
        return jwt;
    }

    public static Map<String, Object> parseJWT(String jwt) {
        Map<String, Object> claims = Jwts.parser()
                .setSigningKey("zxyz")
                .parseClaimsJws(jwt)
                .getBody();
        log.info("解析JWT成功,解析结果:{}", claims);
        return claims;
    }
}
