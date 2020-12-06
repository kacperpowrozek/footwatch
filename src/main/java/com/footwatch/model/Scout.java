package com.footwatch.model;

import com.sun.istack.NotNull;
import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "scouts")
public class Scout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String surname;
    @Column(unique = true)
    private String username;
    @Column
    private String password;
    @Column(name = "date_of_birth")
    private Date dateOfBirth;
    @Column
    private String nationality;
    @Column(name = "license_no")
    private String licenseNo;
    @Column(name= "phone_number")
    private String phoneNumber;
    @Column(name= "email")
    private String email;

    @OneToMany(mappedBy = "scout")
    private Set<Monitoring> monitoring = new HashSet<Monitoring>();

    @OneToMany(mappedBy = "scout")
    private Set<MatchEvaluationScout> matchEvaluationScouts = new HashSet<>();

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }

    public Set<Monitoring> getMonitoring() {
        return monitoring;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    public String getNationality() {
        return nationality;
    }
    public String getLicenseNo() {
        return licenseNo;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getEmail() {
        return email;
    }
    public Set<MatchEvaluationScout> getMatchEvaluationScouts() {
        return matchEvaluationScouts;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setMonitoring(Set<Monitoring> monitoring) {
        this.monitoring = monitoring;
    }
    public void setMatchEvaluationScouts(Set<MatchEvaluationScout> matchEvaluationScouts) {
        this.matchEvaluationScouts = matchEvaluationScouts;
    }
}
