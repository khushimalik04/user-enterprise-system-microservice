package com.user.model.entity;

import com.commonlib.entity.AuditableEntity;
import jakarta.persistence.*;

/**
 * Core user record. Audit timestamps are inherited from the shared auditable base class.
 */
@Entity
@Table(name = "users")
public class User extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String userName;
    private String userEmail;
    private String userCode;
    private String companyName;

    public User(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public User(Long id, String userName, String userEmail, String userCode, String companyName) {
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userCode = userCode;
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userCode='" + userCode + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}
