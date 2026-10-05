package conta.infra.security;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class CPFCrypto {
    private static final int IV_BYTES = 12;
    private static final int TAG_BITS = 128;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final SecretKeySpec CHAVE = getChave();

    private static SecretKeySpec getChave() {
        String valor = System.getenv("CHAVE_AES");
        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException("Variavel de ambiente CHAVE_AES nao definida");
        }
        byte[] bytes = Base64.getDecoder().decode(valor);
        if (bytes.length != 32) {
            throw new IllegalStateException("CHAVE_AES deve ter 32 bytes (Base64)");
        }
        return new SecretKeySpec(bytes, "AES");
    }

    public static String criptografar(String cpf) {
        try {
            byte[] iv = new byte[IV_BYTES];
            RANDOM.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, CHAVE, new GCMParameterSpec(TAG_BITS, iv));
            byte[] cifrado = cipher.doFinal(cpf.getBytes(StandardCharsets.UTF_8));

            // guarda IV + cifrado juntos
            byte[] saida = new byte[iv.length + cifrado.length];
            System.arraycopy(iv, 0, saida, 0, iv.length);
            System.arraycopy(cifrado, 0, saida, iv.length, cifrado.length);

            return Base64.getEncoder().encodeToString(saida);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static String descriptografar(String textoCifrado) {
        try {
            byte[] dados = Base64.getDecoder().decode(textoCifrado);
            byte[] iv = Arrays.copyOfRange(dados, 0, IV_BYTES);
            byte[] cifrado = Arrays.copyOfRange(dados, IV_BYTES, dados.length);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, CHAVE, new GCMParameterSpec(TAG_BITS, iv));

            return new String(cipher.doFinal(cifrado), StandardCharsets.UTF_8);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}