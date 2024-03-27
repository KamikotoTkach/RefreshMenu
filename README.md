# RefreshMenu 1.0
#### Библиотека на меню для Paper

Настройка конфигов [тут](docs/readme.md)

**Кратко:** <br>

**Menu** <br>
Используется как контейнер для ```View```

**View** <br>
Связывает отображение ```InventoryShape``` и поведение

**InventoryShape** <br>
Хранит схему отображения ```Ingredient```-ов

**Ingredient** <br>
Интерфейс, реализующий метод ```getItem```, возвращающий ```ItemStack``` (представление ингридиента в меню)

**Применение** <br>
1. Создаём своё ```Menu```:
```java
public class TestMenu extends Menu {
  public TestMenu() {
    setView("default", new TestView()); //Устанавливаем в качестве дефолтного View - наш TestView
  }
}
```
2. Создаём свой ```View```:
```java
public class TestView extends View {
  public TestView() {
    new ShapeBuilder()
       .name("<gold>MiniMessage title") //название меню
       .chest(27) //тип меню - сундук и размер 27 (можно указать другой тип меню через .type(InventoryType.HOPPER))
       .shape( //схема отображения меню
          "---------",
          "-A-----B-",
          "---------"
       )
       .ingredient('A', Ingredient.builder()
                                  .name("<gold>Some text: <placeholder>") 
                                  .type(Material.SPECTRAL_ARROW)
                                  .customModelData(1)
                                  .description("line 1",
                                               "line 2")
                                  .build()) //Так к элементу схемы A привязывается Ingredient
       
       .ingredient('B', Ingredient.of(new ItemStack(Material.GOLD_BLOCK))) //Ingredient может быть задан готовым ItemStack
       .build(this); //Устанавливаем InventoryShape для нашего View
    
    getPlaceholders().add("placeholder", "<gray>foo"); //добавляем плейсхолдер, который будет резолвится во всех ингридиентах и названии меню (Кроме ингридиентов, созданных из ItemStack напрямую)
    
    getBehavior().bind('A', ClickType.LEFT, (e) -> { //Указываем элементу схемы A действие по нажатию ЛКМ
      e.getWhoClicked().closeInventory();
      //or getMenu().openView((Player)e.getWhoClicked(), "anotherView");
    });
  }
}
```
3. Done

![image](https://github.com/KamikotoTkach/RefreshMenu/assets/110531613/b871eb02-3a04-4fe1-a4a0-b7399e4d4811)


**PagedView** <br>
Это реализация ```View```, созданная для отображения динамических данных (Например: предметы в магазине, онлайн игроки) <br>

**Применение**: <br>
1. Аналогично с обычным ```View``` указываем его как ```default``` в какой-либо ```Menu```
2. Создаём экземпляр:
```java
public class TestPagedView extends PagedView<Ingredient /*или любая своя реализация*/> {
  {
    InventoryShape.builder()
                  .name("Paged menu")
                  .chest(27)
                  .shape("#########", //# по дефолту является "динамическим" символом, в ингридиентах он не определяется
                         "#########",
                         "0000000<>")
    
                  .ingredient('<', Ingredient.builder() //дефолтный символ для действия "на прошлую страницу"
                                             .type(Material.ARROW)
                                             .name("На <prevPage> страницу")
                                             .description("<page>/<maxPage>")
                                             .build())
    
                  .ingredient('>', Ingredient.builder() //дефолтный символ для действия "на следующую страницу"
                                             .type(Material.ARROW)
                                             .name("На <nextPage> страницу")
                                             .description("<page>/<maxPage>")
                                             .build())
    
                  .ingredient('0', Ingredient.builder()
                                             .type(Material.GRAY_STAINED_GLASS_PANE)
                                             .name("")
                                             .build())
    
                  .build(this);
  }
  
  public TestPagedView() {
    setDynamic(Arrays.stream(Material.values()) //Получаем горючие материалы и создаём из них ингридиенты
                     .filter(Material::isFuel)
                     .map(x -> new ItemIngredient(new ItemStack(x))) 
                     .collect(Collectors.toList())
    );
    
    getBehavior().bind(getDynamicChar(), ClickType.LEFT, (e) -> { //по нажатию ЛКМ на любой динамический слот
      getDynamic(e.getSlot()).ifPresent(ingredient -> {//получаем ингридиент, и если в этом слоте он есть
        //выполняем какое-то действие
      });
    });
  }
}
```
3. Done
![image](https://github.com/KamikotoTkach/RefreshMenu/assets/110531613/abd1decf-5119-45be-93de-bddff3660ac5)

PagedView автоматически предоставляет плейсхолдеры ```maxPage``` ```page``` ```nextPage``` ```prevPage``` и методы ```updatePlaceholders```, ```updateDynamicContent```


Как показать меню игроку:
1. Получить экземпляр MenuManager и сохранить куда-то:
   ```java
   var menuManager = RefreshMenu.getManager(YourPlugin.getInstance());
   ```
   
2. Открыть меню 
   ```java
   menuManager.open(new YourMenu(), (Player) viewer);
   ```
