package com.jinho.randb.domain.account.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jinho.randb.domain.opinion.domain.Opinion;
import com.jinho.randb.domain.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Table(indexes = {
        @Index(name = "idx_member_login_id",columnList = "login_id")
})
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"posts"})
public class Account {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(unique = true)
    private String loginId;
    private String email;
    @JsonIgnore
    @Column(nullable = false, columnDefinition = "varchar(255) default 'ROLE_USER'")
    private String roles;
    private String loginType;

    private String username;

    private String nickname;

    private String password;

    private LocalDate createAt;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean verified;

    @Builder.Default
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL,orphanRemoval = true)
    List<Post> posts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL,orphanRemoval = true)
    List<Opinion> opinions = new ArrayList<>();

    public List<String> getRoleList() {
        if (this.roles != null && this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }
}
