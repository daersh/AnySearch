package com.zizonhyunwoo.anysearch.domain;

import com.zizonhyunwoo.anysearch.request.AnyDataInsertRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AnyData")
public class AnyData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "any_data_id", unique = true, nullable = false)
    private UUID id;

    // 어떤 데이터인지 구분
    @Column(name = "type",length = 100,nullable = false)
    private String type;
    // 데이터 타이틀
    @Column(name = "title",length = 100)
    private String title;
    // 데이터 내용
    @Column(name = "description",length = 1000)
    private String description;
    // 데이터 부가 정보명 (구분자: '†', 예시: 가격†태그†상표†...)
    @Column(name = "add_info",length = 100)
    private String addInfo;
    // 데이터 부가내용 (구분자: '†', 1000†일상,추천†삼성†...)
    @Column(name = "add_detail",length = 100)
    private String addDetail;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    @Column(name = "is_active")
    private Boolean isActive;

    @JoinColumn(name = "user_id", nullable = false,updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UserInfo userInfo;

    public AnyData(UserInfo userInfo, AnyDataInsertRequest anyData) {
        this.type = anyData.type();
        this.title = anyData.title();
        this.description = anyData.description();
        this.addInfo = anyData.addInfo();
        this.addDetail = anyData.addDetail();
        this.isActive = anyData.isActive();
        this.createdAt = this.createdAt!=null?this.createdAt:LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.userInfo = userInfo;
    }
}
