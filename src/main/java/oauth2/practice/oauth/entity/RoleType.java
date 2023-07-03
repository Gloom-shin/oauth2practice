package oauth2.practice.oauth.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {
    USER("ROLE_USER", "일반 사용자"),
    ADMIN("ROLE_ADMIN", "관리자"),
    GUEST("ROLE_GUEST", "게스트");

    private String code;
    private String displayName;

    public static RoleType of(String code) {
        for (RoleType roleType : RoleType.values()) {
            if (roleType.getCode().equals(code)) {
                return roleType;
            }
        }
        throw new IllegalArgumentException("Unknown RoleType code : " + code);
    }

}
