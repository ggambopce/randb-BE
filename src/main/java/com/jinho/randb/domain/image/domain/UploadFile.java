package com.jinho.randb.domain.image.domain;

import com.jinho.randb.domain.profile.domain.Profile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "img_id")
    private Long id;

    @Column(length = 400)
    private String originFileName;      // 실제 파일명

    @Column(length = 400,name = "store_file_name")
    private String storeFileName;       // DB에 저장될 파일명

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Profile profile;

    public UploadFile(String originFileName, String storeFileName) {
        this.originFileName = originFileName;
        this.storeFileName = storeFileName;
    }

    public void update(String originFileName, String storeFileName) {
        this.originFileName = originFileName;
        this.storeFileName = storeFileName;
    }

    public static UploadFile createUploadFile(String originFileName, String storeFileName) {
        return UploadFile.builder()
                .originFileName(originFileName)
                .storeFileName(storeFileName)
                .build();
    }
}
