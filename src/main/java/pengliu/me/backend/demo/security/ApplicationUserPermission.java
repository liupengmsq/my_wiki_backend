package pengliu.me.backend.demo.security;

public enum ApplicationUserPermission {
    NAV_READ("nav:read"),
    NAV_WRITE("nav:write"),
    WIKI_READ("wiki:read"),
    WIKI_WRITE("wiki:write"),
    WIKI_CATEGORY_READ("wiki_category:read"),
    WIKI_CATEGORY_WRITE("wiki_category:write");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
