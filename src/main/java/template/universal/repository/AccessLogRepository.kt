package template.universal.repository

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import template.universal.model.AccessLog

@Mapper
interface AccessLogRepository {
    @Insert("insert into access_log (access_id, access_page, access_ip_address, access_time) values (#{accessId}, #{accessPage}, #{accessIpAddress}, #{accessTime})")
    fun save(accessLog: AccessLog)

    @Select("select * from access_log where access_page = #{accessPage} order by access_time desc")
    fun getAllLogsByPage(accessPage: String): List<AccessLog>

    @Select("select * from access_log")
    fun getAllLogs(): List<AccessLog>
}