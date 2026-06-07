package mk.ukim.finki.traveltoursbackend.model.enumeration;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_CUSTOMER, ROLE_GUIDE, ROLE_ADMINISTRATOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
