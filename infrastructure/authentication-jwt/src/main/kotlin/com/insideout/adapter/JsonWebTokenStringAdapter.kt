package com.insideout.adapter

import com.insideout.usecase.member.port.TokenPort
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.Date
import javax.crypto.SecretKey

@Component
class JsonWebTokenStringAdapter : TokenPort {
    // TODO: 추후 토큰 DataClass 추가
    override fun create(
        subject: String,
        secretKey: SecretKey,
        expirationMillis: Long,
    ): String {
        return Jwts.builder()
            .subject(subject)
            .signWith(secretKey)
            .expiration(Date.from(Instant.now().plusMillis(expirationMillis)))
            .compact()
    }

    override fun <T> parse(
        token: String,
        secretKey: SecretKey,
        converter: (String) -> T,
    ): T {
        return runCatching {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .payload
                .subject
                .let(converter)
        }.getOrElse {
            throw IllegalArgumentException() // TODO
        }
    }
}
