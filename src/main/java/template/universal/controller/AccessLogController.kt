package template.universal.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import template.universal.model.AccessLog
import template.universal.model.Responses
import template.universal.service.AccessLogService
import template.universal.util.getRequestIpAddress
import template.universal.util.uuid
import java.util.*
import javax.servlet.http.HttpServletRequest

@RestController
class AccessLogController {
    @Autowired
    private lateinit var accessLogService: AccessLogService

    @RequestMapping("/access/{page}")
    fun accessPage(request: HttpServletRequest, @PathVariable page: String?): Responses<AccessLog> {
        if (page == null) {
            return Responses.fail(message = "参数错误")
        }

        val accessLog = AccessLog()

        accessLog.accessId = uuid()
        accessLog.accessPage = page
        accessLog.accessTime = Date()
        accessLog.accessIpAddress = request.getRequestIpAddress()

        return accessLogService.save(accessLog)
    }
}