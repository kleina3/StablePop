//keeps track of contents of a closed environment
public class Locale{
    private List<Predator> predList;
    private int basePrey;
    
    public Locale(int predPop, int preyPop){
        this.basePrey = preyPop;
        this.predList = new ArrayList<Predator>(predPop);
        
    }
}