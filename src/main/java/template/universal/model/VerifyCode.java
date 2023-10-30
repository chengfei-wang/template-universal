package template.universal.model;

import java.util.Date;

public class VerifyCode {
    private String codeId;
    private String codePage;
    private String codeKey;
    private String codeValue;
    private Date codeExpire;

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getCodePage() {
        return codePage;
    }

    public void setCodePage(String codePage) {
        this.codePage = codePage;
    }

    public String getCodeKey() {
        return codeKey;
    }

    public void setCodeKey(String codeKey) {
        this.codeKey = codeKey;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public Date getCodeExpire() {
        return codeExpire;
    }

    public void setCodeExpire(Date codeExpire) {
        this.codeExpire = codeExpire;
    }
}
