package tkachgeek.refreshmenu.configurationUI.valueSupplier;

import java.util.ArrayList;
import java.util.List;

public class ValueSupplierRegistry {
  List<ValueSupplier> suppliers = new ArrayList<>();
  
  public ValueSupplierRegistry() {
    register(new AnvilValueSupplier());
    register(new LocationValueSupplier());
    register(new ItemStackValueSupplier());
  }
  
  public void register(ValueSupplier valueSupplier) {
    suppliers.add(valueSupplier);
  }
  
  public ValueSupplier match(Class<?> type) {
    for (ValueSupplier supplier : suppliers) {
      if(supplier.supports(type)) return supplier;
    }
    return null;
  }
  
  public boolean supports(Class<?> type) {
    return match(type) != null;
  }
}
