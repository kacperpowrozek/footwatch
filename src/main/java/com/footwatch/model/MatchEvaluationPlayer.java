package com.footwatch.model;

import javax.persistence.*;

@Entity
@Table(name="match_evaluations_player")
public class MatchEvaluationPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="evaluation_tactical_discipline")
    private int evaluationTacticalDiscipline;
    @Column(name="evaluation_team_play")
    private int evaluationTeamPlay;
    @Column(name="evaluation_defense")
    private int evaluationDefense;
    @Column(name="evaluation_offense")
    private int evaluationOffense;
    @Column(name="evaluation_engagement")
    private int evaluationEngagement;
    @Column(name="evaluation_verbal", length = 10000)
    private String evaluationVerbal;

    @OneToOne
    @JoinColumn(name="match_id", nullable = false)
    private Match match;

    public void setId(Long id) {
        this.id = id;
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

    public Long getId() {
        return id;
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
}
