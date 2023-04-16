package pengliu.me.backend.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "wiki")
public class WikiConfiguration {
    private String ftpServerIPAddress;
    private Integer ftpServerPort;
    private String ftpUserName;
    private String ftpUserPassword;

    public String getFtpServerIPAddress() {
        return ftpServerIPAddress;
    }

    public void setFtpServerIPAddress(String ftpServerIPAddress) {
        this.ftpServerIPAddress = ftpServerIPAddress;
    }

    public Integer getFtpServerPort() {
        return ftpServerPort;
    }

    public void setFtpServerPort(Integer ftpServerPort) {
        this.ftpServerPort = ftpServerPort;
    }

    public String getFtpUserName() {
        return ftpUserName;
    }

    public void setFtpUserName(String ftpUserName) {
        this.ftpUserName = ftpUserName;
    }

    public String getFtpUserPassword() {
        return ftpUserPassword;
    }

    public void setFtpUserPassword(String ftpUserPassword) {
        this.ftpUserPassword = ftpUserPassword;
    }
}
