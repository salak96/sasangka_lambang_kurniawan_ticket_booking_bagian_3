package event.booking.system.development.entity.managementuser;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "roles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false, length = 32)
    private RoleName name;

    public enum RoleName { ROLE_ADMIN, ROLE_SELLER, ROLE_USER }
}


