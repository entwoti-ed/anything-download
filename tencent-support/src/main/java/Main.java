import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Base64;

public class Main {

    private static final String AES = "BC";
    private static final String AES_CBC = "AES/CBC/PKCS7PADDING";

    public static void main(String[] args) throws IOException {
        byte[] aes = aes(
                new FileInputStream("C:\\Users\\dnydi\\Desktop\\03000900005E2490DC8BB7800000000C690A83-6952-41E8-AEF4-3E048D0271B7-00001.ts").readAllBytes(),
                Base64.getDecoder().decode("C5WOWGdvl4b9QNg64pm/mg=="),
                new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                Cipher.DECRYPT_MODE
        );
        new FileOutputStream("C:\\Users\\dnydi\\Desktop\\out.ts").write(aes);
    }

    /**
     * 使用AES加密或解密无编码的原始字节数组, 返回无编码的字节数组结果.
     *
     * @param input 原始字节数组
     * @param key   符合AES要求的密钥
     * @param iv    初始向量
     * @param mode  Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
     */
    public static byte[] aes(byte[] input, byte[] key, byte[] iv, int mode) {
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC);
            cipher.init(mode, new SecretKeySpec(key, AES), new IvParameterSpec(iv));
            cipher.update(input);
            return cipher.doFinal(input);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("加解密出现了异常，当前的key：" + Arrays.toString(key) + "，IV：" + Arrays.toString(iv));
        }
    }
}
