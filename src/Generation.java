//For each pred:
//          Use pred's kill rate to determine how much it eats.
//          Record this value with the pred.
//          Decrement the prey population by this amount.
//        Group the preds into random breeding pairs.
//        Find the number of children for each breeding pair using the fitness/reproduction function from Ito et al.
//        Use crossover to create children for each breeding pair, then apply mutation to the offspring.
//        Shuffle the list of offspring.
//        Multiply the prey population by its growth constant.

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generation{
    private Random random;
    private Locale locale;
    private double preyGrowth;
    private double predGrowth;

    public Generation(double preyGrowthRate, double predGrowthRate) {
        this.random = new Random();
        this.preyGrowth = preyGrowthRate;
        this.predGrowth = predGrowthRate;
    }
    
    public void runGeneration(Locale newLoc) {
        this.locale = newLoc;
        for (Predator pred : locale.getPredList()){
            pred.setKills(hunt(pred));
        }
        locale.setBasePrey(((int)Math.ceil(locale.getBasePrey() * preyGrowth)));
        locale.shufflePredList(random);
        locale.killPreds();
        //TODO: kill predators
        locale.setPredList(makeKids(locale.getPredList()));
        locale.shufflePredList(random);
    }

    //this code shuffles: Collections.shuffle(*insert list here*, random);

    //figures out number of kills
    //returns the number of kills
    //side-effect: reduces the base prey pop by number of kills
    private int hunt(Predator pred) {
        int killCount = 0;
        for (int i = 0; i < locale.getBasePrey(); i++){
            if (random.nextFloat() <= pred.getKillRate()){
                killCount++;
            }
        }
        locale.reduceBasePrey(killCount);
        return killCount;
    }

    /**
     * Uses fitness method modified from Ito et al. to determine how many kids to produce for two predators, then
     * produces said kids using crossover and mutation.
     * @return
     */
    private List<Predator> makeKids(List<Predator> predators) {
        List<Predator> kids = new ArrayList<Predator>();
        Predator pred1;
        Predator pred2;
        int i;
        for (i = 0; i < predators.size() - 1; i += 2) {
            pred1 = predators.get(i);
            pred2 = predators.get(i+1);
            int pairFitness = pred1.getKills() + pred2.getKills();
            pairFitness = (int)Math.floor(pairFitness * predGrowth);
//            //cap max number of kids
//            if (pairFitness > 4){
//                pairFitness = 4;
//            }
            Random rand = new Random();
            for (int j = 0; j < pairFitness; j++) {
                float crossingPoint = rand.nextFloat();
                double kidKillRate = crossingPoint * pred1.getKillRate() + (1 - crossingPoint) * pred2.getKillRate();
                Predator kid = new Predator(kidKillRate);
                kids.add(kid);
                //TODO: implement mutation
            }
        }
        if (i < predators.size()){
            Predator oddPred = predators.get(i);
            for (int j = 0; j < oddPred.getKills(); j++){
                Predator kid = new Predator(oddPred.getKillRate());
                kids.add(kid);
            }
        }
        kids.addAll(predators);
        return kids;
    }
}