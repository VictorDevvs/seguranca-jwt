package security.jwt.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static security.jwt.domain.Permissoes.*;

@RequiredArgsConstructor
public enum Role {
    COMUM(Collections.emptySet()),
    ADMIN(Set.of(
            ADMIN_CREATE,
            ADMIN_READ,
            ADMIN_UPDATE,
            ADMIN_DELETE
    ))
    ;

    @Getter
    private final Set<Permissoes> permissoes;

    public List<SimpleGrantedAuthority> autoridades(){
        var autoridades = getPermissoes()
                .stream()
                .map(p -> new SimpleGrantedAuthority(p.getPermissao()))
                .collect(Collectors.toList());

        autoridades.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return autoridades;
    }
}
