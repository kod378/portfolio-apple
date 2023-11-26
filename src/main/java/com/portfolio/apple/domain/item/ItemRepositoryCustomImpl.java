package com.portfolio.apple.domain.item;

import com.portfolio.apple.domain.category.QCategory;
import com.portfolio.apple.domain.itemFile.ItemFile;
import com.portfolio.apple.domain.itemFile.QItemFile;
import com.portfolio.apple.mapper.ItemMapper;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final ItemMapper itemMapper;
    QItem item = QItem.item;
    QCategory category = QCategory.category;
    QItemFile itemFile = QItemFile.itemFile;
    @Override
    public Page<ItemResponseDTO> findPageWithResponseDto(Pageable pageable) {
        List<Item> itemList = queryFactory
                .selectFrom(item)
                .leftJoin(item.category, category).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        itemList.forEach(i -> {
            i.getItemFiles().forEach(ItemFile::getFileName);
        });

        List<ItemResponseDTO> itemResponseDTOList = itemMapper.entityListToResponseDtoList(itemList);

        JPAQuery<Long> countQuery = queryFactory
                .select(item.count())
                .from(item);

        return PageableExecutionUtils.getPage(itemResponseDTOList, pageable, countQuery::fetchOne);
    }

    @Override
    public Optional<ItemResponseDTO> findItemResponseDtoById(Long itemId) {
        Item findItem = queryFactory
                .selectFrom(item).distinct()
                .leftJoin(item.itemFiles, itemFile).fetchJoin()
                .where(item.id.eq(itemId))
                .orderBy(itemFile.orderNumber.asc())
                .fetchOne();

        return Optional.ofNullable(itemMapper.entityToResponseDto(findItem));
    }
}
