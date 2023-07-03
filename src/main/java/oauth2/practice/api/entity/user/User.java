package oauth2.practice.api.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oauth2.practice.oauth.entity.ProviderType;
import oauth2.practice.oauth.entity.RoleType;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member") // 예약어 걸릴수 있음
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String username;
    @Column (nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String emailVerifiedYn;
    @Column(nullable = false)
    private String profileImageUrl;
    @Enumerated(EnumType.STRING)
    @Column
    private ProviderType providerType;
    @Enumerated(EnumType.STRING)
    @Column
    private RoleType roleType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public User(String userId, String username, String email, String emailVerifiedYn, String profileImageUrl, ProviderType providerType, RoleType roleType, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.username = username;
        this.password = "No Password";
        this.email = email != null ? email : "No Email";
        this.emailVerifiedYn = emailVerifiedYn;
        this.profileImageUrl = profileImageUrl != null ? profileImageUrl : "No Profile Image";
        this.providerType = providerType;
        this.roleType = roleType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
