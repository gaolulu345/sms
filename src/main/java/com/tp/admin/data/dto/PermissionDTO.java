package com.tp.admin.data.dto;

import com.tp.admin.data.entity.AdminMenu;
import com.tp.admin.data.entity.AdminOperations;
import com.tp.admin.data.entity.AdminPkRolesMenu;
import com.tp.admin.data.entity.AdminPkRolesOperations;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PermissionDTO {

    List<PermissionMenuDTO> menu = new ArrayList<>();

    // 菜单总数
    int totalMenu;

    // 操作总数
    int totalOp;

    @Data
    public class PermissionMenuDTO {

        int id;

        String name;

        boolean enable = false;

        int orderBy;

        List<PermissionOperationsDTO> op = new ArrayList<>();
    }

    @Data
    public class PermissionOperationsDTO {

        int id;

        String name;

        boolean enable = false;

    }

    public PermissionDTO(List<AdminMenu> mList , List<AdminPkRolesMenu> pmList,
                         List<AdminOperations> opList , List<AdminPkRolesOperations>  operationsList) {
        if (null == mList || mList.isEmpty()) {
            return;
        }
        totalMenu = mList.size();

        totalOp = opList.size();
        Map<Integer,AdminPkRolesMenu> map = new HashMap<>();
        for (AdminPkRolesMenu m : pmList){
            map.put(m.getMenuId(),m);
        }
        PermissionMenuDTO menuDTO = null;
        for (AdminMenu m : mList){
            menuDTO = new PermissionMenuDTO();
            if (map.containsKey(m.getId())) {
                menuDTO.setEnable(map.get(m.getId()).isEnable());
            }
            menuDTO.setName(m.getMenuName());
            menuDTO.setId(m.getId());
            menuDTO.setOrderBy(m.getOrderBy());
            if (null != opList || !opList.isEmpty()) {
                menuDTO = build(menuDTO , opList , operationsList);
            }
            menu.add(menuDTO);
        }
    }

    private PermissionMenuDTO build(PermissionMenuDTO  permissionMenuDTO , List<AdminOperations>
            opList , List<AdminPkRolesOperations>  operationsList ){
        Map<Integer,AdminPkRolesOperations> map = new HashMap<>();
        for (AdminPkRolesOperations o : operationsList){
            map.put(o.getOperationsId(),o);
        }
        List<PermissionOperationsDTO> op = new ArrayList<>();
        PermissionOperationsDTO permissionOperationsDTO = null;
        for (AdminOperations o : opList ){
            if (o.getMenuId() == permissionMenuDTO.getId()) {
                permissionOperationsDTO = new PermissionOperationsDTO();
                if (map.containsKey(o.getId())) {
                    permissionOperationsDTO.setEnable(map.get(o.getId()).isEnable());
                }
                permissionOperationsDTO.setId(o.getId());
                permissionOperationsDTO.setName(o.getOperationsName());
                op.add(permissionOperationsDTO);
            }
        }
        permissionMenuDTO.getOp().addAll(op);
        return permissionMenuDTO;
    }

}
