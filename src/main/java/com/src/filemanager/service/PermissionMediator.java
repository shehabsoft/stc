package com.src.filemanager.service;

import com.src.filemanager.domain.enumeration.ItemType;
import com.src.filemanager.domain.enumeration.PermissionLevel;
import com.src.filemanager.service.dto.FileDataDTO;
import com.src.filemanager.service.dto.ItemDTO;
import com.src.filemanager.service.dto.PermissionGroupsDTO;
import com.src.filemanager.service.dto.PermissionUsersGroupDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.transaction.Transactional;
import java.io.*;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class PermissionMediator {
    private final PermissionGroupsService permissionGroupsService;

    private final PermissionUsersGroupService permissionUsersGroupService;

    private final ItemService itemService;

    private  final FileDataService fileDataService;

    public PermissionMediator(PermissionGroupsService permissionGroupsService, PermissionUsersGroupService permissionUsersGroupService, ItemService itemService,FileDataService fileDataService)
    {
        this.permissionGroupsService=permissionGroupsService;
        this.permissionUsersGroupService=permissionUsersGroupService;
        this.itemService=itemService;
        this.fileDataService=fileDataService;
    }
    //first api
    @Transactional
    public PermissionGroupsDTO createPermissionGroup()
    {
        PermissionGroupsDTO permissionGroupsDTO =new PermissionGroupsDTO();
        //assign a permission group like admin
        permissionGroupsDTO.setGroupName("admin");
        permissionGroupsDTO.setId(new Random().nextLong());
        PermissionGroupsDTO groupsDTO = this.permissionGroupsService.save(permissionGroupsDTO);

        PermissionUsersGroupDTO permissionUsersGroupDTOView=new PermissionUsersGroupDTO();
        permissionUsersGroupDTOView.setPermissionGroup(groupsDTO);
        //user with VIEW access
        permissionUsersGroupDTOView.setPermissionLevel(PermissionLevel.VIEW);
        permissionUsersGroupDTOView.setId(new Random().nextLong());
        permissionUsersGroupDTOView.setUserEmail("shehabsoft96@gmail.com");
        PermissionUsersGroupDTO permissionUsersGroupDTO = this.permissionUsersGroupService.save(permissionUsersGroupDTOView);

        PermissionUsersGroupDTO permissionUsersGroupDTOEdit=new PermissionUsersGroupDTO();
        permissionUsersGroupDTOEdit.setPermissionGroup(groupsDTO);
        //another one with EDIT access
        permissionUsersGroupDTOEdit.setPermissionLevel(PermissionLevel.EDIT);
        permissionUsersGroupDTOEdit.setId(new Random().nextLong());
        permissionUsersGroupDTOEdit.setUserEmail("shehabsoft94@gmail.com");
        PermissionUsersGroupDTO permissionUsersGroupDTO1 = this.permissionUsersGroupService.save(permissionUsersGroupDTOEdit);

        ItemDTO itemDTOView=new ItemDTO();

        //create space called “stc-assessments”
        itemDTOView.setName("stc-assessments");
        itemDTOView.setType(ItemType.SPACE);
        itemDTOView.setPermissionUsersGroup(permissionUsersGroupDTO);
        this.itemService.save(itemDTOView);
        ItemDTO itemDTOEdit=new ItemDTO();

        //create space called “stc-assessments”
        itemDTOEdit.setName("stc-assessments");
        itemDTOView.setPermissionUsersGroup(permissionUsersGroupDTO1);
        itemDTOEdit.setType(ItemType.SPACE);
        this.itemService.save(itemDTOEdit);

        return permissionGroupsDTO;

    }
    @Transactional
    //second api
    public ItemDTO createFolder()
    {    //get user with edit action

        Optional<PermissionGroupsDTO> adminPermissionGroupsDTO = this.permissionGroupsService.findOne("admin");
        Optional<PermissionUsersGroupDTO> editPermissionUserGroup = this.permissionUsersGroupService.findOneByPermissionLevel(PermissionLevel.EDIT,adminPermissionGroupsDTO.get());
        //get spce stc assessement with edit permission

        ItemDTO itemDTO=new ItemDTO();
        itemDTO.setId(new Random().nextLong());
        itemDTO.setPermissionUsersGroup(editPermissionUserGroup.get());
        //create folder called “Backend”
        itemDTO.setName("Backend");
        itemDTO.setType(ItemType.FOLDER);

        itemService.save(itemDTO);

        return  itemDTO;

    }
    @Transactional
//third api
    public FileDataDTO createFile() throws IOException {
        Optional<PermissionGroupsDTO> adminPermissionGroupsDTO = this.permissionGroupsService.findOne("admin");
        Optional<PermissionUsersGroupDTO> editPermissionUserGroup = this.permissionUsersGroupService.findOneByPermissionLevel(PermissionLevel.EDIT,adminPermissionGroupsDTO.get());
        Optional<ItemDTO> backendItemDTO = itemService.findByNameAndUserPermissionGroup("Backend",editPermissionUserGroup.get());

        ItemDTO itemDTO=new ItemDTO();
        itemDTO.setId(new Random().nextLong());
        itemDTO.setPermissionUsersGroup(backendItemDTO.get().getPermissionUsersGroup());
        //create File called “stc-assessments”
        itemDTO.setName("stc-assessment.pdf");
        itemDTO.setType(ItemType.FILE);
        ClassLoader classLoader = getClass().getClassLoader();
        File file = ResourceUtils.getFile("classpath:stc-assessment.pdf");
        InputStream in = new FileInputStream(file);
        ItemDTO itemDTO1 = itemService.save(itemDTO);
        //create File Data  called “stc-assessments”
        FileDataDTO fileDataDTO=new FileDataDTO();
        fileDataDTO.setId(new Random().nextLong());
        fileDataDTO.setBinery(in.readAllBytes());
        fileDataDTO.setItem(itemDTO1);
        fileDataDTO.setBineryContentType("PDF");
        fileDataService.save(fileDataDTO);
        return fileDataDTO;



    }

    public FileDataDTO getFile(String fileName,String userEmail) throws IOException {
        Optional<FileDataDTO> oneByName = fileDataService.findOneByName(fileName, userEmail);
        if(oneByName.isPresent())
          return oneByName.get();
        else
           throw new FileNotFoundException("File Not Found");
    }



}
