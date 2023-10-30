package template.universal.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import template.universal.model.*
import template.universal.service.UserVerifyService
import template.universal.util.checkEmail

@RestController
class UserVerifyController {
    @Autowired
    private lateinit var userVerifyService: UserVerifyService

    @RequestMapping("/verify/send/{page}")
    fun getEmailVerifyCode(@PathVariable page: String?, @RequestBody verifyCodeReq: VerifyCodeReq?): Responses<VerifyCodeResp> {
        if (page == null || verifyCodeReq?.email == null || !checkEmail(verifyCodeReq.email)) {
            return Responses.fail(message = "邮箱格式不正确")
        }
        return userVerifyService.getEmailVerifyCode(page, verifyCodeReq.email)
    }

    @RequestMapping("/verify/{codeId}")
    fun verifyEmail(@PathVariable codeId: String?, @RequestBody trustableKeyReq: TrustableKeyReq?): Responses<TrustableKeyResp> {
        if (codeId == null || trustableKeyReq?.codeValue == null) {
            return Responses.fail(message = "验证码不能为空")
        }
        return userVerifyService.verifyEmailCode(codeId, trustableKeyReq.codeValue)
    }
}