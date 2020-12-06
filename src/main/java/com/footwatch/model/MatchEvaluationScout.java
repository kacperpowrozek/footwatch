package com.footwatch.model;

import javax.persistence.*;

@Entity
@Table(name = "match_evaluations_scout")
public class MatchEvaluationScout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private int goals;
    @Column
    private int assists;
    @Column(name = "chances_created")
    private int chancesCreated;
    @Column
    private int shots;
    @Column(name = "shots_on_target")
    private int shotsOnTarget;
    @Column
    private int passes;
    @Column(name = "passes_completed")
    private int passesCompleted;
    @Column
    private int tackles;
    @Column
    private int fouls;
    @Column
    private int interceptions;
    @Column(name = "played_as")
    private String playedAs;
    @Column(name = "evaluation_tactical_discipline")
    private int evaluationTacticalDiscipline;
    @Column(name = "evaluation_team_play")
    private int evaluationTeamPlay;
    @Column(name = "evaluation_defense")
    private int evaluationDefense;
    @Column(name = "evaluation_offense")
    private int evaluationOffense;
    @Column(name = "evaluation_engagement")
    private int evaluationEngagement;
    @Column(name = "evaluation_verbal", length = 10000)
    private String evaluationVerbal;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne
    @JoinColumn(name = "scout_id", nullable = false)
    private Scout scout;


    public Long getId() {
        return id;
    }

    public int getGoals() {
        return goals;
    }

    public int getAssists() {
        return assists;
    }

    public int getChancesCreated() {
        return chancesCreated;
    }

    public int getShots() {
        return shots;
    }

    public int getShotsOnTarget() {
        return shotsOnTarget;
    }

    public int getPasses() {
        return passes;
    }

    public int getPassesCompleted() {
        return passesCompleted;
    }

    public int getTackles() {
        return tackles;
    }

    public int getFouls() {
        return fouls;
    }

    public int getInterceptions() {
        return interceptions;
    }

    public String getPlayedAs() {
        return playedAs;
    }

    public int getEvaluationTacticalDiscipline() {
        return evaluationTacticalDiscipline;
    }

    public int getEvaluationTeamPlay() {
        return evaluationTeamPlay;
    }

    public int getEvaluationDefense() {
        return evaluationDefense;
    }

    public int getEvaluationOffense() {
        return evaluationOffense;
    }

    public int getEvaluationEngagement() {
        return evaluationEngagement;
    }

    public String getEvaluationVerbal() {
        return evaluationVerbal;
    }

    public Match getMatch() {
        return match;
    }

    public Scout getScout() {
        return scout;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public void setChancesCreated(int chancesCreated) {
        this.chancesCreated = chancesCreated;
    }

    public void setShots(int shots) {
        this.shots = shots;
    }

    public void setShotsOnTarget(int shotsOnTarget) {
        this.shotsOnTarget = shotsOnTarget;
    }

    public void setPasses(int passes) {
        this.passes = passes;
    }

    public void setPassesCompleted(int passesCompleted) {
        this.passesCompleted = passesCompleted;
    }

    public void setTackles(int tackles) {
        this.tackles = tackles;
    }

    public void setFouls(int fouls) {
        this.fouls = fouls;
    }

    public void setInterceptions(int interceptions) {
        this.interceptions = interceptions;
    }

    public void setPlayedAs(String playedAs) {
        this.playedAs = playedAs;
    }

    public void setEvaluationTacticalDiscipline(int evaluationTacticalDiscipline) {
        this.evaluationTacticalDiscipline = evaluationTacticalDiscipline;
    }

    public void setEvaluationTeamPlay(int evaluationTeamPlay) {
        this.evaluationTeamPlay = evaluationTeamPlay;
    }

    public void setEvaluationDefense(int evaluationDefense) {
        this.evaluationDefense = evaluationDefense;
    }

    public void setEvaluationOffense(int evaluationOffense) {
        this.evaluationOffense = evaluationOffense;
    }

    public void setEvaluationEngagement(int evaluationEngagement) {
        this.evaluationEngagement = evaluationEngagement;
    }

    public void setEvaluationVerbal(String evaluationVerbal) {
        this.evaluationVerbal = evaluationVerbal;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public void setScout(Scout scout) {
        this.scout = scout;
    }
}
