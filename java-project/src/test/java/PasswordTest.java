import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "parola123"; // Parola introdusă
        String storedPassword = "$2a$10$GKq5RgCxO1GzGEbY319AFewx7OMX3mEsKLkCnYxSu3rsy8SKAw0w2"; // Parola stocată

        boolean matches = encoder.matches(rawPassword, storedPassword);
        System.out.println("Parola se potrivește? " + matches);
    }
}


