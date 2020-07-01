/**
 * Created by fyh on 2020/7/1.
 */

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @ClassName mainJava
 * @Description DOTO
 * @Author fyh
 * Date 2020/7/116:06
 */
public class mainJava {
    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String admins = bCryptPasswordEncoder.encode("admin");
        bCryptPasswordEncoder.matches("123",admins);
    }
}
