package com.src.filemanager.web.rest;

import com.src.filemanager.service.PermissionMediator;
import com.src.filemanager.service.dto.FileDataDTO;
import com.src.filemanager.service.dto.ItemDTO;
import com.src.filemanager.service.dto.PermissionGroupsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

@RestController
@RequestMapping("/api/stc")
public class StcResource {

    @Autowired
    private PermissionMediator permission;


    @PostMapping("/space")
    public ResponseEntity<PermissionGroupsDTO> createCustomPermissionGroups( )
        throws URISyntaxException {
        PermissionGroupsDTO permissionGroup = permission.createPermissionGroup();
        return ResponseEntity
            .created(new URI("/api/permission-groups/" + permissionGroup.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(permissionGroup.getGroupName(), true, ENTITY_NAME, permissionGroup.getId().toString()))
            .body(permissionGroup);
    }

    @PostMapping("/folder")
    public ResponseEntity<ItemDTO> createCustomPermissionGroupsFolder( )
        throws URISyntaxException {
        ItemDTO folder = permission.createFolder();
        return ResponseEntity
            .created(new URI("/api/items/" + folder.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(folder.getName(), true, ENTITY_NAME, folder.getId().toString()))
            .body(folder);
    }

    @PostMapping("/file-data")
    public ResponseEntity<FileDataDTO> createCustomPermissionGroupsFile( )
        throws URISyntaxException  {
        FileDataDTO folder=null;
        try {
              folder = permission.createFile();
        }catch (Exception e)
        {
            return ResponseEntity
                .created(new URI("/api/file-data/" + folder.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(folder.getItem().getName(), true, ENTITY_NAME, folder.getId().toString()))
                .body(folder);
        }
        return ResponseEntity
            .created(new URI("/api/file-data/" + folder.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(folder.getItem().getName(), true, ENTITY_NAME, folder.getId().toString()))
            .body(folder);
    }

    @GetMapping("/file-data/{fileName}/userEmail/{userEmail}")
    public ResponseEntity<FileDataDTO> getFileData(@PathVariable String fileName,@PathVariable String userEmail) throws URISyntaxException {

        FileDataDTO fileDataDTO = null;
        try {
            fileDataDTO = permission.getFile(fileName,userEmail);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity
            .created(new URI("/api/file-data/" + fileDataDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(fileDataDTO.getItem().getName(), true, ENTITY_NAME, fileDataDTO.getId().toString()))
            .body(fileDataDTO);
    }


}
