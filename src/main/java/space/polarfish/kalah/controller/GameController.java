package space.polarfish.kalah.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import space.polarfish.kalah.exceptions.GameException;
import space.polarfish.kalah.game.GameBoard;
import space.polarfish.kalah.game.GameStatus;
import space.polarfish.kalah.game.Player;
import space.polarfish.kalah.manager.GameManager;
import space.polarfish.kalah.model.GameBoardModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
public class GameController {

    private HttpSession session;
    private GameManager manager;
    private HttpServletRequest request;

    @Autowired
    public GameController(HttpSession session, GameManager manager, HttpServletRequest request) {
        this.session = session;
        this.manager = manager;
        this.request = request;
    }

    @GetMapping("/game")
    public String game() {
        if (session.getAttribute("currentGameId") != null) {
            return "redirect:/game/" + session.getAttribute("currentGameId");
        }
        return "game";
    }

    @GetMapping("/game/{gameIdString}")
    public String game(@PathVariable String gameIdString, Model model) {
        try {
            UUID gameId = UUID.fromString(gameIdString);
            GameBoard gameBoard = manager.getGameById(gameId);

            model.addAttribute("gameId", gameId);

            GameStatus status = gameBoard.getStatus();
            model.addAttribute("status", status);

            if (status == GameStatus.NOT_STARTED) {
                model.addAttribute("joinURL", request.getScheme() + "://"
                        + request.getServerName()
                        + ((request.getServerPort() == 80 || request.getServerPort() == 443) ? "" : (":" + request.getServerPort()))
                        + "/game/" + gameId.toString() + "/join");
            } else {

                Player player = (Player) session.getAttribute("player");
                GameBoardModel gameBoardModel = gameBoard.getModel(player);
                model.addAttribute("board", gameBoardModel);
                model.addAttribute("currentUserIsPlayer", player.equals(gameBoardModel.getPlayer()));
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Invalid game identifier: " + gameIdString);
        } catch (GameException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "board";
    }

    @PostMapping("/game/create")
    public String createGame(Model model) {
        if (session.getAttribute("currentGameId") != null) {
            model.addAttribute("error", "You are already playing the game " + session.getAttribute("currentGameId"));
            return "error";
        }

        UUID gameId = manager.createGame((Player) session.getAttribute("player"));
        session.setAttribute("currentGameId", gameId);

        return "redirect:/game/" + gameId.toString();
    }

    @PostMapping("/game/leave")
    public String leaveGame(Model model) {
        if (session.getAttribute("currentGameId") == null) {
            model.addAttribute("error", "You are not playing any game currently");
            return "error";
        }

        UUID gameId = (UUID) session.getAttribute("currentGameId");
        GameBoard gameBoard = manager.getGameById(gameId);

        if (gameBoard != null) {
            gameBoard.abort();
        }

        session.removeAttribute("currentGameId");

        return "redirect:/game";
    }

    @GetMapping("/game/{gameIdString}/join")
    public String joinGamePage(@PathVariable String gameIdString, Model model) {
        try {
            UUID gameId = UUID.fromString(gameIdString);
            manager.getGameById(gameId);
            model.addAttribute("gameId", gameId);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Invalid game identifier: " + gameIdString);
        } catch (GameException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "join";
    }

    @PostMapping("/game/join")
    public String joinGame(@RequestParam String gameIdString, Model model) {
        try {
            UUID gameId = UUID.fromString(gameIdString);
            Player player = (Player) session.getAttribute("player");
            manager.getGameById(gameId).joinGame(player);
            session.setAttribute("currentGameId", gameId);
            return "redirect:/game/" + gameId.toString();
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Invalid game identifier: " + gameIdString);
        } catch (GameException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "error";
    }

    @PostMapping("/game/turn")
    public String joinGame(@RequestParam Integer pitIndex, Model model) {
        try {
            UUID currentGameId = (UUID) session.getAttribute("currentGameId");

            if (currentGameId == null) {
                model.addAttribute("error", "You do not participate in a game");
                return "error";
            }

            Player player = (Player) session.getAttribute("player");
            manager.getGameById(currentGameId).makeTurn(player, pitIndex);

            return "redirect:/game/" + currentGameId;
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Invalid game identifier");
            return "error";
        } catch (GameException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}

