package template.universal.model;

import java.util.Date;

public class FormData {
    private String submitId;
    private String submitPage;
    private String submitIpAddress;
    private Date submitTime;
    private String submitContent;
    private String submitUser;

    public String getSubmitId() {
        return submitId;
    }

    public void setSubmitId(String submitId) {
        this.submitId = submitId;
    }

    public String getSubmitPage() {
        return submitPage;
    }

    public void setSubmitPage(String submitPage) {
        this.submitPage = submitPage;
    }

    public String getSubmitIpAddress() {
        return submitIpAddress;
    }

    public void setSubmitIpAddress(String submitIpAddress) {
        this.submitIpAddress = submitIpAddress;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public String getSubmitContent() {
        return submitContent;
    }

    public void setSubmitContent(String submitContent) {
        this.submitContent = submitContent;
    }

    public String getSubmitUser() {
        return submitUser;
    }

    public void setSubmitUser(String submitUser) {
        this.submitUser = submitUser;
    }
}
