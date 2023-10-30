package template.universal.security

import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class JwtManager {
    @Autowired
    lateinit var rsaKeyFile: RsaKeyProperties

    val algorithm: Algorithm by lazy {
        Algorithm.RSA512(
            rsaKeyFile.publicKey.rsaPublicKey(),
            rsaKeyFile.privateKey.rsaPrivateKey()
        )
    }

    companion object {
        const val DEFAULT_TIMEOUT = 7 * 24 * 60 * 60 * 1000
    }
}