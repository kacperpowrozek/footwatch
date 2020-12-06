package com.footwatch.controller;

import com.footwatch.model.*;
import com.footwatch.service.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@Secured({"ROLE_PLAYER"})
@RequestMapping("/player")
public class PlayerController {

    final PlayerService playerService;
    final ScoutService scoutService;
    final MonitoringService monitoringService;
    final MatchService matchService;
    final MatchEvaluationPlayerService matchEvaluationPlayerService;
    final MatchEvaluationScoutService matchEvaluationScoutService;

    public PlayerController(PlayerService playerService, ScoutService scoutService, MonitoringService monitoringService,
                            MatchService matchService, MatchEvaluationPlayerService matchEvaluationPlayerService, MatchEvaluationScoutService matchEvaluationScoutService) {
        this.playerService = playerService;
        this.scoutService = scoutService;
        this.monitoringService = monitoringService;
        this.matchService = matchService;
        this.matchEvaluationPlayerService = matchEvaluationPlayerService;
        this.matchEvaluationScoutService = matchEvaluationScoutService;
    }

    @GetMapping
    public String playerInfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Player player = playerService.findByUsername(authentication.getName());
        if (player == null) {
            model.addAttribute("errorText", "Could not find player with given ID!");
            return "wrong_data";
        } else {
            model.addAttribute("player", player);
            return "index_player";
        }
    }

    @GetMapping("/matches")
    public String allMatches(Model model, @RequestParam(name = "selectedSeason", required = false) Optional<String> selectedSeason) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Player player = playerService.findByUsername(authentication.getName());
        if (player == null) {
            model.addAttribute("errorText", "Could not find player");
            return "wrong_data";
        } else {
            List<String> seasons = Stream.of(Season.values()).map(Season::toString).collect(Collectors.toList());
            Collections.reverse(seasons);
            String season = selectedSeason.orElse("None");

            List<Match> playerMatches = player.getMatches().stream()
                    .filter(match -> season.equals("None") || match.getSeason().toString().equals(season)).sorted(Comparator.comparing(Match::getDate, Comparator.reverseOrder())).collect(Collectors.toList());

            model.addAttribute("selectedSeason", season);
            model.addAttribute("player", player);
            model.addAttribute("playerMatches", playerMatches);
            model.addAttribute("seasons", seasons);
            return "player_matches";
        }
    }

    @GetMapping("matches/{id}")
    public String openMatch(@PathVariable(value = "id") String id, Model model) {
        Match match = matchService.getMatchById(Long.parseLong(id));
        if (match == null) {
            model.addAttribute("errorText", "Could not find match");
            return "wrong_data";
        } else {
            MatchEvaluationPlayer mep = matchEvaluationPlayerService.findByMatch(match);

            float score = (mep.getEvaluationDefense() + mep.getEvaluationEngagement() + mep.getEvaluationOffense() + mep.getEvaluationTacticalDiscipline() + mep.getEvaluationTeamPlay()) / 5f;

            model.addAttribute("score", score);
            model.addAttribute("match", match);
            model.addAttribute("matchEvaluationPlayer", mep);
            return "player_matches_open";
        }
    }

    @GetMapping("matches/{id}/scout_evaluations")
    public String openMatchSeeScoutEvaluations(@PathVariable(value = "id") String matchId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Player player = playerService.findByUsername(authentication.getName());
        List<Monitoring> playerMonitoringList = new ArrayList<>(player.getMonitoring());

        model.addAttribute("playerMonitoringList", playerMonitoringList);
        model.addAttribute("matchId", matchId);
        return "player_matches_open_scout_evaluations";
    }

    @GetMapping("matches/{id_match}/scout_evaluations/{id_scout}")
    public String openMatchSeeScoutEvaluation(@PathVariable(value = "id_match") String matchId,
                                              @PathVariable(value = "id_scout") String scoutId,
                                              Model model) {
        Scout scout = scoutService.getOne(Long.parseLong(scoutId));
        Match match = matchService.getMatchById(Long.parseLong(matchId));
        MatchEvaluationScout mes = matchEvaluationScoutService.findOne(match, scout);

        float score = mes != null
                ? (mes.getEvaluationDefense() + mes.getEvaluationEngagement() + mes.getEvaluationOffense() + mes.getEvaluationTacticalDiscipline() + mes.getEvaluationTeamPlay()) / 5f
                : 0;

        model.addAttribute("score", score);
        model.addAttribute("matchEvaluationScout", mes);
        model.addAttribute("match", match);
        model.addAttribute("scout", scout);
        return "player_matches_open_scout_evaluation";
    }

    @GetMapping("new_match")
    public String newMatch(Model model) {
        List<String> matchLevels = Stream.of(MatchLevel.values()).map(MatchLevel::name).collect(Collectors.toList());
        List<String> seasons = Stream.of(Season.values()).map(Season::toString).collect(Collectors.toList());
        Collections.reverse(seasons);
        model.addAttribute("matchLevels", matchLevels);
        model.addAttribute("seasons", seasons);
        return "player_matches_add";
    }

    @PostMapping("add_match")
    public ModelAndView addMatch(@RequestBody MultiValueMap<String, String> formData, ModelMap modelMap) {
        Map<String, String> singleValueMap = formData.toSingleValueMap();

        Match newMatch = new Match();
        newMatch.setHomeTeam(singleValueMap.get("home_team"));
        newMatch.setHomeTeamGoals(Integer.parseInt(singleValueMap.get("home_team_goals")));
        newMatch.setAwayTeam(singleValueMap.get("away_team"));
        newMatch.setAwayTeamGoals(Integer.parseInt(singleValueMap.get("away_team_goals")));
        newMatch.setMatchLevel(MatchLevel.valueOf(singleValueMap.get("match_level")));
        newMatch.setDate(Date.valueOf(singleValueMap.get("date")));
        newMatch.setLinkToVideo(singleValueMap.get("link_to_video"));
        newMatch.setSeason(Season.getEnum(singleValueMap.get("season")));

        MatchEvaluationPlayer newMatchEvaluationPlayer = new MatchEvaluationPlayer();
        newMatchEvaluationPlayer.setEvaluationTacticalDiscipline(Integer.parseInt(singleValueMap.get("tactical_discipline")));
        newMatchEvaluationPlayer.setEvaluationTeamPlay(Integer.parseInt(singleValueMap.get("team_play")));
        newMatchEvaluationPlayer.setEvaluationDefense(Integer.parseInt(singleValueMap.get("defense")));
        newMatchEvaluationPlayer.setEvaluationOffense(Integer.parseInt(singleValueMap.get("offense")));
        newMatchEvaluationPlayer.setEvaluationEngagement(Integer.parseInt(singleValueMap.get("engagement")));
        newMatchEvaluationPlayer.setEvaluationVerbal(singleValueMap.get("verbal_evaluation"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Player player = playerService.findByUsername(authentication.getName());

        newMatch.setPlayer(player);
        matchService.createNew(newMatch);

        newMatchEvaluationPlayer.setMatch(newMatch);
        matchEvaluationPlayerService.createNew(newMatchEvaluationPlayer);

        modelMap.addAttribute("successText", "New match was created");
        return new ModelAndView("redirect:/player/matches", modelMap);
    }

    @GetMapping("/monitoring")
    public String playerMonitoring(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Player player = playerService.findByUsername(authentication.getName());
        if (player == null) {
            model.addAttribute("errorText", "There were some problems with desired Scout or his monitored players!");
            return "wrong_data";
        } else {
            List<Scout> monitoringScouts = monitoringService.getAllMonitoringScouts(authentication.getName());
            List<Scout> monitoringRequests = monitoringService.getAllScoutsWithPendingRequests(authentication.getName());
            List<Monitoring> playerMonitoringList = new ArrayList<>(player.getMonitoring());
            model.addAttribute("monitoringScouts", monitoringScouts);
            model.addAttribute("monitoringRequests", monitoringRequests);
            model.addAttribute("playerMonitoringList", playerMonitoringList);
            model.addAttribute("player", player);
            return "player_monitoring";
        }
    }

    @GetMapping("/monitoring/accept/{id}")
    public ModelAndView playerMonitoringAccept(@PathVariable(value = "id") String id, ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long playerIdLong = playerService.findByUsername(authentication.getName()).getId();
        Long scoutIdLong = Long.parseLong(id);
        monitoringService.acceptMonitoring(scoutIdLong, playerIdLong);
        return new ModelAndView("redirect:/player/monitoring", modelMap);
    }

    @GetMapping("/monitoring/delete/{scoutId}")
    public ModelAndView playerMonitoringDelete(@PathVariable(value = "scoutId") String scoutId, ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long playerIdLong = playerService.findByUsername(authentication.getName()).getId();
        Long scoutIdLong = Long.parseLong(scoutId);
        monitoringService.deleteMonitoring(scoutIdLong, playerIdLong);
        return new ModelAndView("redirect:/player/monitoring", modelMap);
    }
}
