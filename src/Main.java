
import java.util.Scanner;

public class Main {

    public static int n,m, available[],saveOldAvailable[],allocation[][],need[][],maximum[][],release[],request[],test=0;
    public static int firstAvailable[],remain=0;
    public static boolean safeState,recovryState=false;


    //*****need calculation ****

    public static int[][] calculateNeed(){
        need=new int [maximum.length][maximum[0].length];
        for(int i=0;i<maximum.length;i++)
            for(int j=0;j<maximum[0].length;j++)
                need[i][j]=maximum[i][j]-allocation[i][j];

        /**  for(int i=0;i<maximum.length;i++)
         for(int j=0;j<maximum[0].length;j++)
         System.out.println(need[i][j]);**/

        return need;
    }

    //*****updating Available in each step ****
    public static int[] updateAvailable(int p)
    {
        for(int j=0;j<allocation[0].length;j++) {
            available[j] = available[j] + allocation[p][j];

        }
        /** for(int i=0;i<allocation[0].length;i++)
         System.out.println(available[i]);**/
        return available;
    }
    public static void resetAvailable()
    {
        for(int j=0;j<m;j++) {
            available[j] = firstAvailable[j];

        }
        /** for(int i=0;i<allocation[0].length;i++)
         System.out.println(available[i]);**/

    }
    //*****verify if satisfying the banker conditon ****
    public static boolean verify(int i){
        for(int j=0;j<m;j++)
            if(available[j]<need[i][j])
                return false;
        return true;
    }

    //**************Banker Algorithm ****
    public static void  bankerAlgorithm()
    {

        int All=0;

        boolean finished[]=new boolean[n];
        while(All<n){
            boolean flag=false;



            for(int i=0;i<n;i++){

                if(!finished[i] && verify(i) && allocation[i][0]!=-1){
                    System.out.println("current Available resources");
                    for(int a=0;a<allocation[0].length;a++)
                        System.out.println(available[a]);
                    System.out.println("process : "+i+" is allocated ");
                    flag=finished[i]=true;
                    All++;
                    updateAvailable(i);
                }
            }
            if(!flag)
            {
                break;}
        }
        if(All==n || (recovryState&&All==remain)) {
            System.out.println("\nAll Processes are Safely allocated!! (APPROVED) ");
            System.out.println("\nfinal instances:");
            for(int i=0;i<allocation[0].length;i++)
                System.out.println(available[i]);
            safeState=true;
            test=1;
        }
        else{
            System.out.println("cant be allocated safely !!(DENIED)");
            safeState=false;}
    }
    //**************recovering part ****
    public static int sumNeed(int p)
    {
        int sum=0;
        for (int i =0;i<m;i++)
            sum=sum+need[p][i];
        return sum;
    }
    public static int victim ()
    {
        int location;
        int sumNeed=0;
        int maxi=0;
        location = 0;
        for (int i = 0; i < n; i++) {
            if (sumNeed(i)>maxi&&allocation[i][0]!=-1) {
                maxi = sumNeed(i);
                location = i;
            }
            else if ( allocation[i][0]==-1)
            {
                maxi++;
            }

        }
        for(int j=0;j<m;j++)
            available[j] = available[j] + allocation[location][j];
        for(int j=0;j<m;j++)
            allocation[location][j]=-1;

        return location;
    }
    public static void  recoveryAlgorithm() {
        Scanner input = new Scanner(System.in);
        recovryState=true;


        if (safeState) System.out.println("YOU ARE IN SAFE STATE!");
        else {
            while (!safeState) {
                System.out.println("Trying!");
                victim();
                for(int a=0;a<allocation[0].length;a++){
                    saveOldAvailable[a]=available[a];

                }

                remain--;
                bankerAlgorithm();
                if(test==0){

                    for(int a=0;a<allocation[0].length;a++){
                        available[a]=saveOldAvailable[a];

                    }test=0;
                }
            }
        }
    }

    //**************requesting part ****
    public static void  request(int pNum,int[] request){

        for (int i = 0; i < m; i++) {
            need[pNum][i] += request[i];
        }

        bankerAlgorithm();
        resetAvailable();
        if (safeState==false)
        {
            System.out.println("We aren't in safe state!!");
        }
        else
        {System.out.println("We aren in safe state!!");}
    }
    //**************releasing part ****
    public static void release(int pNum,int[] release)
    {
        if (verify2(pNum)){
            for (int i = 0; i < m; i++) {
                allocation[pNum][i] = allocation[pNum][i]-release[i];
                available[i] += release[i];
                need[pNum][i] = maximum[pNum][i] + allocation[pNum][i];
            }
            System.out.println("release done successfully !");
        }
        else System.out.println("Enter valid! !");
    }
    public static boolean verify2(int i){
        for(int j=0;j<m;j++)
            if(release[j]>allocation[i][j])
                return false;
        return true;
    }


    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        System.out.println("Enter number of Processes and rescources ");
        String data = input.nextLine();
        String[] splitStr = data.split("\\s+");
        n = Integer.parseInt(splitStr[0]);
        remain=n;
        m = Integer.parseInt(splitStr[1]);
        System.out.println("Enter Allocation Table for each Resource");
        allocation = new int[n][m];
        for (int i = 0; i < n; i++) {
            String data1 = input.nextLine();
            String[] splitStr1 = data1.split("\\s+");
            for (int j = 0; j < m; j++) {
                allocation[i][j] = Integer.parseInt(splitStr1[j]);
            }
        }
        System.out.println("Enter MAX Table for each Process");
        maximum = new int[n][m];
        for (int i = 0; i < n; i++) {
            String maxi = input.nextLine();
            String[] splitStr2 = maxi.split("\\s+");
            for (int j = 0; j < m; j++) {
                maximum[i][j] = Integer.parseInt(splitStr2[j]);
            }
        }
        System.out.println("Enter Available amount of each Process");
        available=new int[m];
        saveOldAvailable=new int[m];
        firstAvailable=new int[m];

        String Available = input.nextLine();
        String[] splitStr3= Available.split("\\s+");
        for (int i = 0; i < m; i++) {
            available[i]=Integer.parseInt(splitStr3[i]);
        }
        for (int i = 0; i < m; i++) {
            firstAvailable[i]=available[i];
        }
        calculateNeed();
        bankerAlgorithm();
        resetAvailable();
//**************Options Part ****
        while (true) {
            String options = input.nextLine();
            String[] all = options.split("\\s+");
            String option = all[0];
            if (option.equals("Quit")){
                System.exit(0);
                break;}
            else if (option.equals("Recover")) {
                recoveryAlgorithm();


            } else if (option.equals("RQ")) {
                request = new int[m];
                int pNum = Integer.parseInt(all[1]);
                for (int i = 0, j = 2; i < m; i++, j++) {

                    request[i] = Integer.parseInt(all[j]);

                }
                request(pNum, request);
            } else if (option.equals("RL")) //RL 0 0 1 0
            {

                release = new int[m];
                int pNum = Integer.parseInt(all[1]);
                for (int i = 0, j = 2; i < m; i++, j++) {

                    release[i] = Integer.parseInt(all[j]);
                }
                release(pNum, release);
                if (verify2(pNum)){
                    for (int a = 0; a < m; a++)
                        System.out.println(allocation[pNum][a]);}
            }
        }




    }

}