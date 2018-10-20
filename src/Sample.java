/**
 * Created with IntelliJ IDEA.
 * User: lutfi
 * Date: 6/8/13
 * Time: 8:36 AM
 * To change this template use File | Settings | File Templates.
 */
import com.upi.fuzzy.*;

public class Sample {
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
                        "if (Water is Cold) or (Water is Tepid) then Power is High"));
        fuzzyEngine.getFuzzyRuleCollection().add(
                new FuzzyRule("if (Water is Hot) then Power is Low"));

        water.setInputValue(60);

        try {
            System.out.println(fuzzyEngine.Defuzzify());
        } catch (Exception e) {
            System.out.println("error "+e.getMessage());
        }
    }
}
