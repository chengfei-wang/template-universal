package template.universal.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import template.universal.model.AccessLog
import template.universal.model.PageAccessStatisticsResp
import template.universal.model.Responses
import template.universal.repository.AccessLogRepository

@Service
class AccessLogService {
    @Autowired
    lateinit var accessLogRepository: AccessLogRepository

    fun save(accessLog: AccessLog): Responses<AccessLog> {
        accessLogRepository.save(accessLog)
        return Responses.ok(data = accessLog)
    }


    fun getAccessStatistics(pageId: String): Responses<PageAccessStatisticsResp> {
        return Responses.ok(data = PageAccessStatisticsResp(accessLogRepository.getAllLogsByPage(pageId)))
    }
}