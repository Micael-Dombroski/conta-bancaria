package conta.infra.security;

import org.mindrot.jbcrypt.BCrypt;

public class HashSenha {
    public static String hash(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt(12));
    }

    public static boolean verificar(String senha, String hash) {
        return BCrypt.checkpw(senha, hash);
    }
}
