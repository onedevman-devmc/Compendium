package mc.compendium.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface EventHandler {

    EventHandlerPriority priority() default EventHandlerPriority.NORMAL;

    boolean ignoreCancelled() default false;

}
