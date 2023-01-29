package com.src.filemanager.repository;

import com.src.filemanager.domain.FileData;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the FileData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileDataRepository extends JpaRepository<FileData, Long> {


    @Query(
        value = "select * from file_data f ,item i,permission_users_group pug\n" +
            "         where f.item_id=i.id\n" +
            "         and pug.id=i.permission_users_group_id\n" +
            "         and pug.user_email= :emailAddress \n" +
            "         and i.name= :fileName ",
        nativeQuery = true)
     Optional<FileData> findfileByCriticia(@Param("name") String name, @Param("userEmail") String userEmail);
}
