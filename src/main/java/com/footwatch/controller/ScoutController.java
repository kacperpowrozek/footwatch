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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@Secured({"ROLE_SCOUT"})
@RequestMapping("/scout")
public class ScoutController {

    final ScoutService scoutService;
    final MonitoringService monitoringService;
    final PlayerService playerService;
    final MatchService matchService;
    final MatchEvaluationScoutService matchEvaluationScoutService;
    final MatchEvaluationPlayerService matchEvaluationPlayerService;

    public ScoutController(ScoutService scoutService, MonitoringService monitoringService, PlayerService playerService,
                           MatchService matchService, MatchEvaluationScoutService matchEvaluationScoutService,
                           MatchEvaluationPlayerService matchEvaluationPlayerService) {
        this.scoutService = scoutService;
        this.monitoringService = monitoringService;
        this.playerService = playerService;
        this.matchService = matchService;
        this.matchEvaluationScoutService = matchEvaluationScoutService;
        this.matchEvaluationPlayerService = matchEvaluationPlayerService;
    }

    @GetMapping
    public String scoutInfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Scout scout = scoutService.findByUsername(authentication.getName());
        if (scout == null) {
            model.addAttribute("errorText", "Could not find scout with given ID!");
            return "wrong_data";
        } else {
            model.addAttribute("scout", scout);
            return "index_scout";
        }
    }

    @RequestMapping("/monitoring")
    public String scoutMonitoring(Model model, @RequestParam(name = "successText", required = false) String successText) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Scout scout = scoutService.findByUsername(authentication.getName());
        if (scout == null) {
            model.addAttribute("errorText", "There were some problems with desired Scout or his monitored players!");
            return "wrong_data";
        } else {
            List<Player> monitoredPlayers = monitoringService.getAllMonitoredPlayers(authentication.getName());
            List<Player> playersWithPendingRequestFromScout = monitoringService.getAllPlayersWithPendingRequest(authentication.getName());
            List<Monitoring> scoutMonitoringList = new ArrayList<>(scout.getMonitoring());
            List<Monitoring> scoutMonitoringAcceptedList = scoutMonitoringList.stream().filter(Monitoring::isAcceptedByPlayer).collect(Collectors.toList());
            List<Monitoring> scoutMonitoringRequested = scoutMonitoringList.stream().filter(sma -> !sma.isAcceptedByPlayer()).collect(Collectors.toList());
            scoutMonitoringAcceptedList.sort(Comparator.comparing(Monitoring::getStartDate).reversed());
            scoutMonitoringRequested.sort(Comparator.comparing(Monitoring::isAcceptedByPlayer));
            model.addAttribute("monitoredPlayers", monitoredPlayers);
            model.addAttribute("playersWithPendingRequestFromScout", playersWithPendingRequestFromScout);
            model.addAttribute("scout", scout);
            model.addAttribute("scoutMonitoringAcceptedList", scoutMonitoringAcceptedList);
            model.addAttribute("scoutMonitoringRequested", scoutMonitoringRequested);
            model.addAttribute("successText", successText);
            return "scout_monitoring";
        }
    }

    @RequestMapping("/monitoring/open_player/{id}")
    public String scoutMonitoringOpenPlayer(@PathVariable(value = "id") String id, @RequestParam(name = "selectedSeason", required = false) Optional<String> selectedSeason, Model model) {
        Player player = playerService.getOne(Long.parseLong(id));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Scout scout = scoutService.findByUsername(authentication.getName());
        if (monitoringService.exists(player.getId(), scout.getId())) {
            List<String> seasons = Stream.of(Season.values()).map(Season::toString).collect(Collectors.toList());
            Collections.reverse(seasons);
            String season = selectedSeason.orElse("None");

            model.addAttribute("player", player);
            List<Match> playerMatches = player.getMatches().stream()
                    .filter(match -> season.equals("None") || match.getSeason().toString().equals(season)).sorted(Comparator.comparing(Match::getDate, Comparator.reverseOrder())).collect(Collectors.toList());

            model.addAttribute("playerMatches", playerMatches);
            model.addAttribute("selectedSeason", season);
            model.addAttribute("seasons", seasons);
            return "scout_monitoring_open_player";
        } else {
            model.addAttribute("errorText", "Monitoring does not exist, could not open player profile!");
            return "wrong_data";
        }
    }

    @RequestMapping("/monitoring/new_request")
    public ModelAndView scoutMonitoringNewRequestPostMapping(ModelMap modelMap,
                                                             @RequestParam(name = "errorText", required = false)
                                                                     String errorText) {
        modelMap.addAttribute("errorText", errorText);
        return new ModelAndView("scout_monitoring_new_request", modelMap);
    }

    @PostMapping("/monitoring/new_request")
    public ModelAndView scoutMonitoringNewRequestGetMapping(@RequestBody MultiValueMap<String, String> formData,
                                                            ModelMap modelMap) {
        Map<String, String> singleValueMap = formData.toSingleValueMap();
        List<Player> players = playerService.findPlayersByNameAndSurname(singleValueMap.get("name"),
                singleValueMap.get("surname"));
        modelMap.addAttribute("players", players);
        modelMap.addAttribute("successText", "search");
        return new ModelAndView("scout_monitoring_new_request", modelMap);
    }

    @RequestMapping("/monitoring/send_request/{id}")
    public ModelAndView scoutMonitoringSendRequest(@PathVariable(value = "id") String id, ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long scoutIdLong = scoutService.findByUsername(authentication.getName()).getId();
        Long playerIdLong = Long.parseLong(id);
        if (!monitoringService.exists(playerIdLong, scoutIdLong)) {
            Monitoring newMonitoring = new Monitoring();
            MonitoringPK newMonitoringPK = new MonitoringPK();
            newMonitoringPK.setPlayerid(playerIdLong);
            newMonitoringPK.setScoutid(scoutIdLong);
            newMonitoring.setId(newMonitoringPK);
            newMonitoring.setScout(scoutService.getOne(scoutIdLong));
            newMonitoring.setPlayer(playerService.getOne(playerIdLong));
            newMonitoring.setAcceptedByPlayer(false);
            monitoringService.createNew(newMonitoring);
            modelMap.addAttribute("successText", "Request was sent to a chosen player!");
            return new ModelAndView("redirect:/scout/monitoring", modelMap);
        } else {
            modelMap.addAttribute("errorText", "You've already sent request to this player!");
            return new ModelAndView("redirect:/scout/monitoring/new_request", modelMap);
        }
    }

    @RequestMapping("/monitoring/delete_monitoring_request/{player_id}")
    public ModelAndView scoutMonitoringDeleteRequest(@PathVariable(value = "player_id") String playerId, ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long scoutIdLong = scoutService.findByUsername(authentication.getName()).getId();
        Long playerIdLong = Long.parseLong(playerId);
        monitoringService.deleteMonitoring(scoutIdLong, playerIdLong);
        return new ModelAndView("redirect:/scout/monitoring", modelMap);
    }

    @RequestMapping("/monitoring/open_player/{player_id}/open_match/{match_id}")
    public String scoutMonitoringOpenMatch(@PathVariable(value = "match_id") String matchId, Model model,
                                           @PathVariable(value = "player_id") String playerId,
                                           @RequestParam(name = "successText", required = false) String successText) {
        Match match = matchService.getMatchById(Long.parseLong(matchId));
        MatchEvaluationPlayer mep = matchEvaluationPlayerService.findByMatch(match);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Scout scout = scoutService.findByUsername(authentication.getName());
        MatchEvaluationScout mes = matchEvaluationScoutService.findOne(match, scout);

        float playerScore = (mep.getEvaluationDefense() + mep.getEvaluationEngagement() + mep.getEvaluationOffense() + mep.getEvaluationTacticalDiscipline() + mep.getEvaluationTeamPlay()) / 5f;
        float scoutScore = mes != null
                ? (mes.getEvaluationDefense() + mes.getEvaluationEngagement() + mes.getEvaluationOffense() + mes.getEvaluationTacticalDiscipline() + mes.getEvaluationTeamPlay()) / 5f
                : 0;

        model.addAttribute("playerScore", playerScore);
        model.addAttribute("scoutScore", scoutScore);
        model.addAttribute("match", match);
        model.addAttribute("matchEvaluationPlayer", mep);
        model.addAttribute("matchEvaluationScout", mes);
        model.addAttribute("successText", successText);
        model.addAttribute("playerId", playerId);
        Player player = playerService.getOne(Long.parseLong(playerId));
        model.addAttribute("player", player);
        return "scout_monitoring_open_match";
    }

    @RequestMapping("/monitoring/open_player/{player_id}/open_match/{match_id}/add_scout_evaluation")
    public String scoutMonitoringOpenMatchAddScoutEvaluationRequestMapping(@PathVariable(value = "match_id") String matchId,
                                                                           @PathVariable(value = "player_id") String playerId,
                                                                           Model model) {
        model.addAttribute("matchId", matchId);
        model.addAttribute("playerId", playerId);
        return "scout_evaluation_add";
    }

    @PostMapping("/monitoring/open_player/{player_id}/open_match/{match_id}/add_scout_evaluation")
    public ModelAndView scoutMonitoringOpenMatchAddScoutEvaluationPostMapping(@PathVariable(value = "match_id") String matchId,
                                                                              @PathVariable(value = "player_id") String playerId,
                                                                              ModelMap modelMap,
                                                                              @RequestBody MultiValueMap<String, String> formData) {
        Map<String, String> singleValueMap = formData.toSingleValueMap();
        Match match = matchService.getMatchById(Long.parseLong(matchId));
        MatchEvaluationScout newMatchEvaluationScout = new MatchEvaluationScout();
        newMatchEvaluationScout.setGoals(Integer.parseInt(singleValueMap.get("goals")));
        newMatchEvaluationScout.setAssists(Integer.parseInt(singleValueMap.get("assists")));
        newMatchEvaluationScout.setChancesCreated(Integer.parseInt(singleValueMap.get("chances_created")));
        newMatchEvaluationScout.setShots(Integer.parseInt(singleValueMap.get("shots")));
        newMatchEvaluationScout.setShotsOnTarget(Integer.parseInt(singleValueMap.get("shots_on_target")));
        newMatchEvaluationScout.setPassesCompleted(Integer.parseInt(singleValueMap.get("passes_completed")));
        newMatchEvaluationScout.setPasses(Integer.parseInt(singleValueMap.get("passes")));
        newMatchEvaluationScout.setTackles(Integer.parseInt(singleValueMap.get("tackles")));
        newMatchEvaluationScout.setFouls(Integer.parseInt(singleValueMap.get("fouls")));
        newMatchEvaluationScout.setInterceptions(Integer.parseInt(singleValueMap.get("interceptions")));
        newMatchEvaluationScout.setPlayedAs(singleValueMap.get("played_as"));
        newMatchEvaluationScout.setEvaluationTacticalDiscipline(Integer.parseInt(singleValueMap.get("tactical_discipline")));
        newMatchEvaluationScout.setEvaluationTeamPlay(Integer.parseInt(singleValueMap.get("team_play")));
        newMatchEvaluationScout.setEvaluationDefense(Integer.parseInt(singleValueMap.get("defense")));
        newMatchEvaluationScout.setEvaluationOffense(Integer.parseInt(singleValueMap.get("offense")));
        newMatchEvaluationScout.setEvaluationEngagement(Integer.parseInt(singleValueMap.get("engagement")));
        newMatchEvaluationScout.setEvaluationVerbal(singleValueMap.get("verbal_evaluation"));
        newMatchEvaluationScout.setMatch(match);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Scout scout = scoutService.findByUsername(authentication.getName());
        newMatchEvaluationScout.setScout(scout);
        matchEvaluationScoutService.createNew(newMatchEvaluationScout);
        modelMap.addAttribute("successText", "Scout evaluation was successfully added to this match!");
        return new ModelAndView("redirect:/scout/monitoring/open_player/" + playerId + "/open_match/" + matchId, modelMap);
    }

    @RequestMapping("/monitoring/open_player/{player_id}/delete_monitoring")
    public ModelAndView scoutMonitoringDeleteMonitoring(@PathVariable(value = "player_id") String playerId, ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long scoutIdLong = scoutService.findByUsername(authentication.getName()).getId();
        Long playerIdLong = Long.parseLong(playerId);
        monitoringService.deleteMonitoring(scoutIdLong, playerIdLong);
        return new ModelAndView("redirect:/scout/monitoring", modelMap);
    }
}
