package template.universal.model

data class DeployPageReq(
    val pageId: String?,
    val title: String?,
    val elements: String?,
    val deployType: String?,
    val userVerify: String?,
    val deployAddition: String?
)