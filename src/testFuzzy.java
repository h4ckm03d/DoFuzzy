/**
 * Created with IntelliJ IDEA.
 * User: lutfi
 * Date: 6/8/13
 * Time: 8:36 AM
 * To change this template use File | Settings | File Templates.
 */
import com.upi.fuzzy.*;

public class testFuzzy {
    public static void main(String[] args) {
        LinguisticVariable water = new LinguisticVariable("Water");
        water.getMembershipFunctionCollection().add(
                new MembershipFunction("Cold", 0, 0, 20, 40));
        water.getMembershipFunctionCollection().add(
                new MembershipFunction("Tepid", 30, 50, 50, 70));
        water.getMembershipFunctionCollection().add(
                new MembershipFunction("Hot", 50, 80, 100, 100));

        LinguisticVariable power = new LinguisticVariable("Power");
        power.getMembershipFunctionCollection().add(
                new MembershipFunction("Low", 0, 25, 25, 50));
        power.getMembershipFunctionCollection().add(
                new MembershipFunction("High", 25, 50, 50, 75));

        FuzzyEngine fuzzyEngine = new FuzzyEngine();
        fuzzyEngine.getLinguisticVariableCollection().add(water);
        fuzzyEngine.getLinguisticVariableCollection().add(power);
        fuzzyEngine.setConsequent("Power");
        fuzzyEngine
                .getFuzzyRuleCollection()
                .add(new FuzzyRule(
                        "IF (Water IS Cold) OR (Water IS Tepid) THEN Power IS High"));
        fuzzyEngine.getFuzzyRuleCollection().add(
                new FuzzyRule("IF (Water IS Hot) THEN Power IS Low"));

        water.setInputValue(60);

        try {
            System.out.println(fuzzyEngine.Defuzzify());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }

    }
}
