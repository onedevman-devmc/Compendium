package mc.compendium.chestinterface.components.configurations;

public abstract class BasicInterfaceConfig implements InterfaceConfig {

    private String name;
    private boolean silent;

    //

    protected BasicInterfaceConfig(String name, boolean silent) {
        this.name = name;
        this.silent = silent;
    }

    //

    @Override
    public String name() { return this.name; }

    @Override
    public boolean silent() { return this.silent; }

    //

    @Override
    public void setName(String name) { this.name = name; }

    @Override
    public void setSilent(boolean silent) { this.silent = silent; }

}
