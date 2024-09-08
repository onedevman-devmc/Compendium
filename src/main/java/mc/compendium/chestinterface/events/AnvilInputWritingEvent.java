package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.AnvilInput;
import mc.compendium.protocol.events.IncomingPacketEvent;
import net.minecraft.network.protocol.game.PacketPlayInItemName;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class AnvilInputWritingEvent extends AnvilInputEvent<IncomingPacketEvent<PacketPlayInItemName>> {

    private final String partialText;
    private String resultText;

    //

    public AnvilInputWritingEvent(IncomingPacketEvent<PacketPlayInItemName> originalEvent, Player entity, Inventory inventory, AnvilInput anvilInput, String partialText) {
        this(originalEvent, entity, inventory, anvilInput, partialText, true);
    }

    public AnvilInputWritingEvent(IncomingPacketEvent<PacketPlayInItemName> originalEvent, Player entity, Inventory inventory, AnvilInput anvilInput, String partialText, boolean cancellable) {
        super(originalEvent, entity, inventory, anvilInput, cancellable);

        //

        this.partialText = partialText;
        this.resultText = this.partialText;
    }

    //

    public String getPartialText() { return this.partialText; }

    public String getResultText() { return this.resultText; }

    public void setResultText(String resultText) { this.resultText = resultText; }

}
