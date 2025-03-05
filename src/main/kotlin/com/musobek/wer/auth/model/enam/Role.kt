package com.musobek.wer.auth.model.enam


import lombok.Getter
import lombok.RequiredArgsConstructor
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.stream.Collectors

@Getter
@RequiredArgsConstructor

enum class Role(private val permissions: Set<Permission>) {
    ADMIN(
        setOf(
            Permission.ADMIN_CREATE,
            Permission.ADMIN_READ,
            Permission.ADMIN_DELETE,
            Permission.ADMIN_UPDATE,
            Permission.MANAGER_CREATE,
            Permission.MANAGER_READ
        )
    ),
    USER(
        setOf(
            Permission.USER_READ,
            Permission.USER_CREATE,
            Permission.USER_DELETE,
            Permission.USER_UPDATE
        )
    ),
    MANAGER(
        setOf(
            Permission.MANAGER_READ,
            Permission.MANAGER_CREATE,
            Permission.MANAGER_DELETE,
            Permission.MANAGER_UPDATE
        )
    );

    fun grantedAuthorities(): List<SimpleGrantedAuthority> {
        val authorities = permissions.map { SimpleGrantedAuthority(it.name) }.toMutableList()
        authorities.add(SimpleGrantedAuthority("ROLE_$name"))
        return authorities
    }
}