package com.footwatch.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MonitoringPK implements Serializable {

    @Column(name = "player_id")
    private Long playerid;

    @Column(name = "scout_id")
    private Long scoutid;

    public MonitoringPK() {

    }

    public MonitoringPK(Long playerid, Long scoutid) {
        this.playerid = playerid;
        this.scoutid = scoutid;
    }

    public Long getPlayerid() {
        return playerid;
    }

    public Long getScoutid() {
        return scoutid;
    }

    public void setPlayerid(Long playerid) {
        this.playerid = playerid;
    }

    public void setScoutid(Long scoutid) {
        this.scoutid = scoutid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonitoringPK that = (MonitoringPK) o;
        return playerid.equals(that.playerid) &&
                scoutid.equals(that.scoutid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerid, scoutid);
    }
}
