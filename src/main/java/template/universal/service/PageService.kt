package template.universal.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import template.universal.model.PageInfo
import template.universal.model.Responses
import template.universal.repository.PageInfoRepository

@Service
class PageService {

    @Autowired
    private lateinit var pageInfoRepository: PageInfoRepository

    fun getPageInfo(pageId: String): Responses<PageInfo> {
        val pageInfo = pageInfoRepository.getPageInfo(pageId) ?: return Responses.fail(message = "页面不存在或已删除")
        return Responses.ok(data = pageInfo)
    }
}