# RefreshMenu 1.1.0
#### Библиотека на меню для Paper
##### Инструкция для настройки конфигов

Важно: Все текстовые элементы меню форматируются при помощи MiniMessage, но перед этим резолвятся все плейсхолдеры из PlaceholderAPI, в связи с чем плейсхолдер не должен возвращать текст с форматированием в других форматах.

Обычно в конфиг всегда выносится InventoryShape (шейп), его и будем настраивать. Вот типичный шейп:

```yaml
some_view:
  type: !<chest> #тип менюшки - далее подробней
    size: 36
  name: "Название меню"
  shape: # сам шейп - представляйте это инвентарём, где символы - предметы
  - "_________"
  - "_#######_"
  - "_#######_"
  - "_________"
  ingredientMap: # а тут собственно привязываем символы к предметам (ингредиентам) - далее подробней
    _: !<simple> 
      name: ""
      description: null
      amount: 1
      type: "GRAY_STAINED_GLASS_PANE"
      customModelData: 0
```

**Типы меню**

- Сундук:
    ```yaml
    type: !<chest> # указываем, что тип это сундук
      size: 36 # и указываем сколько в нём слотов (9, 18, 27, 36, 45, 54)
    ```
- Другой
    ```yaml
    type: !<type> # указываем, что это не сундук, а какой-то тип инвентаря
      type: DISPENSER # и указываем тип инвентаря. В данном случае раздатчик
    ```
  Все типы инвентарей можно узнать [тут](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/event/inventory/InventoryType.html)

**Карта ингредиентов**

```yaml
ingredientMap: 
    '_': !<simple> #тут указывается символ, к которому привязывается ингредиент, и его тип  
      name: "<red>Название предмета" # Название предмета, может быть null, если не нужно изменять
      description: # описание, если null (description: null), тогда последующие строчки не указываются 
      - "Строка 1"
      - "Строка 2"
      amount: 1 # кол-во предмета
      type: "GRAY_STAINED_GLASS_PANE" # тип предмета
      customModelData: 0
    'A': !<action> # тип ингредиента - обычный предмет с действием
      name: "<red>Название предмета"
      description: null
      amount: 1
      type: "GRAY_STAINED_GLASS_PANE"
      customModelData: 0
      actions: # указываем при каких нажатиях какие будут выполняться действия
        LEFT: !<consoleCommand> # выполнение команды от консоли
          command: "say <player> молодец"
        DROP: !<sendMessage> # отправка сообщения нажавшему игроку
          message: "Ты молодец"
        RIGHT: !<playerCommand> # выполнение команды от имени игрока
          command: "say Я молодец"
    'X': !<head> # тип ингредиента - голова игрока
      name: "<red>Название предмета" 
      description: null
      amount: 1
      texture: "base64 текстура скина"
    'C': !<item> # тип ингредиента - закодированный предмет в SNBT или Base64
      item: "snbt или base64"

```
Все типы предметов можно узнать [тут](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html)\
Все типы кликов можно узнать [тут](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/event/inventory/ClickType.html)