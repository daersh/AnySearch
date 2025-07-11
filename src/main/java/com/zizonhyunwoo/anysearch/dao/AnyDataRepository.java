package com.zizonhyunwoo.anysearch.dao;

import com.zizonhyunwoo.anysearch.domain.AnyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnyDataRepository extends JpaRepository<AnyData, UUID> {
    void deleteByIdAndUserInfo_Id(UUID id, UUID id1);


    @Query( "SELECT a.type FROM AnyData a GROUP BY a.type")
    List<String> findType();
}

