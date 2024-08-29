package mc.compendium.chestinterface.components.configurations;

public abstract class BasicMenuConfig extends BasicInterfaceConfig implements MenuConfig {

    protected int rows;

    //

    protected BasicMenuConfig(String name, boolean silent, int rows) {
        super(name, silent);

        //

        this.rows = rows;
    }

    //

    @Override
    public int rows() { return this.rows; }

}
