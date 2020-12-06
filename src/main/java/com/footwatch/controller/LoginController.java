package com.footwatch.controller;

import com.footwatch.model.Player;
import com.footwatch.model.Scout;
import com.footwatch.service.PlayerService;
import com.footwatch.service.ScoutService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import java.sql.Date;
import java.util.Map;
import java.util.Optional;

@Controller
public class LoginController {

    final ScoutService scoutService;
    final PlayerService playerService;

    public LoginController(ScoutService scoutService, PlayerService playerService) {
        this.scoutService = scoutService;
        this.playerService = playerService;
    }

    @GetMapping("/")
    public ModelAndView loginPageRedirect(ModelMap modelMap) {
        return new ModelAndView("redirect:/login", modelMap);
    }

    @GetMapping("/login")
    public ModelAndView loginPage(@RequestParam(name="successText", required = false) String successText,
                                  @RequestParam(name="error", required = false) Optional<String> error,
                            @RequestParam(name="errorText", required = false) String errorText, ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            for (GrantedAuthority ga : authentication.getAuthorities()) {
                if(ga.getAuthority().equals("ROLE_PLAYER")) {
                    return new ModelAndView("redirect:/player", modelMap);
                }
                else if(ga.getAuthority().equals("ROLE_SCOUT")) {
                    return new ModelAndView("redirect:/scout", modelMap);
                }
            }
        }
        modelMap.addAttribute("successText", successText);
        modelMap.addAttribute("error", error.isPresent());
        modelMap.addAttribute("errorText", "Provided username and/or password are wrong!");
        return new ModelAndView("login", modelMap);
    }

    @GetMapping("/register/scout")
    public ModelAndView registerPageScout(@RequestParam(name="errorText", required = false) String errorText,
                                    ModelMap modelMap) {
        modelMap.addAttribute("errorText", errorText);
        return new ModelAndView("register_scout", modelMap);
    }

    @PostMapping("/register/scout")
    public ModelAndView receiveRegisterDataScout(@RequestBody MultiValueMap<String, String> formData, ModelMap modelMap) {
        Map<String, String> singleValueMap = formData.toSingleValueMap();
        if(!scoutService.exists(singleValueMap.get("username"))
            && !playerService.exists(singleValueMap.get("username"))) {
            Scout newScout = new Scout();
            newScout.setName(singleValueMap.get("name"));
            newScout.setSurname(singleValueMap.get("surname"));
            newScout.setUsername(singleValueMap.get("username"));
            newScout.setPassword(new BCryptPasswordEncoder().encode(singleValueMap.get("password")));
            newScout.setDateOfBirth(Date.valueOf(singleValueMap.get("date_of_birth")));
            newScout.setNationality(singleValueMap.get("nationality"));
            newScout.setLicenseNo(singleValueMap.get("license_no"));
            newScout.setPhoneNumber(singleValueMap.get("phone_number"));
            newScout.setEmail(singleValueMap.get("email"));
            scoutService.createNew(newScout);
            modelMap.addAttribute("successText", "Scout account created successfully!");
            return new ModelAndView("redirect:/login", modelMap);
        }
        else {
            modelMap.addAttribute("errorText", "Provided username already exists!");
            return new ModelAndView("redirect:/register/scout", modelMap);
        }
    }

    @GetMapping("register/player")
    public ModelAndView registerPagePlayer(@RequestParam(name="errorText", required = false) String errorText,
                                           ModelMap modelMap) {
        modelMap.addAttribute("errorText", errorText);
        return new ModelAndView("register_player", modelMap);
    }

    @PostMapping("register/player")
    public ModelAndView receiveRegisterDataPlayer(@RequestBody MultiValueMap<String, String> formData, ModelMap modelMap) {
        Map<String, String> singleValueMap = formData.toSingleValueMap();
        if(!scoutService.exists(singleValueMap.get("username"))
                && !playerService.exists(singleValueMap.get("username"))) {
            Player newPlayer = new Player();
            newPlayer.setName(singleValueMap.get("name"));
            newPlayer.setSurname(singleValueMap.get("surname"));
            newPlayer.setUsername(singleValueMap.get("username"));
            newPlayer.setPassword(new BCryptPasswordEncoder().encode(singleValueMap.get("password")));
            newPlayer.setDateOfBirth(Date.valueOf(singleValueMap.get("date_of_birth")));
            newPlayer.setNationality(singleValueMap.get("nationality"));
            newPlayer.setTeamName(singleValueMap.get("team_name"));
            newPlayer.setPhoneNumber(singleValueMap.get("phone_number"));
            newPlayer.setEmail(singleValueMap.get("email"));
            playerService.createNew(newPlayer);
            modelMap.addAttribute("successText", "Player account created successfully!");
            return new ModelAndView("redirect:/login", modelMap);
        }
        else {
            modelMap.addAttribute("errorText", "Provided username already exists!");
            return new ModelAndView("redirect:/register/player", modelMap);
        }
    }
}
