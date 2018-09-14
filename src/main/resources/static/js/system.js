
var vm = new Vue({
    el: "#app",
    data: {
        pageName: 'system',
        pageTitle: '系统设置',
        adminId: adminId,
        adminRoleId: adminRoleId,
        admin: admin,
        menuPer: menuPer,
        opPer: opPer,

        // 下拉菜单
        menuOptions: [],
        allResource: [],
        pageResource: [],
        opResource: [],

        isAll: false,
        isAllOp: false,

        menuList: [],
        operationList: [],
        currentMenu: {},

        // 多选的结果
        menuSelection: [],
        opSelection: [],

        showEditMenu: false,
        showAddMenu: false,
        showAddOperation: false,
        showEditOp: false,

        addMenuInfo: {
            menuName: '',
            details: '',
            resource: '',
            orderBy: null,
        },
        editMenuInfo: {

        },
        addOperationInfo: {
            operationsName: '',
            details: '',
            resource: '',
            menuId: null,
        },
        editOpInfo: {

        },

        dialogLabelWidth: '200px',
    },
    
    mounted: function() {
    	console.log('mounted......')
        this.$http.post("/api/private/sys/list/menu", {
            pageSize: 1000,
            pageIndex: 1,
            all: false
        }).then(function(res){
            let result = res.json().data.result;
            let menuOptions = [];
            result.forEach(function(val) {
                val.createTime = formatTimestampToDay(val.createTime)
                val.modifyTime = formatTimestampToDay(val.modifyTime)
                let eachMenu = {}
                eachMenu.value = val.id
                eachMenu.label = val.menuName
                menuOptions.push(eachMenu)
            })
            vm.menuOptions = menuOptions

            vm.menuList = result;
            vm.currentMenu = vm.menuList[0];
            this.$http.post("/api/private/sys/list/operations", {
                pageSize: 1000,
                pageIndex: 1,
                menuId: ''
                // menuId: vm.menuList[0].id || ''
            }).then(function(res){
                let result = res.json();
                vm.operationList = result.data.result;
            })
        })

        // this.getAllMenuResource()
        // this.getAllOperationResource()


        
    },

    methods: {
        // 获取所有未分配的资源
        // 获取所有未分配菜单地址
        getAvillableMenuResource: function() {
            this.$http.post("/api/private/sys/menu/maps", {}).then(function(res){
                let result = res.json();
                vm.pageResource = result.data;
            })
        },
        getAvillableOperationResource: function() {
            this.$http.post("/api/private/sys/operations/maps", {}).then(function(res){
                let result = res.json();
                vm.opResource = result.data;
            })
        },
        
        // <---------------------菜单相关请求----------------------->
        // 获取菜单列表
        getMenuList: function(isAll) {
            console.log('isAll....', isAll)
            this.$http.post("/api/private/sys/list/menu", {
                pageSize: 1000,
                pageIndex: 1,
                all: isAll
            }).then(function(res){
                let result = res.json().data.result;
                result.forEach(function(val) {
                    val.createTime = formatTimestampToDay(val.createTime)
                    val.modifyTime = formatTimestampToDay(val.modifyTime)
                })
                vm.menuList = result;
                vm.currentMenu = vm.menuList[0];
                vm.getOperationList(vm.menuList[0].id, vm.isAllOp);
                vm.isAll = isAll
            })
        },
        // 表格相关的事件绑定
        indexMethod: function(index) {
            return index + 1
        },
        setCurrent(row) {
            this.$refs.singleTable.setCurrentRow(row);
        },

        // 点击菜单，获取当前菜单信息
        getMenuInfo: function(row) {
            vm.currentMenu = row;
            this.getOperationList(row.id, vm.isAllOp);
        },

        // 添加菜单
        clickShowAddMenu: function() {
            vm.showAddMenu = true;
            vm.getAvillableMenuResource()
        },
        addMenu: function() {
            this.$http.post("/api/private/sys/save/menu", vm.addMenuInfo).then(function(res){
                let result = res.json();
                if(result.code == 200){
                    vm.$message.success('已添加');
                    vm.showAddMenu = false;
                    window.location.reload();
                }else {
                    vm.$message.error(result.message)
                }
            })
        },
        // 修改菜单
        openEditMenuDialog: function() {
            vm.editMenuInfo = vm.currentMenu;
            vm.getAvillableMenuResource()
            vm.showEditMenu = true
        },
        editMenu: function() {
            let infos = {
                id: vm.editMenuInfo.id,
                menuName: vm.editMenuInfo.menuName,
                details: vm.editMenuInfo.details,
                resource: vm.editMenuInfo.resource,
                orderBy: vm.editMenuInfo.orderBy,
            }
            this.$http.post("/api/private/sys/update/menu", infos).then(function(res){
                let result = res.json();
                if(result.code == 200){
                    vm.$message.success('已修改');
                    vm.showAddOperation = false;
                    vm.currentMenu = vm.editMenuInfo;
                    vm.editMenuInfo = {}
                }else {
                    vm.$message.error(result.message)
                }
            })
        },
        // 批量删除菜单
        deleteMenu: function() {
            if (vm.menuSelection.length == 0) {
                vm.$message.warning('请选择要删除的菜单！');
            } else {
                let selection = vm.menuSelection
                let ids = []
                for(var key in selection) {
                    ids.push(selection[key].id)
                }

                vm.$confirm('确认删除这' + ids.length + '个菜单?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    vm.$http.post("/api/private/sys/update/menu/deleted", {
                        ids: ids,
                        deleted: true
                    }).then(function(res){
                        let result = res.json();
                        if(result.code == 200){
                            vm.$message.success('已删除');
                            // window.location.reload();
                        }else {
                            vm.$message.error(result.message)
                        }
                    })
                }).catch(() => {
                    vm.$message.info('已取消操作')
                });
            }
        },
        // 单个启用菜单
        handleMenuChangeEnable: function(val) {
            vm.$http.post("/api/private/sys/update/menu/enable", {
                ids: [vm.currentMenu.id],
                enable: val
            }).then(function(res){
                let result = res.json();
                if(result.code == 200){
                    vm.$message.success('已更新');
                }else {
                    vm.$message.error(result.message)
                }
            })
        },
        // 单个删除菜单
        handleMenuChangeDelete: function(val) {
            vm.$http.post("/api/private/sys/update/menu/deleted", {
                ids: [vm.currentMenu.id],
                deleted: val
            }).then(function(res){
                let result = res.json();
                if(result.code == 200){
                    vm.$message.success('已更新');
                }else {
                    vm.$message.error(result.message)
                }
            })
        },



        // <-----------------------操作相关请求------------------------->
        // 获取操作列表
        getOperationList: function(menuId, isAllOp) {
            this.$http.post("/api/private/sys/list/operations", {
                pageSize: 1000,
                pageIndex: 1,
                menuId: menuId,
                all: isAllOp
            }).then(function(res){
                let result = res.json();
                vm.operationList = result.data.result;
                vm.isAllOp = isAllOp;
            })
        },

        // 添加操作
        openAddOperationDialog: function() {
            vm.addOperationInfo.menuId = vm.currentMenu.id;
            console.log(vm.addOperationInfo)
            vm.getAvillableOperationResource()
            vm.showAddOperation = true
        },
        addOperation: function() {
            this.$http.post("/api/private/sys/save/operations", vm.addOperationInfo).then(function(res){
                let result = res.json();
                if(result.code == 200){
                    vm.$message.success('已添加');
                    vm.showAddOperation = false;
                    vm.getOperationList(vm.currentMenu.id, vm.isAllOp)
                }else {
                    vm.$message.error(result.message)
                }
            })
        },
        // 批量删除操作
        deleteOp: function() {
            if (vm.opSelection.length == 0) {
                vm.$message.warning('请选择要删除的操作！');
            } else {
                let selection = vm.opSelection
                let ids = []
                for(var key in selection) {
                    ids.push(selection[key].id)
                }

                vm.$confirm('确认删除这' + ids.length + '个操作?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    vm.$http.post("/api/private/sys/update/operations/deleted", {
                        ids: ids,
                        deleted: true
                    }).then(function(res){
                        let result = res.json();
                        if(result.code == 200){
                            vm.$message.success('已删除');
                            // window.location.reload();

                            vm.getOperationList(vm.currentMenu.id, vm.isAllOp)
                        }else {
                            vm.$message.error(result.message)
                        }
                    })
                }).catch(() => {
                    vm.$message.info('已取消操作')
                });
            }
        },
        // 单个启用操作
        handleOpChangeEnable: function(val, id) {
            console.log(val, id)
            vm.$http.post("/api/private/sys/update/operations/enable", {
                ids: [id],
                enable: val
            }).then(function(res){
                let result = res.json();
                if(result.code == 200){
                    vm.$message.success('已更新');
                }else {
                    vm.$message.error(result.message)
                }
            })
        },
        // 单个删除操作
        handleOpChangeDelete: function(val, id) {
            vm.$http.post("/api/private/sys/update/operations/deleted", {
                ids: [id],
                deleted: val
            }).then(function(res){
                let result = res.json();
                if(result.code == 200){
                    vm.$message.success('已更新');
                }else {
                    vm.$message.error(result.message)
                }
            })
        },

        // 修改操作信息
        clickEditOp(row) {
            vm.editOpInfo = row;
            vm.getAvillableOperationResource()
            vm.showEditOp = true;
        },
        editOp: function() {
            let infos = {
                id: vm.editOpInfo.id,
                operationsName: vm.editOpInfo.operationsName,
                details: vm.editOpInfo.details,
                resource: vm.editOpInfo.resource,
                // resource: '/pages/ter/status',
                menuId: vm.editOpInfo.menuId,
            }
            this.$http.post("/api/private/sys/update/operations", infos).then(function(res){
                let result = res.json();
                if(result.code == 200){
                    vm.$message.success('已修改');
                    vm.showEditOp = false;
                    vm.getOperationList(vm.currentMenu.id, vm.isAllOp)
                }else {
                    vm.$message.error(result.message)
                }
            })
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
        handleMenuSelectionChange(val) {
            this.menuSelection = val;
        },
        handleOpSelectionChange(val) {
            this.opSelection = val;
        }  
    }    
});




$('.nav-each').removeClass('active');
$('.'+vm.pageName).addClass('active');

