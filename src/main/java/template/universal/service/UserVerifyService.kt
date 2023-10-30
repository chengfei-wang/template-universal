package template.universal.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import template.universal.model.*
import template.universal.repository.PageInfoRepository
import template.universal.repository.UserVerifyRepository
import template.universal.security.TrustableKeyProvider
import template.universal.util.uuid
import java.util.*

@Service
class UserVerifyService {

    @Autowired
    private lateinit var trustableKeyProvider: TrustableKeyProvider

    @Autowired
    private lateinit var userVerifyRepository: UserVerifyRepository

    @Autowired
    private lateinit var pageInfoRepository: PageInfoRepository

    @Autowired
    private lateinit var mailService: MailService

    fun getEmailVerifyCode(page: String, email: String): Responses<VerifyCodeResp> {
        pageInfoRepository.getPageInfo(page) ?: return Responses.fail(message = "页面不存在")

        val userVerify = VerifyCode()
        userVerify.codeId = uuid()
        userVerify.codePage = page
        userVerify.codeKey = email
        userVerify.codeValue = generateVerifyCode()
        userVerify.codeExpire = Date(System.currentTimeMillis() + CODE_EXPIRE_TIME)

        val result = userVerifyRepository.addUserVerify(userVerify)
        if (result > 0) {
            sendEmailVerify(userVerify.codeKey, userVerify.codeValue)
            return Responses.ok(data = VerifyCodeResp(userVerify.codeId, userVerify.codeExpire))
        }
        return Responses.fail(message = "验证码生成失败")
    }

    fun verifyEmailCode(codeId: String, codeValue: String): Responses<TrustableKeyResp> {
        val userVerify = userVerifyRepository.getUserVerify(codeId) ?: return Responses.fail(message = "验证码错误")
        if (userVerify.codeValue != codeValue) {
            return Responses.fail(message = "验证码错误")
        }

        if (userVerify.codeExpire.before(Date())) {
            return Responses.fail(message = "验证码已过期")
        }

        val trustableKey = TrustableKey()
        trustableKey.key = userVerify.codeKey
        trustableKey.page = userVerify.codePage
        val token = trustableKeyProvider.signTrustableToken(trustableKey)
        return Responses.ok(data = TrustableKeyResp(token))
    }

    private fun sendEmailVerify(email: String, code: String) {
        mailService.sendMail(email, "来自TemplateShop的验证码", "您的验证码为$code，用于验证您的邮箱地址，请勿泄漏。")
    }

    // generate verify code 0~9, a~z
    private fun generateVerifyCode(): String {
        val chars = "0123456789abcdefghijklmnopqrstuvwxyz"
        val sb = StringBuilder()
        for (i in 0 until 6) {
            val index = (Math.random() * 36).toInt()
            sb.append(chars[index])
        }
        return sb.toString()
    }

    companion object {
        // 验证码有效时间
        private const val CODE_EXPIRE_TIME = 10 * 60 * 1000L
    }
}