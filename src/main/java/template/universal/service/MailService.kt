package template.universal.service

import org.springframework.stereotype.Service
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

@Service
class MailService {
    fun sendMail(to: String, title: String, content: String) {
        println("send mail to $to with title $title and content $content")
        try {
            val props = Properties()
            props["mail.smtp.auth"] = "true"
            props["mail.smtp.host"] = MAIL_HOST
            props["mail.smtp.port"] = MAIL_PORT
            val authenticator = object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(MAIL_USER, MAIL_PASSWORD)
                }
            }
            val session = Session.getDefaultInstance(props, authenticator)
            val message = MimeMessage(session)
            message.setFrom(InternetAddress(MAIL_USER))
            message.setRecipient(Message.RecipientType.TO, InternetAddress(to))
            message.subject = title
            message.setContent(content, "text/html;charset=utf-8")
            Transport.send(message)
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }


    companion object {
        const val MAIL_HOST = ""
        const val MAIL_PORT = 0
        const val MAIL_USER = ""
        const val MAIL_PASSWORD = ""
    }
}