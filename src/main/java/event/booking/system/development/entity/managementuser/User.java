package event.booking.system.development.entity.managementuser;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(
        name = "m_users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_users_username", columnNames = "username")
        },
        indexes = {
                @Index(name = "idx_user_created_date", columnList = "createdDate"),
                @Index(name = "idx_user_modified_date", columnList = "modifiedDate"),
                @Index(name = "idx_user_username", columnList = "username"),
                @Index(name = "idx_user_email", columnList = "email"),
                @Index(name = "idx_user_status", columnList = "status"),
                @Index(name = "idx_user_role", columnList = "role")
        }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false)
    private String passwordHash;

    @Builder.Default
    private Boolean isActive = true;

    // 🔥 ini penting -> relasi ke Role entity
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
