package mc.compendium.types;

public interface Formatter<From, To> {

    To format(From input);

}