import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class teste {
 public static void main(String[] args) {
  String str = "Example String";
  byte[] b = str.getBytes();
  System.out.println("S " + str);
  System.out.println("Array " + b);
  System.out.println("Array as String -> " + new String(b));

  byte[] c =Encrypt(b);
  System.out.println("Criptografado " +  new String(c));

  byte[] d = Decript(c,"none".getBytes());
  System.out.println("Decriptografado " +  new String(d));

     System.out.println("Contains " +  new String(d).contains("String"));
  
 }


public static byte[] Decript(byte[] message, byte[] key) {
    try {
        SecretKeySpec keySpec = new SecretKeySpec(key, "Blowfish");

        Cipher cipher = Cipher.getInstance("Blowfish");

        cipher.init(Cipher.DECRYPT_MODE, keySpec);



        System.out.println("message size (bytes) = "+ message.length);

         return cipher.doFinal(message);

    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

    public static byte[] Encrypt(byte[] message){
        try {
            byte[] key = "none".getBytes();
            SecretKeySpec keySpec = new SecretKeySpec(key, "Blowfish");

            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            return cipher.doFinal(message);

        } catch (Exception e) {
            // don't try this at home
            e.printStackTrace();
        }
        return null;
    }
}