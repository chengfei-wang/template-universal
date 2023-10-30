package template.universal.repository

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import template.universal.model.FormData


@Mapper
interface FormDataRepository {
    @Select("select * from form_data")
    fun getAllFormData(): List<FormData>

    @Select("select * from form_data where submit_page = #{pageId}")
    fun getFormDataByPage(pageId: String): List<FormData>

    @Insert("insert into form_data (submit_id, submit_page, submit_ip_address, submit_time, submit_content, submit_user) values (#{submitId}, #{submitPage}, #{submitIpAddress}, #{submitTime}, #{submitContent}, #{submitUser})")
    fun insertFormData(formData: FormData): Int
}