import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateEncodedPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "parola123"; // Parola "în clar"
        String encodedPassword = encoder.encode(rawPassword); // Criptare parola
        System.out.println("Parola criptată: " + encodedPassword);
    }
}