import com.upi.fuzzy.*;

import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: lutfi
 * Date: 6/9/13
 * Time: 1:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    private String command="";
    private boolean isExit=false;
    private FuzzyEngine fuzzyEngine;

    public Main(){
        fuzzyEngine = new FuzzyEngine();
        command = "addVar addLing addRule lsVar lsLing lsRule details setOutput defuzz exit sample";
    }

    public void run(){
        System.out.println(
                "Judul Applikasi\n"+
                        "keterangan tambahan\n"
        );
        Scanner sc = new Scanner(System.in); // buat menerima input
        String input="";  // untuk menampung inputan sementara
        String preText = "Logfuzz> ";
        while (!isExit){
            System.out.print(preText);
            input = sc.nextLine();
            parse(input);
        }
        System.out.println("bye");
        sc.close();
    }

    public void showHelp(){

    }

    /***
     * parse = menerjemahkan perintah dari input ke program
     * @param input
     */
    public void parse(String input){
        if(input.length()==0 || input == null){
            System.out.println("Invalid input");
            return;
        }
        String [] token = input.toLowerCase().split(" "); // input diubah menjadi huruf kecil semua, pemisah berdasarkan spasi
        String cmd = token[0];
        if(!validateCommand(cmd)){
            System.out.println("--Command not found--\n");
            return;
        }

        if(cmd.equalsIgnoreCase("addVar")){
            this.addVar(token);
        }else if(cmd.equalsIgnoreCase("addLing")){
            this.addLing(token);
        }else if(cmd.equalsIgnoreCase("addRule")){
            this.addRule(token);
        }else if(cmd.equalsIgnoreCase("lsVar")){
            this.lsVar();
        }else if(cmd.equalsIgnoreCase("lsLing")){
            this.lsLing(token);
        }else if(cmd.equalsIgnoreCase("lsRule")){
            this.lsRule();
        }else if(cmd.equalsIgnoreCase("details")){
            this.details();
        }else if(cmd.equalsIgnoreCase("setOutput")){
            this.setOutput(token);
        }else if(cmd.equalsIgnoreCase("defuzz")){
            this.defuzz(token);
        }else if(cmd.equalsIgnoreCase("sample")){
            this.sample();
        }else if(cmd.equalsIgnoreCase("exit")){
            isExit = true;
        }
    }

    /***
     * untuk mengecek ada atau tidaknya command
     * @param cmd
     * @return
     */
    public boolean validateCommand(String cmd){
        String [] cmdArr = this.command.split(" ");
        for(String c : cmdArr){
            if(cmd.equalsIgnoreCase(c))
                return true;
        }

        return false;
    }

    public void addVar(String [] params){
        if(params.length < 2){
            System.out.println("--Invalid params--\n");
            return;
        }

        for (int i=1;i<params.length;i++){
            fuzzyEngine.getLinguisticVariableCollection().add(new LinguisticVariable((params[i])));
        }
        System.out.println("Variable Added");
    }

    public void addLing(String [] params){
        if(params.length!=7){
            System.out.println("--Invalid params--\n");
            return;
        }
        try{
            fuzzyEngine.getLinguisticVariableCollection().Find(params[1]).getMembershipFunctionCollection().add(new MembershipFunction(params[2], Double.parseDouble(params[3]), Double.parseDouble(params[4]), Double.parseDouble(params[5]), Double.parseDouble(params[6])));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println("Membership function "+params[2]+" added"); // yang bertambah adalah param 2, membership function
    }

    public void addRule(String [] params){
        if(params.length<2){
            System.out.println("--Invalid params--\n");
            return;
        }

        StringBuffer rule = new StringBuffer();
        for (int i=1;i<params.length;i++){
            rule.append(params[i]+" ");        // concatenate token
        }

        fuzzyEngine.getFuzzyRuleCollection().add(new FuzzyRule(rule.substring(0,rule.length()-2)));
        System.out.println("Rule Added");
    }

    public boolean linguisticVariableCollectionCheck(){
        if(fuzzyEngine.getLinguisticVariableCollection().size()==0){
            System.out.println("No variable added");
            return false;
        }
        return true;
    }

    public boolean rulesCheck(){
        if(fuzzyEngine.getFuzzyRuleCollection().size()==0){
            System.out.println("No rules added");
            return false;
        }
        return true;
    }

    public boolean membershipCollectionCheck(String varName){
        try{
            if(fuzzyEngine.getLinguisticVariableCollection().Find(varName).getMembershipFunctionCollection().size()==0){
                System.out.println("No membership function added");
                return false;
            }
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }


    public void lsVar(){
        if(!linguisticVariableCollectionCheck()){
            return;
        }
        System.out.println("----------------\nList of variable\n----------------");
        for(LinguisticVariable var: fuzzyEngine.getLinguisticVariableCollection()){
            System.out.printf("- %1$4s\n", var.getName());
        }
        System.out.println();
    }

    public void lsLing(String [] params){
        if(params.length!=2){
            System.out.println("Invalid params\nExample usage: lsLing variableName");
            return;
        }
        if(!linguisticVariableCollectionCheck()){
            return;
        }

        if(!membershipCollectionCheck(params[1])){
            return;
        }
        try{

            if(fuzzyEngine.getLinguisticVariableCollection().Find(params[1]).getMembershipFunctionCollection().size()==0){
                System.out.println("No membership function added");
            }

            System.out.println("--------------------------------\n"+
                    "List of linguistic variable "+params[1]+
                    "\n--------------------------------");
            MembershipFunctionCollection membershipFunctionCollection = this.fuzzyEngine.getLinguisticVariableCollection().Find(params[1]).getMembershipFunctionCollection();
            for(MembershipFunction mf: membershipFunctionCollection){
                System.out.printf("%1$-5s %2$5s %3$5s %4$5s %5$5s\n",mf.getName(), mf.getX0(),mf.getX1(), mf.getX2(), mf.getX3());
            }
            System.out.println();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void defuzz(String [] params){
        if(params.length!=fuzzyEngine.getLinguisticVariableCollection().size()){
            System.out.println("--Invalid params--\n");
            return;
        }
        if(!linguisticVariableCollectionCheck()){
            return;
        }
        if(!rulesCheck()){
            return;
        }
        if(this.fuzzyEngine.getConsequent()==null || this.fuzzyEngine.getConsequent().length()==0)
        {
            System.out.println("Output var not set");
            return;
        }
        try{
            for (int i=1;i<params.length;i++){
                String [] token = params[i].split("=");
                if(token.length!=2){
                    System.out.println("--Invalid params--\n");
                    return;
                }
                fuzzyEngine.getLinguisticVariableCollection().Find(token[0]).setInputValue(Double.parseDouble(token[1]));
            }
            System.out.println(fuzzyEngine.Defuzzify());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void lsRule(){
        if(!rulesCheck()){
            return;
        }
        int no=1;
        System.out.println("----------------\nList of rules\n----------------");
        for(FuzzyRule fuzzyRule: fuzzyEngine.getFuzzyRuleCollection()){
            System.out.printf("%1$4s. %2$s\n",no, fuzzyRule.getText());
            no++;
        }
        System.out.println();
    }

    public void details(){
        try{
            for(LinguisticVariable lv : fuzzyEngine.getLinguisticVariableCollection()){
                System.out.println("- "+lv.getName());
                MembershipFunctionCollection membershipFunctionCollection = this.fuzzyEngine.getLinguisticVariableCollection().Find(lv.getName()).getMembershipFunctionCollection();
                for(MembershipFunction mf: membershipFunctionCollection){
                    System.out.printf("%1$-5s %2$5s %3$5s %4$5s %5$5s\n",mf.getName(), mf.getX0(),mf.getX1(), mf.getX2(), mf.getX3());
                }
                System.out.println();
            }
            System.out.println("OUTPUT: "+this.fuzzyEngine.getConsequent());
            this.lsRule();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void sample1(){
        LinguisticVariable water = new LinguisticVariable("water");
        water.getMembershipFunctionCollection().add(
                new MembershipFunction("cold", 0, 0, 20, 40));
        water.getMembershipFunctionCollection().add(
                new MembershipFunction("tepid", 30, 50, 50, 70));
        water.getMembershipFunctionCollection().add(
                new MembershipFunction("hot", 50, 80, 100, 100));

        LinguisticVariable power = new LinguisticVariable("power");
        power.getMembershipFunctionCollection().add(
                new MembershipFunction("low", 0, 25, 25, 50));
        power.getMembershipFunctionCollection().add(
                new MembershipFunction("high", 25, 50, 50, 75));

        fuzzyEngine.getLinguisticVariableCollection().add(water);
        fuzzyEngine.getLinguisticVariableCollection().add(power);
        fuzzyEngine.setConsequent("power");
        fuzzyEngine
                .getFuzzyRuleCollection()
                .add(new FuzzyRule(
                        "if (water is cold) or (water is tepid) then power is high"));
        fuzzyEngine.getFuzzyRuleCollection().add(
                new FuzzyRule("if (water is hot) then power is low"));

        water.setInputValue(60);
    }
    public void sample(){
        LinguisticVariable tinggibadan = new LinguisticVariable("tinggibadan");
        tinggibadan.getMembershipFunctionCollection().add(
                new MembershipFunction("rendah", 145, 150, 160, 165));
        tinggibadan.getMembershipFunctionCollection().add(
                new MembershipFunction("normal", 155, 160, 170, 175));
        tinggibadan.getMembershipFunctionCollection().add(
                new MembershipFunction("tinggi", 165, 170, 185, 190));

        LinguisticVariable beratbadan = new LinguisticVariable("beratbadan");
        beratbadan.getMembershipFunctionCollection().add(
                new MembershipFunction("ringan", 35, 45, 50, 55));
        beratbadan.getMembershipFunctionCollection().add(
                new MembershipFunction("normal", 45, 50, 60, 65));
        beratbadan.getMembershipFunctionCollection().add(
                new MembershipFunction("berat", 60, 65, 75, 80));

        LinguisticVariable statusgizi = new LinguisticVariable("statusgizi");
        statusgizi.getMembershipFunctionCollection().add(
                new MembershipFunction("ktb", 13, 14, 16, 17));
        statusgizi.getMembershipFunctionCollection().add(
                new MembershipFunction("ktr", 16, 17, 17.5, 18.5));
        statusgizi.getMembershipFunctionCollection().add(
                new MembershipFunction("normal", 17.5, 19, 24, 25));
        statusgizi.getMembershipFunctionCollection().add(
                new MembershipFunction("gtr", 24, 25, 26, 27));
        statusgizi.getMembershipFunctionCollection().add(
                new MembershipFunction("gtb", 26, 28, 30, 33));

        fuzzyEngine.getLinguisticVariableCollection().add(tinggibadan);
        fuzzyEngine.getLinguisticVariableCollection().add(beratbadan);
        fuzzyEngine.getLinguisticVariableCollection().add(statusgizi);
        fuzzyEngine.setConsequent("statusgizi");
        fuzzyEngine
                .getFuzzyRuleCollection()
                .add(new FuzzyRule
                        ("if (tinggibadan is rendah) and (beratbadan is ringan) then statusgizi is normal"));
        fuzzyEngine
                .getFuzzyRuleCollection()
                .add(new FuzzyRule
                        ("if (tinggibadan is rendah) and (beratbadan is normal) then statusgizi is gtr"));

        fuzzyEngine
                .getFuzzyRuleCollection()
                .add(new FuzzyRule
                        ("if (tinggibadan is rendah) and (beratbadan is berat) then statusgizi is gtb"));
        fuzzyEngine
                .getFuzzyRuleCollection()
                .add(new FuzzyRule
                        ("if (tinggibadan is normal) and (beratbadan is ringan) then statusgizi is ktr"));
        fuzzyEngine
                .getFuzzyRuleCollection()
                .add(new FuzzyRule
                        ("if (tinggibadan is normal) and (beratbadan is normal) then statusgizi is normal"));
        fuzzyEngine
                .getFuzzyRuleCollection()
                .add(new FuzzyRule
                        ("if (tinggibadan is normal) and (beratbadan is berat) then statusgizi is gtr"));
        fuzzyEngine
                .getFuzzyRuleCollection()
                .add(new FuzzyRule
                        ("if (tinggibadan is tinggi) and (beratbadan is ringan) then statusgizi is ktb"));
        fuzzyEngine
                .getFuzzyRuleCollection()
                .add(new FuzzyRule
                        ("if (tinggibadan is tinggi) and (beratbadan is normal) then statusgizi is ktr"));
        fuzzyEngine
                .getFuzzyRuleCollection()
                .add(new FuzzyRule
                        ("if (tinggibadan is tinggi) and (beratbadan is berat) then statusgizi is normal"));


        tinggibadan.setInputValue(160);
        beratbadan.setInputValue(42);
    }

    public void setOutput(String [] params){
        if(params.length!=2){
            System.out.println("Invalid Params");
            return;
        }

        if(!linguisticVariableCollectionCheck())
            return;

        fuzzyEngine.setConsequent(params[1]);
        System.out.println("Output variable set to "+params[1]);
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }
}
