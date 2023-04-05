package mfpai.gouv.sn.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String ROLE_CHEF_ETABLISSEMENT = "ROLE_CHEF_ETABLISSEMENT";
    public static final String BFPA = "ROLE_BFPA";
    public static final String APPRENANT = "ROLE_APPRENANT";
    public static final String ENSEIGNENT = "ROLE_ENSEIGNANT";

    private AuthoritiesConstants() {}
}
