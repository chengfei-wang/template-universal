package template.universal.security

import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTVerificationException
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import template.universal.model.TrustableKey
import java.util.*

@Component
class TrustableKeyProvider {
    @Autowired
    private lateinit var jwtManager: JwtManager

    fun signTrustableToken(trustableKey: TrustableKey): String {
        return JWT.create()
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + TRUSTABLE_KEY_EXPIRE_TIME))
            .withClaim(TRUSTABLE_KEY, gson.toJson(trustableKey))
            .sign(jwtManager.algorithm)
    }

    fun verifyTrustableToken(token: String): TrustableKey? {
        try {
            val decoded = JWT.require(jwtManager.algorithm)
                .build()
                .verify(token)
            return gson.fromJson(
                decoded.getClaim(TRUSTABLE_KEY).asString(),
                object : TypeToken<TrustableKey>() {}.type
            )
        } catch (e: JWTVerificationException) {
            e.printStackTrace()
        } catch (e: JsonSyntaxException) {
            println("JsonSyntaxException, Token: $token")
        }
        return null
    }

    companion object {
        const val TRUSTABLE_KEY = "trustable_key"

        // 提交凭证有效时间
        private const val TRUSTABLE_KEY_EXPIRE_TIME = 24 * 60 * 60 * 1000L

        val gson = Gson()
    }
}