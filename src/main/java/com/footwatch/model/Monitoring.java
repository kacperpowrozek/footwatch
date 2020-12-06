package com.footwatch.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "monitoring")
public class Monitoring implements Serializable {

    @EmbeddedId
    private MonitoringPK id;

    @ManyToOne
    @MapsId("playerId")
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne
    @MapsId("scoutId")
    @JoinColumn(name = "scout_id", nullable = false)
    private Scout scout;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "is_accepted_by_player", columnDefinition="BOOLEAN DEFAULT false")
    private boolean isAcceptedByPlayer;

    public MonitoringPK getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public Scout getScout() {
        return scout;
    }

    public Date getStartDate() {
        return startDate;
    }

    public boolean isAcceptedByPlayer() {
        return isAcceptedByPlayer;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setScout(Scout scout) {
        this.scout = scout;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setAcceptedByPlayer(boolean acceptedByPlayer) {
        isAcceptedByPlayer = acceptedByPlayer;
    }

    public void setId(MonitoringPK id) {
        this.id = id;
    }
}
