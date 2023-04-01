

public class Mainzinha {

    public static void main(String[] args) throws Exception {

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


    }
}