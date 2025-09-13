package event.booking.system.development.config;
import event.booking.system.development.entity.managementuser.Role;
import event.booking.system.development.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder {
    private final RoleRepository roleRepository;

    @PostConstruct
    public void seed() {
        for (Role.RoleName rn : Role.RoleName.values()) {
            roleRepository.findByName(rn).orElseGet(() ->
                    roleRepository.save(Role.builder().name(rn).build()));
        }
    }
}
