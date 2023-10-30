package template.universal.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import template.universal.model.*
import template.universal.repository.FormDataRepository
import template.universal.repository.PageInfoRepository
import template.universal.security.TrustableKeyProvider

@Service
class DataReceiveService {
    @Autowired
    lateinit var formDataRepository: FormDataRepository

    @Autowired
    lateinit var pageInfoRepository: PageInfoRepository

    @Autowired
    lateinit var trustableKeyProvider: TrustableKeyProvider

    fun receiveData(token: String?, data: FormData): Responses<FormData> {
        val pageInfo = pageInfoRepository.getPageInfo(data.submitPage) ?: return Responses.fail(message = "页面不存在")

        val deployType = DeployOption.DEPLOY_TYPE.firstOrNull { it.id == pageInfo.deployType }
        val userVerify = DeployOption.USER_VERIFY.firstOrNull { it.id == pageInfo.userVerify }

        if (deployType == null) {
            // 默认不接受数据
            return Responses.fail(message = "页面未配置发布方式")
        }

        if (deployType.id == DeployOption.DEPLOY_TYPE_STATIC) {
            // 静态发布方式不接受数据
            return Responses.fail(message = "静态发布方式不接受数据")
        }

        if (userVerify != null && (userVerify.id == DeployOption.USER_VERIFY_TEL || userVerify.id == DeployOption.USER_VERIFY_EMAIL)) {
            if (token.isNullOrEmpty()) {
                return Responses.fail(message = "数据提交需要验证身份")
            }
            val trustableKey =
                trustableKeyProvider.verifyTrustableToken(token) ?: return Responses.fail(message = "用户验证失败")

            if (trustableKey.page != pageInfo.pageId) {
                return Responses.fail(message = "提交页面错误")
            }

            data.submitUser = trustableKey.key
        }

        val value = formDataRepository.insertFormData(data)
        if (value > 0) {
            return Responses.ok(data = data)
        }
        return Responses.fail(message = "提交数据异常")
    }

    fun getPageFromData(pageId: String): Responses<PageFromDataResp> {
        val pageInfo = pageInfoRepository.getPageInfo(pageId) ?: return Responses.fail(message = "页面不存在")
        val elements: List<Element> = GSON.fromJson(pageInfo.elements, object : TypeToken<List<Element>>() {}.type)
        val entries = evalElementsEntry(elements)
        val formDataList = formDataRepository.getFormDataByPage(pageId)
        val evalFormDataList = evalFormData(entries, formDataList)
        return Responses.ok(data = PageFromDataResp(entries, evalFormDataList))
    }

    companion object {
        private val GSON = Gson()

        private fun evalElementsEntry(elements: List<Element>): List<Element.Entry> {
            val entries = ArrayList<Element.Entry>()
            for (element in elements) {
                if (element.node_prop?.name != null) {
                    entries.add(Element.Entry(element.node_prop.name, element.node_prop.content))
                }
                if (element.children != null) {
                    for (child in element.children) {
                        entries.addAll(evalElementsEntry(child.children))
                    }
                }
            }
            return entries
        }

        private fun evalFormData(entries: List<Element.Entry>, formDataList: List<FormData>): List<UserFormData> {
            val evalData = ArrayList<UserFormData>()
            for (formData in formDataList) {
                val data = HashMap<String, String>()
                val content: Map<String, List<String>> =
                    GSON.fromJson(formData.submitContent, object : TypeToken<Map<String, List<String>>>() {}.type)
                for (entry in entries) {
                    data[entry.name] = content[entry.name]?.joinToString() ?: ""
                }
                val userFormData = UserFormData(formData.submitId, formData.submitIpAddress, formData.submitTime, formData.submitUser, data)
                evalData.add(userFormData)
            }
            return evalData
        }
    }
}