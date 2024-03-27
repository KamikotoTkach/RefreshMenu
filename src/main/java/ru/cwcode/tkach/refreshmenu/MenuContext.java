package ru.cwcode.tkach.refreshmenu;

import org.bukkit.entity.Player;
import ru.cwcode.tkach.refreshmenu.inventory.view.View;

public record MenuContext(View view, Player player) {

}
