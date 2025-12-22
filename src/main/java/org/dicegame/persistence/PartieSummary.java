package org.dicegame.persistence;

import java.time.OffsetDateTime;

public class PartieSummary {
    private final long idPartie;
    private final int nbTours;
    private final int pointsSi7;
    private final Long joueurId;
    private final int scoreFinal;
    private final OffsetDateTime startedAt;
    private final OffsetDateTime finishedAt;

    public PartieSummary(long idPartie, int nbTours, int pointsSi7, Long joueurId, int scoreFinal, OffsetDateTime startedAt, OffsetDateTime finishedAt) {
        this.idPartie = idPartie;
        this.nbTours = nbTours;
        this.pointsSi7 = pointsSi7;
        this.joueurId = joueurId;
        this.scoreFinal = scoreFinal;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }

    public long getIdPartie() { return idPartie; }
    public int getNbTours() { return nbTours; }
    public int getPointsSi7() { return pointsSi7; }
    public Long getJoueurId() { return joueurId; }
    public int getScoreFinal() { return scoreFinal; }
    public OffsetDateTime getStartedAt() { return startedAt; }
    public OffsetDateTime getFinishedAt() { return finishedAt; }
}
