package at.boot.enums;

public enum Role {
    ROLE_USER, ROLE_ADMIN, ROLE_SUPERADMIN, ROLE_UNKNOWN;

    public static Role getDefault() {
        return ROLE_USER;
    }

}
