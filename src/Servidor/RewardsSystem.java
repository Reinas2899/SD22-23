package Servidor;
import Entidades.Localizacao;
import Entidades.Recompensa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.text.DecimalFormat;
public class RewardsSystem
{
    private Map<Localizacao, List<Recompensa>> rewardsMap = new HashMap<>();
    private static List<Localizacao> trotinetes;
    private static Localizacao origem;

    public Map<Localizacao, List<Recompensa>> getRewardsMap() {
        return rewardsMap;
    }

    public void setRewardsMap(Map<Localizacao, List<Recompensa>> rewardsMap) {
        this.rewardsMap = rewardsMap;
    }

    public RewardsSystem(List<Localizacao> trotinetes, Localizacao origem)
    {

        this.trotinetes = trotinetes;
        this.origem = origem;
    }


    public List<Localizacao> findLocalCVariasTrotis()
    {
        List<Localizacao> LocalCVariasTrotis =  new ArrayList<>();

        for (Localizacao local : trotinetes)
        {
            if (local.getNumTrotinetes() >= 2 )
            {
                LocalCVariasTrotis.add(local);
            }
        }

        return LocalCVariasTrotis;
    }

    public List<Localizacao> findLocalSTrotis(Localizacao origem)
    {
        List<Localizacao> LocalSTrotis = new ArrayList<>();

        for (Localizacao local : trotinetes)
        {
            if (local.getNumTrotinetes() == 0)
            {
                LocalSTrotis.add(local);
            }
        }

        return LocalSTrotis;
    }


    public boolean isNearby(Localizacao local1, Localizacao local2)
    {
        double maxDistance = 3; // 300 metros

        double x1 = local1.getX();
        double y1 = local1.getY();
        double x2 = local2.getX();
        double y2 = local2.getY();

        double distance = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));

        return distance <= maxDistance;
    }


    public void addToRewardsMap(Localizacao origem, List<Recompensa> destinos)
    {
        rewardsMap.put(origem, destinos);
    }


    public double calculateReward(Localizacao loc)
    {


        double rewards = 0;

        int x = loc.getX();
        int y = loc.getY();

        double distancia = Math.sqrt(Math.pow(x - origem.getX(), 2) + Math.pow(y - origem.getY(), 2));

        rewards = distancia;



        return (int) (rewards * 10);
    }

    public boolean aux(Localizacao l){
        double dist= 3;
        int count=0;
        int t =0;
        for (Localizacao loc:trotinetes)
        {

            if(Math.sqrt(Math.pow(loc.getX() - l.getX(), 2) + Math.pow(loc.getY() - l.getY(), 2))<=dist)
            {
                t++;
            }

        }

        for (Localizacao loc:findLocalSTrotis(origem))
        {

            if(Math.sqrt(Math.pow(loc.getX() - l.getX(), 2) + Math.pow(loc.getY() - l.getY(), 2))<=dist)
            {
                count++;

            }

        }
        return (count==t) ;

    }

    public void calculateRewards()
    {
        List<Localizacao> locaisCtrotis =findLocalCVariasTrotis();
        List<Recompensa> destinos = new ArrayList<>();
        int i = 0;

        if (locaisCtrotis.contains(origem))
        {
            for (i = 0 ; i<findLocalSTrotis(origem).size();i++) {
                Recompensa res = new Recompensa(0,findLocalSTrotis(origem).get(i));
                destinos.add(res);

            }

            for (Recompensa res:destinos)
            {
                if(aux(res.getL()))
                {
                addToRewardsMap(res.getL(), destinos);
                double rewards = calculateReward(res.getL());
                res.setCreditos(rewards);
                System.out.println("Recompensas para a localização x:" + res.getL().getX()+" y:"+ res.getL().getY() + "  recompensa:" + rewards);
                }

            }
        }
        else
        {
            System.out.println("Não tem rewards.");
        }
    }

}