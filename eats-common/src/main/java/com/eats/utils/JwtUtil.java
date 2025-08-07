package com.eats.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    /**
     * JWTを生�?
     * Hs256アルゴリズムを使用し、秘密鍵は固定キーを使用
     *
     * @param secretKey JWT秘密�?
     * @param ttlMillis JWT有効期限（ミリ秒�?
     * @param claims    設定情報
     * @return
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        // 署名時に使用する署名アルゴリズム、つまりヘッダー部分を指定しま�?
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // JWTの生成時�?
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        // JWTのボディを設�?
        JwtBuilder builder = Jwts.builder()
                // プライベートなクレームがある場合は、必ず最初にこの独自に作成したプライベートなクレームを設定してください。これはビルダーのクレームに値を設定するものであり、標準のクレーム割り当ての後に記述すると、それらの標準のクレームが上書きされてしまいます
                .setClaims(claims)
                // 署名に使用する署名アルゴリズムと署名に使用する秘密鍵を設定します
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                // 有効期限を設定します
                .setExpiration(exp);

        return builder.compact();
    }

    /**
     * トークンの復号化
     *
     * @param secretKey JWT秘密�?この秘密鍵はサーバーサイドで厳重に保管し、外部に漏らさないでください。漏洩すると、署名が偽造される可能性があります。複数のクライアントと連携する場合は、複数の鍵に改造することをお勧めします
     * @param token     暗号化されたトークン
     * @return
     */
    public static Claims parseJWT(String secretKey, String token) {
        // DefaultJwtParserを取�?
        Claims claims = Jwts.parser()
                // 署名の秘密鍵を設�?
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                // 解析が必要なJWTを設�?
                .parseClaimsJws(token).getBody();
        return claims;
    }

}
