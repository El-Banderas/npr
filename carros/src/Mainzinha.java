

public class Mainzinha {

    public static void main(String[] args) throws Exception {
        for (int i = 4; i > 1; i--){
            System.out.println("Antes");
            for (int ii = 0; ii < i; i--)
                System.out.println("I: " + i + " ii: " + ii) ;
            System.out.println("Depois");
        }

/*

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
			public void run() {
                System.out.println("Shutdown Hook is running !");
            }
        });
        System.out.println("Application Terminating ...");

        while (true) {
            System.out.println("Olá");
            Thread.sleep(1000);
        }
*/

    }
}