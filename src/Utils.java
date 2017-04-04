public class Utils{

    public static void PrintUILine(){
        System.out.println("--------------------------------------------");
    }

    public static void ClearTerminal(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void UnableToConnect(Exception e){
        System.out.println("Unable to connect!");
        System.out.println(e.getMessage());
        PrintUILine();
    }
}