package com.zizonhyunwoo.anysearch.anyData.dao;

import com.zizonhyunwoo.anysearch.anyData.domain.AnyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@Repository
public interface AnyDataRepository extends JpaRepository<AnyData, UUID> {
    void deleteByIdAndUserInfo_Id(UUID id, UUID id1);


    @Query( "SELECT a.type FROM AnyData a GROUP BY a.type")
    Page<String> findType(Pageable pageable);

    boolean existsByType(String type);
}

