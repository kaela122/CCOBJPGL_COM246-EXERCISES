//Adapter
class VgaToHdmiAdapter implements VGA {
    private HDMI hdmi;
    
    public VgaToHdmiAdapter(HDMI hdmi) {
        this.hdmi = hdmi;
    }

    @Override
    public void connectWithVga() {
        System.out.println("Adapting HDMI to VGA...");
        hdmi.connectWithHdmi();
    }
}