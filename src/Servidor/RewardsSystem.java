package Servidor;
import Entidades.Localizacao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class RewardsSystem
{
    private Map<Localizacao, List<Localizacao>> rewardsMap = new HashMap<>();
    private static List<Localizacao> trotinetes;


    public RewardsSystem(List<Localizacao> trotinetes)
    {
        this.trotinetes = trotinetes;
    }


    public List<Localizacao> findLocalCVariasTrotis()
    {
        List<Localizacao> LocalCVariasTrotis = new ArrayList<>();

        for (Localizacao local : trotinetes)
        {
            if (local.getNumTrotinetes() >= 2)
            {
                LocalCVariasTrotis.add(local);
            }
        }

        return LocalCVariasTrotis;
    }


    public List<Localizacao> findLocalSTrotis(Localizacao local)
    {
        List<Localizacao> LocalSTrotis = new ArrayList<>();

        for (Localizacao nearbyLocation : trotinetes)
        {
            if (nearbyLocation.getNumTrotinetes() == 0 && isNearby(local, nearbyLocation))
            {
                LocalSTrotis.add(nearbyLocation);
            }
        }

        return LocalSTrotis;
    }


    public boolean isNearby(Localizacao local1, Localizacao local2)
    {
        double maxDistance = 0.5; // 500 metros

        double x1 = local1.getX();
        double x2 = local1.getY();
        double y1 = local2.getX();
        double y2 = local2.getY();

        double distance = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));

        return distance <= maxDistance;
    }


    public void addToRewardsMap(Localizacao origem, List<Localizacao> destinos)
    {
        rewardsMap.put(origem, destinos);
    }


    public int calculateReward(List<Localizacao> destinos)
    {
        int rewards = 0;

        for (Localizacao destino : destinos) {
            List<Localizacao> LocaisSTrotis = findLocalSTrotis(destino);

            if (LocaisSTrotis.size() > 0) {
                rewards += 10;
            }
        }

        return rewards;
    }


    public void calculateRewards()
    {
        List<Localizacao> LocalCVariasTrotis = findLocalCVariasTrotis();

        for (Localizacao origem : LocalCVariasTrotis)
        {
            List<Localizacao> destinos = findLocalSTrotis(origem);
            addToRewardsMap(origem, destinos);

            int rewards = calculateReward(destinos);
            System.out.println("Recompensas para a localização x:" + origem.getX()+" y:"+origem.getY() + "  recompensa:" + rewards);
        }
    }

}