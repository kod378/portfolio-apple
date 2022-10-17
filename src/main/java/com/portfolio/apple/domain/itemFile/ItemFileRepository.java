package com.portfolio.apple.domain.itemFile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemFileRepository extends JpaRepository<ItemFile, Long> {

    @Query("select i from ItemFile i where i.item.id = :itemId order by i.orderNumber")
    List<ItemFile> findByItemId(@Param("itemId") Long itemId);

    @Query("select if.fileName from ItemFile if")
    List<String> findAllFileNames();
}
