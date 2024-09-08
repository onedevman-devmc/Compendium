package mc.compendium.chestinterface.components;

import mc.compendium.reflection.FieldUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Trade extends MerchantRecipe {

    private static final Field MERCHANT_RECIPE_RESULT_FIELD = FieldUtil.getInstance().get(MerchantRecipe.class, field -> {
        int modifiers = field.getModifiers();
        return (
            Modifier.isPrivate(modifiers) && !Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)
            && field.getType().equals(ItemStack.class) && field.getName().equals("result")
        );
    });

    //

    private int stocks = 0;
    private int maxStocks = 0;

    private int earnings = 0;
    private int maxEarnings = Integer.MAX_VALUE;

    //

    public Trade(ItemStack result, ItemStack price) {
        super(result, 0);

        //

        this.setPrice(price);
    }

    //

    @Override
    public synchronized int getUses() { return 0; }

    @Override
    public synchronized void setUses(int uses) {}

    //

    public synchronized boolean hasComplementaryPrice() { return this.getComplementaryPrice() != null; }

    //

    @Override
    public synchronized int getMaxUses() {
        return Math.min(this.getStocks(), this.getAvailableEarningsSpace());
    }

    @Override
    public synchronized void setMaxUses(int maxUses) {}

    //

    public synchronized ItemStack getPrice() {
        return this.getIngredients().get(0);
    }

    public synchronized ItemStack getComplementaryPrice() {
        List<ItemStack> ingredients = this.getIngredients();
        return ingredients.size() > 1 ? ingredients.get(1) : null;
    }

    public synchronized int getStocks() { return this.stocks; }

    public synchronized int getMaxStocks() { return this.maxStocks; }

    public synchronized int getEarnings() { return this.earnings; }

    public synchronized int getAvailableEarningsSpace() { return this.getMaxEarnings() - this.getEarnings(); }

    public synchronized int getMaxEarnings() { return this.maxEarnings; }

    //

    public synchronized void setPrice(ItemStack price) {
        List<ItemStack> currentIngredients = this.getIngredients();
        ItemStack complementaryPrice = currentIngredients.size() > 1 ? currentIngredients.get(1) : null;
        this.setIngredients(complementaryPrice == null ? List.of(price) : List.of(price, complementaryPrice));
    }

    public synchronized void setComplementaryPrice(ItemStack complementaryPrice) {
        List<ItemStack> currentIngredients = this.getIngredients();
        ItemStack price = currentIngredients.get(0);
        this.setIngredients(complementaryPrice == null ? List.of(price) : List.of(price, complementaryPrice));
    }

    public synchronized void setResult(ItemStack result) {
        try {
            FieldUtil.getInstance().setValue(this, MERCHANT_RECIPE_RESULT_FIELD, Objects.requireNonNull(result).clone());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    //

    public synchronized void setStocks(int stocks) {
        if(stocks > this.maxStocks) throw new IllegalArgumentException("Stocks can't be greater than maximum stocks.");
        this.stocks = stocks;
    }

    public synchronized void addStocks(int toAdd) { this.setStocks(Math.min(this.getStocks() + toAdd, this.getMaxStocks())); }

    public synchronized void removeStocks(int toRemove) { this.setStocks(this.getStocks() - toRemove); }

    public synchronized void setMaxStocks(int maxStocks) {
        this.maxStocks = maxStocks;
        if(this.maxStocks < this.stocks) this.stocks = this.maxStocks;
    }

    //

    public synchronized void setEarnings(int earnings) {
        if(earnings > this.maxEarnings) throw new IllegalArgumentException("Earnings can't be greater than maximum earnings.");
        this.earnings = earnings;
    }

    public synchronized void addEarnings(int toAdd) { this.setEarnings(Math.min(this.getEarnings() + toAdd, this.getAvailableEarningsSpace())); }

    public synchronized void removeEarnings(int toRemove) { this.setEarnings(Math.max(this.getEarnings() - toRemove, 0)); }

    public synchronized void setMaxEarnings(int maxEarnings) {
        this.maxEarnings = maxEarnings;
        if(this.maxEarnings < this.earnings) this.earnings = this.maxEarnings;
    }

    //

    public synchronized Map<TradeSlotType, ItemStack> trade(ItemStack price, ItemStack complementaryPrice, int maxCount) {
        Map<TradeSlotType, ItemStack> resultMap;

        //

        ItemStack tradePrice = this.getPrice();
        ItemStack tradeComplementaryPrice = this.getComplementaryPrice();

        //

        int requestedStocks = price.getAmount() / tradePrice.getAmount();
        if(tradeComplementaryPrice != null) {
            if(complementaryPrice == null) {
                complementaryPrice = tradeComplementaryPrice.clone();
                complementaryPrice.setAmount(0);
            }

            requestedStocks = Math.min(requestedStocks, complementaryPrice.getAmount() / tradeComplementaryPrice.getAmount());
        }
        else tradeComplementaryPrice = new ItemStack(Material.AIR, 0);

        requestedStocks = Math.min(this.getMaxUses(), requestedStocks);
        requestedStocks = Math.min(maxCount, requestedStocks);

        //

        resultMap = Map.of(
            TradeSlotType.FIRST_INGREDIENT, tradePrice.clone(),
            TradeSlotType.SECOND_INGREDIENT, tradeComplementaryPrice.clone(),
            TradeSlotType.RESULT, this.getResult().clone()
        );

        for(ItemStack itemStack : resultMap.values()) itemStack.setAmount(itemStack.getAmount() * requestedStocks);

        ItemStack priceResult = resultMap.get(TradeSlotType.FIRST_INGREDIENT);
        priceResult.setAmount(price.getAmount() - priceResult.getAmount());

        ItemStack complementaryPriceResult = resultMap.get(TradeSlotType.SECOND_INGREDIENT);
        complementaryPriceResult.setAmount(complementaryPrice == null ? 0 : complementaryPrice.getAmount() - complementaryPriceResult.getAmount());

        //

        this.removeStocks(requestedStocks);
        this.addEarnings(requestedStocks);

        return resultMap;
    }

    //

    public MerchantRecipe asRecipe() {
        MerchantRecipe recipe = null;

        int uses = this.getUses();
        int maxUses = this.getMaxUses();

        try {
            recipe = new MerchantRecipe(
                FieldUtil.getInstance().getValue(this, MERCHANT_RECIPE_RESULT_FIELD),
                uses, maxUses, false
            );
        }
        catch (IllegalAccessException e) { throw new RuntimeException(e); }

        recipe.setIngredients(this.getIngredients());
        return recipe;
    }

}
