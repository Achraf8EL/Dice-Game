package org.dicegame.tools;

import java.util.List;

import javax.sql.DataSource;

import org.dicegame.persistence.DataSourceProvider;
import org.dicegame.persistence.PartieDao;
import org.dicegame.persistence.PartieSummary;

public class DbInspect {
    public static void main(String[] args) throws Exception {
        DataSource ds = DataSourceProvider.getDataSource();
        PartieDao pd = new PartieDao(ds);
        List<PartieSummary> parties = pd.listAll();
        System.out.println("Parties saved: " + parties.size());
        for (PartieSummary p : parties) {
            System.out.printf("id=%d joueurId=%s nbTours=%d pointsSi7=%d scoreFinal=%d started=%s finished=%s\n",
                    p.getIdPartie(), p.getJoueurId(), p.getNbTours(), p.getPointsSi7(), p.getScoreFinal(), p.getStartedAt(), p.getFinishedAt());
        }
    }
}
