package tkachgeek.refreshmenu.configurationUI;

import org.bukkit.Material;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UIConfigurable {
Material material() default Material.PAPER;
String name() default "";
String description() default "";

}
