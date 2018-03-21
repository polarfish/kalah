package space.polarfish.kalah.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import space.polarfish.kalah.game.Player;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.concurrent.atomic.AtomicLong;

public class PlayerInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private HttpSession session;

    private final AtomicLong playerNumberGenerator = new AtomicLong(0);

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (session.getAttribute("player") == null) {
            session.setAttribute("player", new Player("Player_" + playerNumberGenerator.incrementAndGet()));
        }

        return true;
    }
}