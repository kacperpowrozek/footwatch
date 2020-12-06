package com.footwatch.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "home_team")
    private String homeTeam;
    @Column(name = "away_team")
    private String awayTeam;
    @Column(name = "home_team_goals")
    private Integer homeTeamGoals;
    @Column(name = "away_team_goals")
    private Integer awayTeamGoals;
    @Column(name = "match_level")
    private MatchLevel matchLevel;
    @Column(name = "link_to_video")
    private String linkToVideo;
    @Column
    private Season season;
    @Column
    private Date date;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @OneToMany(mappedBy = "match")
    private Set<MatchEvaluationScout> matchEvaluationScouts = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "match_evaluation_player")
    private MatchEvaluationPlayer matchEvaluationPlayer;

    public Long getId() {
        return id;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public Integer getHomeTeamGoals() {
        return homeTeamGoals;
    }

    public Integer getAwayTeamGoals() {
        return awayTeamGoals;
    }

    public MatchLevel getMatchLevel() {
        return matchLevel;
    }

    public String getLinkToVideo() {
        return linkToVideo;
    }

    public Season getSeason() {
        return season;
    }

    public Date getDate() {
        return date;
    }

    public Player getPlayer() {
        return player;
    }

    public Set<MatchEvaluationScout> getMatchEvaluationScouts() {
        return matchEvaluationScouts;
    }

    public MatchEvaluationPlayer getMatchEvaluationPlayer() {
        return matchEvaluationPlayer;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public void setHomeTeamGoals(Integer homeTeamGoals) {
        this.homeTeamGoals = homeTeamGoals;
    }

    public void setAwayTeamGoals(Integer awayTeamGoals) {
        this.awayTeamGoals = awayTeamGoals;
    }

    public void setMatchLevel(MatchLevel matchLevel) {
        this.matchLevel = matchLevel;
    }

    public void setLinkToVideo(String linkToVideo) {
        this.linkToVideo = linkToVideo;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setMatchEvaluationPlayer(MatchEvaluationPlayer matchEvaluationPlayer) {
        this.matchEvaluationPlayer = matchEvaluationPlayer;
    }
}
