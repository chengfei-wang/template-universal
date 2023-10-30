package template.universal.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import template.universal.model.*
import template.universal.security.RemoteControlProvider
import template.universal.security.RemoteInfoProperties
import template.universal.service.AccessLogService
import template.universal.service.DataReceiveService
import template.universal.service.PageDeployService
import template.universal.service.PageService

@RestController
class PageController {
    @Autowired
    private lateinit var pageService: PageService

    @Autowired
    private lateinit var accessLogService: AccessLogService

    @Autowired
    private lateinit var dataReceiveService: DataReceiveService

    @Autowired
    private lateinit var pageDeployService: PageDeployService

    @Autowired
    private lateinit var remoteControlProvider: RemoteControlProvider

    @Autowired
    private lateinit var remoteInfo: RemoteInfoProperties


    @RequestMapping("/page/{page}")
    fun getPageInfo(@PathVariable page: String): Responses<PageInfo> {
        return pageService.getPageInfo(page)
    }

    @RequestMapping("/page/deploy")
    fun deployPage(@RequestHeader("Authorization") token: String?, @RequestBody deploy: DeployPageReq?): Responses<DeployPageResp> {
        if (token == null) {
            return Responses.fail(message = "凭证为空")
        }
        val remoteAction = remoteControlProvider.verifyServer(token) ?: return Responses.fail(message = "凭证验证失败")
        if (remoteInfo.templateShopUrl != remoteAction.server || !remoteAction.isDeploy) {
            return Responses.fail(message = "ACTION与URL不匹配")
        }

        if (deploy?.pageId == null || deploy.title == null || deploy.elements == null || deploy.deployType == null || deploy.userVerify == null || deploy.deployAddition == null) {
            return Responses.fail(message = "参数不能为空")
        }
        return pageDeployService.deployPage(PageInfo(deploy.pageId, deploy.title, deploy.elements, deploy.deployType, deploy.userVerify, deploy.deployAddition))
    }

    @RequestMapping("/page/delete")
    fun deletePage(@RequestHeader("Authorization") token: String?, @RequestBody delete: DeletePageReq?): Responses<DeletePageResp> {
        if (token == null) {
            return Responses.fail(message = "凭证为空")
        }
        val remoteAction = remoteControlProvider.verifyServer(token) ?: return Responses.fail(message = "凭证验证失败")
        if (remoteInfo.templateShopUrl != remoteAction.server || !remoteAction.isDeploy) {
            return Responses.fail(message = "ACTION与URL不匹配")
        }
        if (delete?.pageId == null) {
            return Responses.fail(message = "参数不能为空")
        }
        return pageDeployService.deletePage(delete.pageId)
    }

    @RequestMapping("/page/access/statistics")
    fun getPageAccessStatistics(@RequestHeader("Authorization") token: String?, @RequestBody page: PageAccessStatisticsReq?): Responses<PageAccessStatisticsResp> {
        if (token == null) {
            return Responses.fail(message = "凭证为空")
        }
        val remoteAction = remoteControlProvider.verifyServer(token) ?: return Responses.fail(message = "凭证验证失败")
        if (remoteInfo.templateShopUrl != remoteAction.server || !remoteAction.isAccessLog) {
            return Responses.fail(message = "ACTION与URL不匹配")
        }

        if (page?.pageId == null) {
            return Responses.fail(message = "参数不能为空")
        }
        return accessLogService.getAccessStatistics(page.pageId)
    }

    @RequestMapping("/page/form/data")
    fun getPageFromData(@RequestHeader("Authorization") token: String?, @RequestBody page: PageFromDataReq?): Responses<PageFromDataResp> {
        if (token == null) {
            return Responses.fail(message = "凭证为空")
        }
        val remoteAction = remoteControlProvider.verifyServer(token) ?: return Responses.fail(message = "凭证验证失败")
        if (remoteInfo.templateShopUrl != remoteAction.server || !remoteAction.isFormData) {
            return Responses.fail(message = "ACTION与URL不匹配")
        }

        if (page?.pageId == null) {
            return Responses.fail(message = "参数不能为空")
        }
        return dataReceiveService.getPageFromData(page.pageId)
    }
}