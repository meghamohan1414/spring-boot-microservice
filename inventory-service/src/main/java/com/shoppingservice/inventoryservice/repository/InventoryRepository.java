package com.shoppingservice.inventoryservice.repository;

import com.shoppingservice.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Query(value = "select * " +
            "from t_inventory inv where inv.sku_code in ?"
            , nativeQuery = true)
    List<Inventory> findBySkuCodeIn(List<String> skuCodes);
}
