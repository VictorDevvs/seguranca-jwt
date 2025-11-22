package security.jwt.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permissoes {

    ADMIN_CREATE("create"),
    ADMIN_READ("read"),
    ADMIN_UPDATE("update"),
    ADMIN_DELETE("delete");

    @Getter
    private final String permissao;
}
