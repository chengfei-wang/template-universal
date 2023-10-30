package template.universal.repository

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import template.universal.model.VerifyCode

@Mapper
interface UserVerifyRepository {
    @Insert("insert into user_verify_code (code_id, code_page, code_key, code_value, code_expire) values (#{codeId}, #{codePage}, #{codeKey}, #{codeValue}, #{codeExpire})")
    fun addUserVerify(userVerify: VerifyCode): Int

    @Select("select * from user_verify_code where code_id = #{codeId} limit 1")
    fun getUserVerify(codeId: String): VerifyCode?
}