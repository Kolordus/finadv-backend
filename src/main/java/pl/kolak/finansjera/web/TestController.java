package pl.kolak.finansjera.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class TestController {


    @GetMapping("/halo")
    public void method(HttpServletResponse resp, HttpServletRequest req) {
        if (req.getCookies() != null)
            for (Cookie cookie : req.getCookies()) {
                System.out.println(cookie.getComment());
                System.out.println(cookie.getDomain());
                System.out.println(cookie.getMaxAge());
                System.out.println(cookie.getName());
                System.out.println(cookie.getPath());
                System.out.println(cookie.getSecure());
                System.out.println(cookie.getValue());
                System.out.println(cookie.getVersion());
                System.out.println("===");
            }


        Cookie cookie = new Cookie("halo", "asadaasdasd");
        cookie.setMaxAge(50);
        resp.addCookie(cookie);
    }
}
