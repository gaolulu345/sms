
var vm = new Vue({
    el: "#app",
    data: {
        pageName: 'roles',
        pageTitle: '角色设置',
        adminId: adminId,
        adminRoleId: adminRoleId,
        admin: admin,
        menuPer: menuPer,
        opPer: opPer,

        isAll: false,

        roleList: [],
        operationList: [],
        currentRole: {},
        currentRolePermission: [],
        qrcodeDialogVisible: false,

        roleSelection: [],

        showAddRole: false,
        showEditRole: false,

        addRoleInfo: {
            rolesName: '',
            details: '',
            createId: adminId
        },
        editRoleInfo: {},
     
        menuDisabled: true,
        opDisabled: true,
        changedMenus: {}, //保存有更改的menu权限
        changedOps: {}, //保存有更改的op权限
        showSaveBtn: false,

        dialogLabelWidth: '200px',        
    },
    
    mounted: function() {
    	console.log('mounted......')
        this.getRoleList(false);
    },

    methods: {
        getRoleList: function(isAll) {
            this.$http.post("/api/private/sys/list/roles", {
                pageSize: 1000,
                pageIndex: 1,
                all: isAll
            }).then(function(res){
                let result = res.json().data.result;

                result.forEach(function(val) {
                    val.createTime = formatTimestampToDay(val.createTime)
                    val.modifyTime = formatTimestampToDay(val.modifyTime)
                })
                vm.roleList = result;
                if(vm.roleList.length > 0){
                    vm.currentRole = vm.roleList[0]
                    vm.getCurrentRolePermission(vm.roleList[0].id)
                    vm.isAll = isAll
                }
            })
        },

        getRoleInfo: function(row) {
            vm.currentRole = row;
            this.getCurrentRolePermission(row.id)
        },
        getCurrentRolePermission: function(rolesId) {
            this.$http.post("/api/private/sys/all/roles/permission", {
                rolesId: rolesId
            }).then(function(res){
                let result = res.json();
                vm.currentRolePermission = result.data.menu;
                vm.showSaveBtn = false;
                vm.menuDisabled = true;
                vm.opDisabled = true;
            })
        },

        setCurrent(row) {
            this.$refs.singleTable.setCurrentRow(row);
        },


        // 添加角色
        addRole: function() {
            this.$http.post("/api/private/sys/save/roles", vm.addRoleInfo).then(function(res){
                let result = res.json();
                if(result.code == 200){
                    vm.$message.success('已添加');
                    vm.showAddRole = false;
                    window.location.reload();
                }else {
                    vm.$message.error(result.message)
                }
            })
        },
        // 修改角色信息
        clickEditRole() {
            vm.editRoleInfo = vm.currentRole;
            vm.showEditRole = true;
        },
        editRole: function() {
            let data = {
                id: vm.editRoleInfo.id,
                rolesName: vm.editRoleInfo.rolesName,
                details: vm.editRoleInfo.details
            }
            this.$http.post("/api/private/sys/update/roles", data).then(function(res){
                let result = res.json();
                if(result.code == 200){
                    vm.$message.success('已修改');
                    vm.showEditRole = false;
                    vm.currentRole = vm.editRoleInfo
                    // window.location.reload();
                }else {
                    vm.$message.error(result.message)
                }
            })
        },
        // 批量删除角色
        handleRoleSelectionChange(val) {
            this.roleSelection = val;
        },
        deleteRole: function() {
            if (vm.roleSelection.length == 0) {
                vm.$message.warning('请选择要删除的角色！');
            } else {
                let selection = vm.roleSelection
                let ids = []
                for(var key in selection) {
                    ids.push(selection[key].id)
                }

                vm.$confirm('确认删除这' + ids.length + '个角色?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    vm.$http.post("/api/private/sys/update/roles/deleted", {
                        ids: ids,
                        deleted: true
                    }).then(function(res){
                        let result = res.json();
                        if(result.code == 200){
                            vm.$message.success('已删除');
                            window.location.reload();
                        }else {
                            vm.$message.error(result.message)
                        }
                    })
                }).catch(() => {
                    vm.$message.info('已取消操作')
                });
            }
        },

        handleChangeEnable: function(val){
            console.log(val)
            this.$http.post("/api/private/sys/update/roles/enable", {
                ids: [vm.currentRole.id],
                enable: val
            }).then(function(res){
                let result = res.json();
                if(result.code == 200){
                    vm.$message.success('已更新');
                }else {
                   
                }
            })

        },

        handleChangeDelete: function(val){
            console.log(val)
            this.$http.post("/api/private/sys/update/roles/deleted", {
                ids: [vm.currentRole.id],
                deleted: val
            }).then(function(res){
                let result = res.json();
                if(result.code == 200){
                    vm.$message.success('已更新');
                }else {
                   
                }
            })

        },


        // 给当前角色分配权限
        // 点击修改权限
        clickEditPermission: function() {
            vm.showSaveBtn = true
            vm.menuDisabled = false
            vm.opDisabled = false
            vm.changedMenus = {}
            vm.changedOps = {}
        },
        savePermission: function() {
            console.log('menu..',vm.changedMenus, 'op..',vm.changedOps)
            let menus = vm.changedMenus
            let ops = vm.changedOps
            let menu1 = []
            let menu0 = []
            let ops1 = []
            let ops0 = []
            // if (menus !== {}) {
                for(var key in menus) {
                    menus[key] == true ? menu1.push(parseInt(key)) : menu0.push(parseInt(key))
                }
            // }
            // if (ops) {
                for(var key in ops) {
                    ops[key] == true ? ops1.push(parseInt(key)) : ops0.push(parseInt(key))
                }
            // }

            console.log(menu0, menu1, ops0, ops1)
            if(menu0.length > 0){this.changePermisson('menu', menu0, false)}
            if(menu1.length > 0){this.changePermisson('menu', menu1, true)}
            if(ops0.length > 0){this.changePermisson('operations', ops0, false)}
            if(ops1.length > 0){this.changePermisson('operations', ops1, true)}

            vm.showSaveBtn = false
            vm.menuDisabled = true
            vm.opDisabled = true
        },
        changePermisson: function(name, ids, val){
            this.$http.post("/api/private/sys/update/roles/" + name, {
                rolesId: vm.currentRole.id,
                ids: ids,
                enable: val
            }).then(function(res){
                let result = res.json();
                if(result.code == 200){
                    // vm.$message.success('已修改');
                }else {
                   
                }
            })
        },

        handleCheckMenuPer: function(menuId, val) {
            console.log(menuId, 'menu', val)
            vm.changedMenus[menuId] = val
        },
        handleCheckOpPer: function(opId, val) {
            console.log(opId, 'op', val)
            vm.changedOps[opId] = val
        },

        // 多选
        toggleSelection(rows) {
            if (rows) {
                rows.forEach(row => {
                    this.$refs.multipleTable.toggleRowSelection(row);
                });
            } else {
                this.$refs.multipleTable.clearSelection();
            }
        },
  
    }    
});




$('.nav-each').removeClass('active');
$('.'+vm.pageName).addClass('active');

