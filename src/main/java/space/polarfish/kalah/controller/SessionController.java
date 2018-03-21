package space.polarfish.kalah.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;
import space.polarfish.kalah.game.Player;

import javax.servlet.http.HttpSession;

@Controller
public class SessionController {

    @Autowired
    private HttpSession session;

    @GetMapping("/")
    public String landing() {
        return "redirect:/game";
    }

    @GetMapping("/user")
    public String login() {
        return "user";
    }

    @PostMapping("/user")
    public String setName(@RequestParam(required = false) String username) {
        if (!StringUtils.isEmptyOrWhitespace(username)) {
            Player player = (Player) session.getAttribute("player");

            if (player == null) {
                session.setAttribute("player", new Player(username));
            } else {
                player.setName(username);
            }

            return "redirect:/game";
        }

        return "user";
    }
}
