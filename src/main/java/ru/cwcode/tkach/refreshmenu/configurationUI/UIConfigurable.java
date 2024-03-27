package ru.cwcode.tkach.refreshmenu.configurationUI;

import org.bukkit.Material;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UIConfigurable {
Material material() default Material.PAPER;
String name() default "";
String description() default "";

}
