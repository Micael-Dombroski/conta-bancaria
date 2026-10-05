package conta.infra.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class HashCPF {
    private static final byte[] SEGREDO = lerSegredo();

    private static byte[] lerSegredo() {
        String valor = System.getenv("SEGREDO");
        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException("Variavel de ambiente SEGREDO nao definida");
        }
        return valor.getBytes(StandardCharsets.UTF_8);
    }
    public static String hash(String cpf) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(SEGREDO, "HmacSHA256"));
            byte[] h = mac.doFinal(cpf.replaceAll("\\D", "").getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : h) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
