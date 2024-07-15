package com.vubq.joyboystore.repositories;

import com.vubq.joyboystore.entities.Image;
import com.vubq.joyboystore.enums.EImageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {

    List<Image> findAllBySecondaryId(String secondaryId);

    List<Image> findAllBySecondaryIdAndType(String secondaryId, EImageType type);
}
