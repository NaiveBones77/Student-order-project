package edu.studentorder.domain;

import java.time.LocalDate;

public class Child extends Person {

    private String setrificateNumber;
    private LocalDate issueDate;
    private RegisterOffice issueDepartment;

    public Child(String surName, String givenName, String patronymic, LocalDate dateOfBirth) {
        super(surName, givenName, patronymic, dateOfBirth);
    }

    public String getSetrificateNumber() {
        return setrificateNumber;
    }

    public void setSetrificateNumber(String setrificateNumber) {
        this.setrificateNumber = setrificateNumber;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public RegisterOffice getIssueDepartment() {
        return issueDepartment;
    }

    public void setIssueDepartment(RegisterOffice issueDepartment) {
        this.issueDepartment = issueDepartment;
    }
}
