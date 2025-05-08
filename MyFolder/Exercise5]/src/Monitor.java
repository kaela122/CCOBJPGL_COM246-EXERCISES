//Client
public class Monitor {
    public void connect(VGA port) {
        System.out.println("Monitor expecting VGA port...");
        port.connectWithVga();
        System.out.println("Monitor connected.");
    }
}