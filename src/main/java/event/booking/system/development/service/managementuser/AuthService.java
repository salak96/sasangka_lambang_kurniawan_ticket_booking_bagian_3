package event.booking.system.development.service.managementuser;

import event.booking.system.development.model.request.LoginRequest;
import event.booking.system.development.model.request.RegisterRequest;
import event.booking.system.development.model.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest req);
    AuthResponse login(LoginRequest req);
}
