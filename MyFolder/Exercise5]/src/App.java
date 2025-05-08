public class App {
    public static void main(String[] args) throws Exception {
        HDMI port = new HDMI();

        VGA adapter = new VgaToHdmiAdapter(port);

        Monitor monitor = new Monitor();
        monitor.connect(adapter);
    }
}