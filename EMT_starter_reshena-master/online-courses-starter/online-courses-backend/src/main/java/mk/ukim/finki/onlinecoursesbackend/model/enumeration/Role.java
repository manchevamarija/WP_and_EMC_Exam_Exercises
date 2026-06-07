package mk.ukim.finki.onlinecoursesbackend.model.enumeration;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_STUDENT, ROLE_TEACHER, ROLE_ADMINISTRATOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
