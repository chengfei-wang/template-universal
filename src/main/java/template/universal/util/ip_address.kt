package template.universal.util

import javax.servlet.http.HttpServletRequest

fun HttpServletRequest.getRequestIpAddress(): String {
    val address = this.getHeader("X-Forwarded-For")
    if (address != null && address.isNotEmpty()) {
        return address
    }
    return this.remoteAddr
}