package mc.compendium.chestinterface.components.configurations;

public class BasicResizeableMenuConfig extends BasicMenuConfig implements ResizeableMenuConfig {

    protected BasicResizeableMenuConfig(String name, boolean silent, int rows) {
        super(name, silent, rows);
    }

    //

    @Override
    public void setRows(int rows) { this.rows = rows; }
}
