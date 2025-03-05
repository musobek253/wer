package com.musobek.wer.auth.model

import com.musobek.wer.auth.model.enam.Role
import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val  id : UUID? = null,

    @Column(name = "last_name")  // Ustun nomini aniq ko'rsatish
    val lastName: String,

    @Column(name = "first_name")  // Ustun nomini aniq ko'rsatish
    val firstName: String,

    @Column(unique = true)
    private val email: String,

    private val password: String,

    @Column(name = "phone_number")  // Ustun nomini aniq ko'rsatish
    val phoneNumber: String,

    @Enumerated(EnumType.STRING)
    val role: Role,

    @OneToMany(mappedBy = "user")
    val tokens: List<Token> = emptyList()
): UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return role.grantedAuthorities()
    }

    override fun getUsername(): String = email

    override fun getPassword(): String = password // BU METODNI QO'SHDIK!

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val otherClass = if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other::class.java
        val thisClass = if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this::class.java
        if (thisClass != otherClass) return false
        other as User
        return id != null && id == other.id
    }

    override fun hashCode(): Int {
        return if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass.hashCode() else javaClass.hashCode()
    }
}